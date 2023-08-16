package cn.aradin.version.nacos.starter.handler;

import org.apache.commons.collections4.CollectionUtils;

import com.alibaba.nacos.api.exception.NacosException;

import cn.aradin.version.core.dispatcher.VersionDispatcher;
import cn.aradin.version.nacos.starter.listener.VersionNacosConfigListener;
import cn.aradin.version.nacos.starter.manager.VersionNacosConfigManager;
import cn.aradin.version.nacos.starter.properties.VersionNacosProperties;

public class VersionNacosListenerHandler {

	public VersionNacosListenerHandler(VersionNacosProperties versionNacos, 
			VersionNacosConfigManager versionNacosConfigManager,
			VersionDispatcher versionDispatcher) {
		if (CollectionUtils.isEmpty(versionNacos.getDataIds())) {
			throw new RuntimeException("Nacos data-ids not config");
		}
		try {
			for(String dataId:versionNacos.getDataIds()) {
				versionNacosConfigManager.getConfigService().addListener(dataId, versionNacos.getGroup(), new VersionNacosConfigListener(versionDispatcher, versionNacos.getGroup(), dataId));
			}
		} catch (NacosException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException("VersionNacosListenerHandler Init-Failed "+e.getMessage());
		}
	}
}
