package cn.aradin.version.zookeeper.starter.handler;

import java.util.Optional;

import org.apache.commons.collections.CollectionUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;

import cn.aradin.version.core.enums.SyncType;
import cn.aradin.version.core.properties.VersionProperties;
import cn.aradin.zookeeper.boot.starter.handler.INodeHandler;
import cn.aradin.zookeeper.boot.starter.manager.ZookeeperClientManager;
import cn.aradin.zookeeper.boot.starter.properties.Zookeeper;
import cn.aradin.zookeeper.boot.starter.properties.ZookeeperProperties;

public class VersionsNodeHandler implements INodeHandler {

	private VersionProperties versionProperties;
	
	public VersionsNodeHandler(VersionProperties versionProperties, ZookeeperProperties zookeeperProperties) {
		// TODO Auto-generated constructor stub
		if (versionProperties == null) {
			throw new RuntimeException("Version is not config");
		}
		if (!SyncType.zookeeper.equals(versionProperties.getSyncType())) {
			throw new RuntimeException("Version is not registed on zookeeper");
		}
		this.versionProperties = versionProperties;
		if (zookeeperProperties != null && versionProperties != null
				&& CollectionUtils.isNotEmpty(zookeeperProperties.getAddresses())) {
			Optional<Zookeeper> result = zookeeperProperties.getAddresses().stream()
					.filter(zookeeper -> zookeeper.getId().equals(versionProperties.getZookeeperAddressId())).findAny();
			if (!result.isPresent()) {
				return;
			}
		}
		throw new RuntimeException("Version's Zookeeper is not config");
	}
	
	@Override
	public void init(ZookeeperClientManager clientManager) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean support(PathChildrenCacheEvent event) {
		// TODO Auto-generated method stub
		String path = event.getData().getPath();
		path = path.substring(0, path.lastIndexOf("/"));
		if (path.contains("/")) {
			path = path.substring(0, path.lastIndexOf("/"));
			if (path.contains("/")) {
				String versions = path.substring(path.lastIndexOf("/") + 1);
				if (versionProperties.getZookeeperAddressId().equalsIgnoreCase(versions)) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void handler(CuratorFramework client, PathChildrenCacheEvent event) {
		// TODO Auto-generated method stub
		
	}
}
