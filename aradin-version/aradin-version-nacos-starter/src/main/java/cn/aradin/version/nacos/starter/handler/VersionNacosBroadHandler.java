package cn.aradin.version.nacos.starter.handler;

import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;

import cn.aradin.version.core.dispatcher.VersionDispatcher;
import cn.aradin.version.core.gentor.IVersionGentor;
import cn.aradin.version.core.handler.IVersionBroadHandler;
import cn.aradin.version.core.properties.VersionNacos;
import cn.aradin.version.nacos.starter.listener.VersionNacosConfigListener;

public class VersionNacosBroadHandler implements IVersionBroadHandler {
	
	private ConfigService configService;
	private IVersionGentor versionGentor;
	
	public VersionNacosBroadHandler(VersionNacos versionNacos, 
			NacosConfigManager nacosConfigManager, 
			VersionDispatcher versionDispatcher,
			IVersionGentor versionGentor) {
		
		try {
			this.configService = nacosConfigManager.getConfigService();
			configService.addListener(versionNacos.getDataId(),
					versionNacos.getGroup(), 
					new VersionNacosConfigListener(versionDispatcher, versionNacos.getGroup(), versionNacos.getDataId()));
		} catch (NacosException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException("VersionNacosBroadHandler Init-Failed "+e.getMessage());
		}
	}
	
	@Override
	public void broadcast(String group, String key) {
		// TODO Auto-generated method stub
		try {
			configService.publishConfig(key, group, versionGentor.nextVersion(group));
		} catch (NacosException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}
}
