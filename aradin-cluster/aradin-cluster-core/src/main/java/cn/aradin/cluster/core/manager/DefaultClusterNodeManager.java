package cn.aradin.cluster.core.manager;

import java.util.HashSet;
import java.util.Set;

public class DefaultClusterNodeManager implements IClusterNodeManager {

	private Set<String> nodes = new HashSet<String>();

	@Override
	public void nodeInit(Set<String> nodes) {
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
	public Set<String> nodeNames() {
		// TODO Auto-generated method stub
		return this.nodes;
	}

}
