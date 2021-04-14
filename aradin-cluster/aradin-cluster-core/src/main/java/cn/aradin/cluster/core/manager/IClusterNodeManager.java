package cn.aradin.cluster.core.manager;

import java.util.Set;

public interface IClusterNodeManager {
	/**
	 * Initial nodes in memory
	 * @param nodes 
	 */
	public void nodeInit(Set<String> nodes);
	
	/**
	 * Add new node
	 * @param node The name of new node
	 */
	public void nodeAdded(String node);
	
	/**
	 * Remove node
	 * @param node The name of target node
	 */
	public void nodeRemoved(String node);
	
	/**
	 * Get node num
	 * @return The number of nodes in the same cluster
	 */
	public Integer nodeNum();
	
	/**
	 * Get all nodes
	 * @return All nodes
	 */
	public Set<String> nodeNames();
}
