package cn.aradin.version.nacos.starter.handler;

import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;

import cn.aradin.version.core.gentor.IVersionGentor;
import cn.aradin.version.core.handler.IVersionBroadHandler;
import cn.aradin.version.nacos.starter.manager.VersionNacosConfigManager;
import cn.aradin.version.nacos.starter.properties.VersionNacosProperties;

public class VersionNacosBroadHandler implements IVersionBroadHandler {
	
	private ConfigService configService;
	private IVersionGentor versionGentor;
	private String group;
	
	public VersionNacosBroadHandler(VersionNacosProperties versionNacos, 
			VersionNacosConfigManager versionNacosConfigManager,
			IVersionGentor versionGentor) {
		this.configService = versionNacosConfigManager.getConfigService();
		this.versionGentor = versionGentor;
		this.group = versionNacos.getGroup();
	}
	
	@Override
	public void broadcast(String key) {
		// TODO Auto-generated method stub
		broadcast(key, versionGentor.nextVersion(key));
	}

	@Override
	public void broadcast(String key, String version) {
		// TODO Auto-generated method stub
		try {
			configService.publishConfig(key, group, version);
		} catch (NacosException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}
}
