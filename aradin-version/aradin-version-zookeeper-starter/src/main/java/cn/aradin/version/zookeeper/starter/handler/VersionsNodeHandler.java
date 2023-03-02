package cn.aradin.version.zookeeper.starter.handler;

import java.util.Optional;

import org.apache.commons.collections.CollectionUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.state.ConnectionState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.aradin.version.core.dispatcher.VersionDispatcher;
import cn.aradin.version.core.properties.VersionProperties;
import cn.aradin.zookeeper.boot.starter.handler.INodeHandler;
import cn.aradin.zookeeper.boot.starter.manager.ZookeeperClientManager;
import cn.aradin.zookeeper.boot.starter.properties.Zookeeper;
import cn.aradin.zookeeper.boot.starter.properties.ZookeeperProperties;

public class VersionsNodeHandler implements INodeHandler {

	private final static Logger log = LoggerFactory.getLogger(VersionsNodeHandler.class);
	
	private VersionProperties versionProperties;

	private VersionDispatcher versionDispatcher;
	
	private CuratorFramework zookeeperClient;

	public VersionsNodeHandler(VersionProperties versionProperties, ZookeeperProperties zookeeperProperties,
			VersionDispatcher versionDispatcher) {
		// TODO Auto-generated constructor stub
		if (versionProperties == null) {
			throw new RuntimeException("Version is not config");
		}
		this.versionProperties = versionProperties;
		this.versionDispatcher = versionDispatcher;
		if (zookeeperProperties != null && versionProperties != null
				&& CollectionUtils.isNotEmpty(zookeeperProperties.getAddresses())
				&& versionProperties.getZookeeper() != null) {
			Optional<Zookeeper> result = zookeeperProperties.getAddresses().stream()
					.filter(zookeeper -> zookeeper.getId().equals(versionProperties.getZookeeper().getAddressId())).findAny();
			if (!result.isPresent()) {
				return;
			}
		}
		throw new RuntimeException("Version's Zookeeper is not config");
	}

	@Override
	public void init(ZookeeperClientManager clientManager) {
		// TODO Auto-generated method stub
		this.zookeeperClient = clientManager.getClient(versionProperties.getZookeeper().getAddressId());
		if (this.zookeeperClient == null) {
			throw new RuntimeException("Version's Zookeeper Client is not exist, Please check the sync-type");
		}
		this.zookeeperClient.getConnectionStateListenable().addListener(this);
	}

	@Override
	public boolean support(PathChildrenCacheEvent event) {
		// TODO Auto-generated method stub
		if (log.isDebugEnabled()) {
			log.debug("Received Event {}", event.getType());
		}
		String path = event.getData().getPath();
		if (log.isDebugEnabled()) {
			log.debug("Received Event Path {} {}", path, event.getType());
		}
		path = path.substring(0, path.lastIndexOf("/"));
		if (path.contains("/")) {
			path = path.substring(0, path.lastIndexOf("/"));
			if (path.contains("/")) {
				String versions = path.substring(path.lastIndexOf("/") + 1);
				if (versionProperties.getZookeeper().getAddressId().equalsIgnoreCase(versions)) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void handler(CuratorFramework client, PathChildrenCacheEvent event) {
		// TODO Auto-generated method stub
		switch (event.getType()) {
		case CHILD_UPDATED:
			String path = event.getData().getPath();
			String key = path.substring(path.lastIndexOf("/")+1);
			path = path.substring(0, path.lastIndexOf("/"));
			String group = path.substring(path.lastIndexOf("/")+1);
			versionDispatcher.dispatchVersion(group, key, String.valueOf(event.getData().getData()));
			break;
		default:
			break;
		}
	}

	@Override
	public void stateChanged(CuratorFramework client, ConnectionState newState) {
		// TODO Auto-generated method stub
		log.warn("Zookeeper Version Connect Status Changed {}", newState.name());
	}
}
