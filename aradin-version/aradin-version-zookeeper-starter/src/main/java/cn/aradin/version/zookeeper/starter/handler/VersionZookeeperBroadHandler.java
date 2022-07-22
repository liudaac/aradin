package cn.aradin.version.zookeeper.starter.handler;

import java.nio.charset.Charset;

import org.apache.curator.framework.CuratorFramework;

import cn.aradin.spring.core.bean.AradinBeanFactory;
import cn.aradin.version.core.gentor.IVersionGentor;
import cn.aradin.version.core.handler.IVersionBroadHandler;
import cn.aradin.version.core.properties.VersionProperties;
import cn.aradin.zookeeper.boot.starter.manager.ZookeeperClientManager;

public class VersionZookeeperBroadHandler implements IVersionBroadHandler{

	private VersionProperties versionProperties;
	
	private IVersionGentor versionGentor;
	
	private CuratorFramework zookeeperClient;
	
	public VersionZookeeperBroadHandler(VersionProperties versionProperties,
			IVersionGentor versionGentor) {
		// TODO Auto-generated constructor stub
		this.versionProperties = versionProperties;
	}
	
	private CuratorFramework getZookeeperClient() {
		if (zookeeperClient == null) {
			zookeeperClient = AradinBeanFactory.getBean(ZookeeperClientManager.class).getClient(versionProperties.getZookeeper().getAddressId());
		}
		return zookeeperClient;
	}
	
	@Override
	public void broadcast(String group, String key) {
		// TODO Auto-generated method stub
		String path = "/" + versionProperties.getZookeeper().getAddressId() + "/" + group + "/" + key;
		String value = versionGentor.nextVersion(path);
		try {
			getZookeeperClient().createContainers(path);
			getZookeeperClient().setData().forPath(path, value.getBytes(Charset.forName("utf-8")));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e.getCause());
		}
	}

	@Override
	public void broadcast(String group, String key, String version) {
		// TODO Auto-generated method stub
		String path = "/" + versionProperties.getZookeeper().getAddressId() + "/" + group + "/" + key;
		try {
			getZookeeperClient().createContainers(path);
			getZookeeperClient().setData().forPath(path, version.getBytes(Charset.forName("utf-8")));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e.getCause());
		}
	}
}
