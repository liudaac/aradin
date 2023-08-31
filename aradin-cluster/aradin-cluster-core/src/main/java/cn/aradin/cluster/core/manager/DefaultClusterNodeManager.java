package cn.aradin.cluster.core.manager;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.aradin.cluster.core.properties.ClusterProperties;

public class DefaultClusterNodeManager implements IClusterNodeManager {

	private final static Logger log = LoggerFactory.getLogger(DefaultClusterNodeManager.class);
	
	private Map<Integer, String> nodes = new ConcurrentHashMap<Integer, String>();
	
	private int currentIndex = -1;
	
	private ClusterProperties clusterProperties;
	
	public DefaultClusterNodeManager(ClusterProperties clusterProperties) {
		// TODO Auto-generated constructor stub
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
				log.error("Cluster init failed for the reason {}", e.getMessage());
				throw new RuntimeException(e.getCause());
			}
		}
		this.clusterProperties = clusterProperties;
	}
	
	@Override
	public synchronized void nodeInit(Map<Integer, String> nodes) {
		// TODO Auto-generated method stub
		this.nodes.clear();
		for(Integer index:nodes.keySet()) {
			this.nodes.put(index, nodes.get(index));
		}
	}

	@Override
	public synchronized void nodeAdded(Integer index, String node) {
		// TODO Auto-generated method stub
		this.nodes.put(index, node);
	}

	@Override
	public synchronized void nodeRemoved(Integer index, String node) {
		// TODO Auto-generated method stub
		String value = this.nodes.get(index);
		if (StringUtils.isNotBlank(value) && value.equals(node)) {
			this.nodes.remove(index);
		}
	}

	@Override
	public Integer nodeNum() {
		// TODO Auto-generated method stub
		return this.nodes.size();
	}

	@Override
	public Collection<String> nodeNames() {
		// TODO Auto-generated method stub
		return this.nodes.values();
	}

	@Override
	public int nodeIndex(String node) {
		// TODO Auto-generated method stub
		for(Integer index:nodes.keySet()) {
			if (nodes.get(index).equals(node)) {
				return index;
			}
		}
		return -1;
	}

	@Override
	public int currentIndex() {
		// TODO Auto-generated method stub
		if (StringUtils.isBlank(clusterProperties.getNodeName())) {
			throw new RuntimeException("Cluster Not Initial");
		}
		return this.currentIndex;
	}

	@Override
	public void setCurrentIndex(int currentIndex) {
		// TODO Auto-generated method stub
		this.currentIndex = currentIndex;
	}

	@Override
	public String currentNode() {
		// TODO Auto-generated method stub
		return clusterProperties.getNodeName();
	}
}
