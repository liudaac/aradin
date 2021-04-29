package cn.aradin.cluster.core.manager;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import cn.aradin.cluster.core.properties.ClusterProperties;

public class DefaultClusterNodeManager implements IClusterNodeManager {

	private List<String> nodes = new ArrayList<String>();

	private ClusterProperties clusterProperties;
	
	public DefaultClusterNodeManager(ClusterProperties clusterProperties) {
		// TODO Auto-generated constructor stub
		this.clusterProperties = clusterProperties;
	}
	
	@Override
	public void nodeInit(List<String> nodes) {
		// TODO Auto-generated method stub
		this.nodes.addAll(nodes);
	}

	@Override
	public void nodeAdded(String node) {
		// TODO Auto-generated method stub
		this.nodes.add(node);
	}

	@Override
	public void nodeRemoved(String node) {
		// TODO Auto-generated method stub
		this.nodes.remove(node);
	}

	@Override
	public Integer nodeNum() {
		// TODO Auto-generated method stub
		return this.nodes.size();
	}

	@Override
	public List<String> nodeNames() {
		// TODO Auto-generated method stub
		return this.nodes;
	}

	@Override
	public int nodeIndex(String node) {
		// TODO Auto-generated method stub
		return this.nodes.indexOf(node);
	}

	@Override
	public int currentIndex() {
		// TODO Auto-generated method stub
		if (StringUtils.isBlank(clusterProperties.getNodeName())) {
			throw new RuntimeException("Cluster Not Initial");
		}
		return this.nodeIndex(clusterProperties.getNodeName());
	}

}
