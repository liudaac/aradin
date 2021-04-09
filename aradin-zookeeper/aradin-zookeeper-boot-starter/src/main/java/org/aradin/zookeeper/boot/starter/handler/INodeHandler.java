package org.aradin.zookeeper.boot.starter.handler;

import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;

public interface INodeHandler {

	public boolean support(PathChildrenCacheEvent event);

	public void handler(PathChildrenCacheEvent event);
}
