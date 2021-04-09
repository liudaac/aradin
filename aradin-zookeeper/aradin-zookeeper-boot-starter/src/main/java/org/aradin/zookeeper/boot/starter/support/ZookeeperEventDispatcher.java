package org.aradin.zookeeper.boot.starter.support;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.apache.commons.collections.CollectionUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.aradin.zookeeper.boot.starter.handler.INodeHandler;

import cn.aradin.spring.core.thread.AradinExecutors;

public class ZookeeperEventDispatcher implements PathChildrenCacheListener{
	
	private List<INodeHandler> nodeHandlers;
	
	public ZookeeperEventDispatcher(List<INodeHandler> nodeHandlers) {
		this.nodeHandlers = nodeHandlers;
	}

	@Override
	public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
		// TODO Auto-generated method stub
		if (CollectionUtils.isNotEmpty(nodeHandlers)) {
			nodeHandlers.forEach(nodeHandler -> {
				if (nodeHandler.support(event)) {
					CompletableFuture.runAsync(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							nodeHandler.handler(event);
						}
					}, AradinExecutors.newFixedThreadPool("zookeeperevent", 4, 8, 10000, 2000l));
				}
			});
		}
	}
}
