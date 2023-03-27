package cn.aradin.version.nacos.starter.handler;

import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.nacos.api.exception.NacosException;

import cn.aradin.version.core.dispatcher.VersionDispatcher;
import cn.aradin.version.core.properties.VersionNacos;
import cn.aradin.version.nacos.starter.listener.VersionNacosConfigListener;

public class VersionNacosListenerHandler {

	public VersionNacosListenerHandler(VersionNacos versionNacos, 
			NacosConfigManager nacosConfigManager,
			VersionDispatcher versionDispatcher) {
		
		try {
			nacosConfigManager.getConfigService().addListener(versionNacos.getDataId(),
					versionNacos.getGroup(),
					new VersionNacosConfigListener(versionDispatcher, versionNacos.getGroup(), versionNacos.getDataId()));
		} catch (NacosException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException("VersionNacosListenerHandler Init-Failed "+e.getMessage());
		}
	}
}
