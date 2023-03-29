package cn.aradin.version.nacos.starter;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.alibaba.cloud.nacos.NacosConfigAutoConfiguration;
import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.cloud.nacos.NacosConfigProperties;

import cn.aradin.version.core.VersionConfiguration;
import cn.aradin.version.core.dispatcher.VersionDispatcher;
import cn.aradin.version.core.gentor.IVersionGentor;
import cn.aradin.version.core.handler.IVersionBroadHandler;
import cn.aradin.version.core.properties.VersionNacos;
import cn.aradin.version.core.properties.VersionProperties;
import cn.aradin.version.nacos.starter.handler.VersionNacosBroadHandler;
import cn.aradin.version.nacos.starter.handler.VersionNacosListenerHandler;
import cn.aradin.version.nacos.starter.manager.VersionNacosConfigManager;

@Configuration
@Import(VersionConfiguration.class)
@AutoConfigureAfter({NacosConfigAutoConfiguration.class})
@ConditionalOnBean({NacosConfigManager.class})
public class VersionNacosAutoConfiguration {
	
	@Bean
	@ConditionalOnProperty(value = "aradin.version.nacos.group", havingValue = "")
	public VersionNacosConfigManager versionNacosConfigManager(NacosConfigProperties nacosConfigProperties,
			VersionProperties versionProperties) {
		return new VersionNacosConfigManager(nacosConfigProperties, versionProperties.getNacos());
	}
	
	@Bean
	@ConditionalOnProperty(value = "aradin.version.nacos.group", havingValue = "")
	public VersionNacosListenerHandler versionNacosListenerHandler(VersionProperties versionProperties,
			VersionNacosConfigManager versionNacosConfigManager, 
			VersionDispatcher versionDispatcher) {
		return new VersionNacosListenerHandler(versionProperties.getNacos(), versionNacosConfigManager, versionDispatcher);
	}
	
	@Bean
	@ConditionalOnProperty(value = "aradin.version.nacos.group", havingValue = "")
	public IVersionBroadHandler versionBroadHandler(VersionProperties versionProperties,
			VersionNacosConfigManager versionNacosConfigManager,
			IVersionGentor versionGentor) {
		return new VersionNacosBroadHandler(versionProperties.getNacos(), 
				versionNacosConfigManager,
				versionGentor);
	}
}
