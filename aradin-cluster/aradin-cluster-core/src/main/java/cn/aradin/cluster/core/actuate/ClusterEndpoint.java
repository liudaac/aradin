package cn.aradin.cluster.core.actuate;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;

import cn.aradin.cluster.core.manager.IClusterNodeManager;

@Endpoint(id = "cluster", enableByDefault = true)
public class ClusterEndpoint{
	
	private static Map<String, String> nodesMap = new HashMap<String, String>(1);
	
	private IClusterNodeManager clusterNodeManager;
	
	public ClusterEndpoint(IClusterNodeManager clusterNodeManager) {
		// TODO Auto-generated constructor stub
		this.clusterNodeManager = clusterNodeManager;
	}
	
	@ReadOperation
	public Map<String, String> cluster() {
		nodesMap.put("nodeNames", String.join(",", clusterNodeManager.nodeNames()));
		nodesMap.put("index", String.valueOf(clusterNodeManager.currentIndex()));
		nodesMap.put("nodeNum", String.valueOf(clusterNodeManager.nodeNum()));
		return nodesMap;
	}
}
