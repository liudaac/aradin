package cn.aradin.cluster.core.manager;

import java.util.Collection;
import java.util.Map;

public interface IClusterNodeManager {
	/**
	 * Initial nodes in memory
	 * @param nodes 
	 */
	public void nodeInit(Map<Integer, String> nodes);
	
	/**
	 * Add new node
	 * @param node The name of new node
	 */
	public void nodeAdded(Integer index, String node);
	
	/**
	 * Remove node
	 * @param node The name of target node
	 */
	public void nodeRemoved(Integer index, String node);
	
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
	public Collection<String> nodeNames();
	
	/**
	 * Get current node index
	 * @return
	 */
	public int currentIndex();
	
	/**
	 * Set current node index
	 */
	public void setCurrentIndex(int currentIndex);
}
