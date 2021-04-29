package cn.aradin.cluster.core.manager;

import java.util.List;

public interface IClusterNodeManager {
	/**
	 * Initial nodes in memory
	 * @param nodes 
	 */
	public void nodeInit(List<String> nodes);
	
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
	 * Get Specific Node Index
	 * @param node
	 * @return
	 */
	public int nodeIndex(String node);
	
	/**
	 * Get node num
	 * @return The number of nodes in the same cluster
	 */
	public Integer nodeNum();
	
	/**
	 * Get all nodes
	 * @return All nodes
	 */
	public List<String> nodeNames();
	
	/**
	 * Get current node index
	 * @return
	 */
	public int currentIndex();
}
