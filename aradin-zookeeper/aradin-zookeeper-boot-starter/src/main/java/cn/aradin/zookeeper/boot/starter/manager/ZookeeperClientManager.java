package cn.aradin.zookeeper.boot.starter.manager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.collections.CollectionUtils;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCache.StartMode;
import org.apache.curator.retry.ExponentialBackoffRetry;

import cn.aradin.zookeeper.boot.starter.properties.ZookeeperProperties;
import cn.aradin.zookeeper.boot.starter.support.ZookeeperEventDispatcher;

public class ZookeeperClientManager {

	private ZookeeperProperties zookeeperProperties;
	
	private ZookeeperEventDispatcher dispatcher;
	
	private Map<String, CuratorFramework> zookeeperClients = new ConcurrentHashMap<String, CuratorFramework>(15);
	
	public ZookeeperClientManager(ZookeeperProperties zookeeperProperties,
			ZookeeperEventDispatcher dispatcher) {
		this.zookeeperProperties = zookeeperProperties;
		this.dispatcher = dispatcher;
		init();
	}
	
	/**
	 * InitialMethod 
	 * 1、Init ZK Clients in properties
	 * 2、Bind Event Dispatchers in properties
	 */
	public void init() {
		if (zookeeperProperties.isEnable()
				&& CollectionUtils.isNotEmpty(zookeeperProperties.getAddresses())) {
			RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
			zookeeperProperties.getAddresses().forEach(address -> {
				CuratorFramework client = zookeeperClients.get(address.getAddress());
				if (client == null) {
					synchronized (address.getAddress().intern()) {
						try {
							client = zookeeperClients.get(address.getAddress());
							if (client == null) {
								client = CuratorFrameworkFactory.builder().connectString(address.getAddress())
										.sessionTimeoutMs(zookeeperProperties.getSessionTimeout()).connectionTimeoutMs(zookeeperProperties.getConnectionTimeout())
										.retryPolicy(retryPolicy).build();
								zookeeperClients.put(address.getAddress(), client);
								client.start();
							}
							client.createContainers(address.getId());
							@SuppressWarnings("resource")
							PathChildrenCache childrenCache = new PathChildrenCache(client, address.getId(), true);
							childrenCache.getListenable().addListener(dispatcher);
							childrenCache.start(StartMode.POST_INITIALIZED_EVENT);
						} catch (Exception e) {
							// TODO: handle exception
						    throw new RuntimeException(e.getCause());
						}
					}
				}
			});
		}
	}
}
