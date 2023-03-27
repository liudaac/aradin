package cn.aradin.version.nacos.starter;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.alibaba.cloud.nacos.NacosConfigAutoConfiguration;
import com.alibaba.cloud.nacos.NacosConfigManager;
import cn.aradin.version.core.VersionConfiguration;
import cn.aradin.version.core.dispatcher.VersionDispatcher;
import cn.aradin.version.core.gentor.IVersionGentor;
import cn.aradin.version.core.handler.IVersionBroadHandler;
import cn.aradin.version.core.properties.VersionProperties;
import cn.aradin.version.nacos.starter.handler.VersionNacosBroadHandler;
import cn.aradin.version.nacos.starter.handler.VersionNacosListenerHandler;

@Configuration
@Import(VersionConfiguration.class)
@AutoConfigureAfter({NacosConfigAutoConfiguration.class})
@ConditionalOnBean({NacosConfigManager.class})
public class VersionNacosAutoConfiguration {
	
	@Bean
	@ConditionalOnProperty("aradin.version.nacos.data-id")
	public VersionNacosListenerHandler versionNacosListenerHandler(VersionProperties versionProperties,
			NacosConfigManager nacosConfigManager, 
			VersionDispatcher versionDispatcher) {
		return new VersionNacosListenerHandler(versionProperties.getNacos(), nacosConfigManager, versionDispatcher);
	}
	
	@Bean
	@ConditionalOnProperty("aradin.version.nacos.data-id")
	public IVersionBroadHandler versionBroadHandler(VersionProperties versionProperties,
			NacosConfigManager nacosConfigManager,
			IVersionGentor versionGentor) {
		return new VersionNacosBroadHandler(versionProperties.getNacos(), 
				nacosConfigManager,
				versionGentor);
	}
}
