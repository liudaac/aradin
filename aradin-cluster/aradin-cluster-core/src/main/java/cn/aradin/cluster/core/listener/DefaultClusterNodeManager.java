package cn.aradin.cluster.core.listener;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class DefaultClusterNodeManager implements IClusterNodeManager {

	private AtomicInteger nodeNum = new AtomicInteger(0);

	private Set<String> nodes = new HashSet<String>();

	@Override
	public void nodeInit(Set<String> nodes) {
		// TODO Auto-generated method stub
		this.nodes.addAll(nodes);
		nodeNum.set(nodes.size());
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
		return this.nodeNum.get();
	}

	@Override
	public Set<String> nodeNames() {
		// TODO Auto-generated method stub
		return this.nodes;
	}

}
