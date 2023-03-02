package cn.aradin.cluster.zookeeper.starter.handler;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;

import cn.aradin.cluster.core.manager.IClusterNodeManager;
import cn.aradin.cluster.core.properties.ClusterProperties;
import cn.aradin.spring.core.enums.RegisterType;
import cn.aradin.zookeeper.boot.starter.handler.INodeHandler;
import cn.aradin.zookeeper.boot.starter.manager.ZookeeperClientManager;
import cn.aradin.zookeeper.boot.starter.properties.Zookeeper;
import cn.aradin.zookeeper.boot.starter.properties.ZookeeperProperties;

public class ClusterNodeHandler implements INodeHandler {

	private final static Logger log = LoggerFactory.getLogger(ClusterNodeHandler.class);
	
	private ClusterProperties clusterProperties;

	private IClusterNodeManager clusterNodeManager;

	private Integer registerRetry = 0;

	public ClusterNodeHandler(ClusterProperties clusterProperties, ZookeeperProperties zookeeperProperties,
			IClusterNodeManager clusterNodeManager) {
		// TODO Auto-generated constructor stub
		if (clusterProperties == null) {
			throw new RuntimeException("Cluster is not config");
		}
		if (!RegisterType.zookeeper.equals(clusterProperties.getRegisterType())) {
			throw new RuntimeException("Cluster is not registed on zookeeper");
		}
		this.clusterProperties = clusterProperties;
		if (zookeeperProperties != null && clusterProperties != null
				&& CollectionUtils.isNotEmpty(zookeeperProperties.getAddresses())) {
			Optional<Zookeeper> result = zookeeperProperties.getAddresses().stream()
					.filter(zookeeper -> zookeeper.getId().equals(clusterProperties.getZookeeperAddressId())).findAny();
			if (result.isPresent()) {
				this.clusterNodeManager = clusterNodeManager;
				return;
			}
		}
		throw new RuntimeException("Cluster's Zookeeper is not config");
	}

	private Integer rebaseNode(List<String> existNodes, Integer maxNode) {
		if (existNodes == null || existNodes.size() == 0) {
			return 0;
		}
		if (existNodes.size() >= maxNode) {
			throw new RuntimeException("Cluster Node Is OutSize With Nodes " + JSONObject.toJSONString(existNodes));
		}
		List<Integer> nodes = Lists.newArrayList();
		existNodes.forEach(existNode -> {
			if (StringUtils.isNumeric(existNode)) {
				nodes.add(Integer.parseInt(existNode));
			}
		});
		Collections.sort(nodes);
		for (int i = 0; i < maxNode; i++) {
			if (nodes.size() > i) {
				if (nodes.get(i) != i) {
					return i;
				}
			} else {
				return i;
			}
		}
		throw new RuntimeException("Cluster Node Is OutSize");
	}

	private void registerNode(CuratorFramework client, String nodeName) {
		if (registerRetry++ > 5) {
			registerRetry = 0;
			throw new RuntimeException("Cluster Node Retry Too Many Times");
		}
		Integer index = -1;
		try {
			List<String> childs = client.getChildren().forPath("/" + clusterProperties.getZookeeperAddressId());
			index = rebaseNode(childs, clusterProperties.getMaxNode());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e.getCause());
		}
		try {
			client.create().withMode(CreateMode.EPHEMERAL).forPath(
					"/" + clusterProperties.getZookeeperAddressId() + "/" + String.valueOf(index),
					clusterProperties.getNodeName().getBytes());
			registerRetry = 0;
			clusterNodeManager.setCurrentIndex(index);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			if (log.isWarnEnabled()) {
				log.warn("Cluster Register Retry Failed {}", registerRetry);
			}
			registerNode(client, nodeName);
		}
	}

	@Override
	public void init(ZookeeperClientManager clientManager) {
		// TODO Auto-generated method stub
		if (StringUtils.isBlank(clusterProperties.getNodeName())) {
			try {
				if (clusterProperties.isPreferIpAddress()) {
					clusterProperties.setNodeName(Inet4Address.getLocalHost().getHostAddress());
				} else {
					clusterProperties.setNodeName(Inet4Address.getLocalHost().getHostName());
				}
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new RuntimeException(e.getCause());
			}
		}
		if (log.isDebugEnabled()) {
			log.debug("Node Registing, {}", clusterProperties.getNodeName());
		}
		CuratorFramework client = clientManager.getClient(clusterProperties.getZookeeperAddressId());
		try {
			registerNode(client, clusterProperties.getNodeName());
			client.getConnectionStateListenable().addListener(this);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if (log.isErrorEnabled()) {
				log.error("Node Registed Failed, {}", e.getMessage());
			}
			throw new RuntimeException(e.getCause());
		}
	}

	private boolean supportPath(String path) {
		if (path.contains("/")) {
			path = path.substring(0, path.lastIndexOf("/"));
			if (path.contains("/")) {
				String cluster = path.substring(path.lastIndexOf("/") + 1);
				if (log.isDebugEnabled()) {
					log.debug("Parse Cluster {}", cluster);
				}
				if (clusterProperties.getZookeeperAddressId().equalsIgnoreCase(cluster)) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean support(PathChildrenCacheEvent event) {
		// TODO Auto-generated method stub
		String path = event.getData().getPath();
		if (log.isDebugEnabled()) {
			log.debug("Received Event Path {} {}", path, event.getType());
		}
		return supportPath(path);
	}

	@Override
	public void handler(CuratorFramework client, PathChildrenCacheEvent event) {
		// TODO Auto-generated method stub
		try {
			switch (event.getType()) {
			case INITIALIZED:
				if (CollectionUtils.isNotEmpty(event.getInitialData())) {
					Map<Integer, String> nodes = new HashMap<Integer, String>(clusterProperties.getMaxNode());
					event.getInitialData().forEach(data -> {
						if (supportPath(data.getPath())) {
							String path = data.getPath().substring(data.getPath().lastIndexOf("/") + 1);
							if (StringUtils.isNumeric(path)) {
								nodes.put(Integer.parseInt(path), new String(data.getData()));
							}
						}
					});
					if (log.isDebugEnabled()) {
						log.debug("Find Cluster Nodes {}", JSONObject.toJSONString(nodes));
					}
					clusterNodeManager.nodeInit(nodes);
				}
				break;
			case CHILD_ADDED:
				String node = event.getData().getPath().substring(event.getData().getPath().lastIndexOf("/") + 1);
				if (log.isDebugEnabled()) {
					log.debug("Node Adding {}", node);
				}
				if (StringUtils.isNumeric(node)) {
					clusterNodeManager.nodeAdded(Integer.parseInt(node), new String(event.getData().getData()));
					if (log.isDebugEnabled()) {
						log.debug("Node Added {}", JSONObject.toJSONString(clusterNodeManager.nodeNames()));
					}
				}
				break;
			case CHILD_REMOVED:
				node = event.getData().getPath().substring(event.getData().getPath().lastIndexOf("/") + 1);
				if (log.isDebugEnabled()) {
					log.debug("Node Removing {}", node);
				}
				if (StringUtils.isNumeric(node)) {
					clusterNodeManager.nodeRemoved(Integer.parseInt(node), new String(event.getData().getData()));
					if (log.isDebugEnabled()) {
						log.debug("Node Removed {}", JSONObject.toJSONString(clusterNodeManager.nodeNames()));
					}
				}
				break;
			default:
				break;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	@Override
	public void stateChanged(CuratorFramework client, ConnectionState newState) {
		// TODO Auto-generated method stub
		if (newState == ConnectionState.LOST) {
			while (true) {
				try {
					if (client.getZookeeperClient().blockUntilConnectedOrTimedOut()) {
						registerNode(client, clusterProperties.getNodeName());
						log.error("Re Register Succeed {}", clusterProperties.getNodeName());
						break;
					}
				} catch (InterruptedException e) {
					// TODO: log something
					break;
				} catch (Exception e) {
					// TODO: log something
					log.error("Re Register Failed {}", e.getMessage());
				}
			}
		}
	}
}
