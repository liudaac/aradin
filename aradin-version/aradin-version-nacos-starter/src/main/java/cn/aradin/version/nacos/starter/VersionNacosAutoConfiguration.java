package cn.aradin.version.nacos.starter;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.alibaba.cloud.nacos.NacosConfigAutoConfiguration;
import com.alibaba.cloud.nacos.NacosConfigProperties;

import cn.aradin.version.core.VersionConfiguration;
import cn.aradin.version.core.dispatcher.VersionDispatcher;
import cn.aradin.version.core.gentor.IVersionGentor;
import cn.aradin.version.core.handler.IVersionBroadHandler;
import cn.aradin.version.nacos.starter.handler.VersionNacosBroadHandler;
import cn.aradin.version.nacos.starter.handler.VersionNacosListenerHandler;
import cn.aradin.version.nacos.starter.manager.VersionNacosConfigManager;
import cn.aradin.version.nacos.starter.properties.VersionNacosProperties;

@Configuration
@Import(VersionConfiguration.class)
@EnableConfigurationProperties(VersionNacosProperties.class)
@AutoConfigureAfter({NacosConfigAutoConfiguration.class})
public class VersionNacosAutoConfiguration {
	
	@Bean
	@ConditionalOnProperty(value = "aradin.version.nacos.group", havingValue = "")
	public VersionNacosConfigManager versionNacosConfigManager(NacosConfigProperties nacosConfigProperties,
			VersionNacosProperties nacosProperties) {
		return new VersionNacosConfigManager(nacosConfigProperties, nacosProperties);
	}
	
	@Bean
	@ConditionalOnProperty(value = "aradin.version.nacos.listen", havingValue = "true", matchIfMissing = true)
	public VersionNacosListenerHandler versionNacosListenerHandler(VersionNacosProperties nacosProperties,
			VersionNacosConfigManager versionNacosConfigManager, 
			VersionDispatcher versionDispatcher) {
		return new VersionNacosListenerHandler(nacosProperties, versionNacosConfigManager, versionDispatcher);
	}
	
	@Bean
	@ConditionalOnProperty(value = "aradin.version.nacos.group", havingValue = "")
	public IVersionBroadHandler versionBroadHandler(VersionNacosProperties nacosProperties,
			VersionNacosConfigManager versionNacosConfigManager,
			IVersionGentor versionGentor) {
		return new VersionNacosBroadHandler(nacosProperties, 
				versionNacosConfigManager,
				versionGentor);
	}
}
