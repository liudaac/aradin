package cn.aradin.version.zookeeper.starter.handler;

import cn.aradin.version.core.handler.IVersionBroadHandler;
import cn.aradin.version.core.properties.VersionProperties;
import cn.aradin.zookeeper.boot.starter.handler.INodeHandler;

public class VersionZookeeperBroadHandler implements IVersionBroadHandler{

	private VersionProperties versionProperties;
	
	private INodeHandler versionNodeHandler;
	
	public VersionZookeeperBroadHandler(VersionProperties versionProperties,
			INodeHandler versionNodeHandler) {
		// TODO Auto-generated constructor stub
		this.versionProperties = versionProperties;
		this.versionNodeHandler = versionNodeHandler;
	}
	
	@Override
	public void broadcast(String group, String key, String value) {
		// TODO Auto-generated method stub
		String path = "/" + versionProperties.getZookeeperAddressId() + "/" + group + "/" + key;
		versionNodeHandler.setValue(path, value);
	}
}
