package cn.aradin.zookeeper.boot.starter.handler;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;

import cn.aradin.zookeeper.boot.starter.manager.ZookeeperClientManager;

public interface INodeHandler {

	public void init(ZookeeperClientManager clientManager);
	
	public boolean support(PathChildrenCacheEvent event);

	public void handler(CuratorFramework client, PathChildrenCacheEvent event);
}
