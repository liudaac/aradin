package cn.aradin.cluster.zookeeper.starter.handler;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.zookeeper.CreateMode;

import cn.aradin.cluster.core.manager.IClusterNodeManager;
import cn.aradin.cluster.core.properties.ClusterProperties;
import cn.aradin.spring.core.enums.RegisterType;
import cn.aradin.zookeeper.boot.starter.handler.INodeHandler;
import cn.aradin.zookeeper.boot.starter.manager.ZookeeperClientManager;
import cn.aradin.zookeeper.boot.starter.properties.Zookeeper;
import cn.aradin.zookeeper.boot.starter.properties.ZookeeperProperties;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ClusterNodeHandler implements INodeHandler {

	private ClusterProperties clusterProperties;
	
	private IClusterNodeManager clusterNodeManager;

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
			if (!result.isPresent()) {
				return;
			}
		}
		throw new RuntimeException("Cluster's Zookeeper is not config");
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
			client.create().withMode(CreateMode.EPHEMERAL).forPath(clusterProperties.getNodeName());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if (log.isErrorEnabled()) {
				log.error("Node Registed Failed, {}", e.getMessage());
			}
			throw new RuntimeException(e.getCause());
		}
	}

	@Override
	public boolean support(PathChildrenCacheEvent event) {
		// TODO Auto-generated method stub
		String path = event.getData().getPath();
		path = path.substring(0, path.lastIndexOf("/"));
		if (path.contains("/")) {
			String cluster = path.substring(path.lastIndexOf("/") + 1);
			if (clusterProperties.getZookeeperAddressId().equalsIgnoreCase(cluster)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void handler(CuratorFramework client, PathChildrenCacheEvent event) {
		// TODO Auto-generated method stub
		switch (event.getType()) {
		case INITIALIZED:
			if (CollectionUtils.isNotEmpty(event.getInitialData())) {
				Set<String> nodes = new HashSet<String>();
				event.getInitialData().forEach(data -> {
					nodes.add(data.getPath());
				});
				clusterNodeManager.nodeInit(nodes);
			}
			break;
		case CHILD_ADDED:
			clusterNodeManager.nodeAdded(event.getData().getPath());
			break;
		case CHILD_REMOVED:
			clusterNodeManager.nodeRemoved(event.getData().getPath());
			break;
		default:
			break;
		}
	}
}
