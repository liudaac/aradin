package cn.aradin.zookeeper.boot.starter.handler;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.state.ConnectionStateListener;

import cn.aradin.zookeeper.boot.starter.manager.ZookeeperClientManager;

public interface INodeHandler extends ConnectionStateListener{

	public void init(ZookeeperClientManager clientManager);

	public boolean support(PathChildrenCacheEvent event);

	public void handler(CuratorFramework client, PathChildrenCacheEvent event);
}
