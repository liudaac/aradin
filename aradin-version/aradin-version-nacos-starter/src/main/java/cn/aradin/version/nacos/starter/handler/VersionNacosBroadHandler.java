package cn.aradin.version.nacos.starter.handler;

import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;

import cn.aradin.version.core.gentor.IVersionGentor;
import cn.aradin.version.core.handler.IVersionBroadHandler;
import cn.aradin.version.core.properties.VersionNacos;
import cn.aradin.version.nacos.starter.manager.VersionNacosConfigManager;

public class VersionNacosBroadHandler implements IVersionBroadHandler {
	
	private ConfigService configService;
	private IVersionGentor versionGentor;
	
	public VersionNacosBroadHandler(VersionNacos versionNacos, 
			VersionNacosConfigManager versionNacosConfigManager,
			IVersionGentor versionGentor) {
		this.configService = versionNacosConfigManager.getConfigService();
		this.versionGentor = versionGentor;
	}
	
	@Override
	public void broadcast(String group, String key) {
		// TODO Auto-generated method stub
		broadcast(group, key, versionGentor.nextVersion(group));
	}

	@Override
	public void broadcast(String group, String key, String version) {
		// TODO Auto-generated method stub
		try {
			configService.publishConfig(key, group, version);
		} catch (NacosException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}
}
