package cn.aradin.cluster.zookeeper.starter.handler;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;

import cn.aradin.zookeeper.boot.starter.handler.INodeHandler;

public class ClusterNodeHandler implements INodeHandler {

	@Override
	public boolean support(PathChildrenCacheEvent event) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void handler(CuratorFramework client, PathChildrenCacheEvent event) {
		// TODO Auto-generated method stub

	}

}
