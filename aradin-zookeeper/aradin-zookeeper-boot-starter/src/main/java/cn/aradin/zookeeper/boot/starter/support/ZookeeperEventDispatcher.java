package cn.aradin.zookeeper.boot.starter.support;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.apache.commons.collections.CollectionUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent.Type;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;

import cn.aradin.spring.core.thread.AradinExecutors;
import cn.aradin.zookeeper.boot.starter.handler.INodeHandler;
import cn.aradin.zookeeper.boot.starter.manager.ZookeeperClientManager;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ZookeeperEventDispatcher implements PathChildrenCacheListener{
	
	private List<INodeHandler> nodeHandlers;
	
	public ZookeeperEventDispatcher(List<INodeHandler> nodeHandlers) {
		this.nodeHandlers = nodeHandlers;
	}

	public void initHandlers(ZookeeperClientManager clientManager) {
		this.nodeHandlers.forEach(handler -> {
			handler.init(clientManager);
		});
	}
	
	@Override
	public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
		// TODO Auto-generated method stub
		if (log.isInfoEnabled()) {
			log.info("Received Event {}", event.getType());
		}
		if (CollectionUtils.isNotEmpty(nodeHandlers)) {
			if (event.getType().equals(Type.CONNECTION_LOST)
					||event.getType().equals(Type.CONNECTION_RECONNECTED)
					||event.getType().equals(Type.CONNECTION_SUSPENDED)) {
				if (log.isWarnEnabled()) {
					log.warn("Received Ignored Event {}", event.getType());
				}
				return;
			}
			nodeHandlers.forEach(nodeHandler -> {
				if (event.getType().equals(Type.INITIALIZED) || nodeHandler.support(event)) {
					CompletableFuture.runAsync(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							nodeHandler.handler(client, event);
						}
					}, AradinExecutors.newFixedThreadPool("zookeeperevent", 4, 8, 10000, 2000l));
				}
			});
		}
	}
}
