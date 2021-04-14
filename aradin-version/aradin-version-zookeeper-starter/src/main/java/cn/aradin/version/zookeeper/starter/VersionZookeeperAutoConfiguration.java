package cn.aradin.version.zookeeper.starter;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import cn.aradin.version.core.VersionConfiguration;
import cn.aradin.version.core.properties.VersionProperties;
import cn.aradin.version.zookeeper.starter.handler.VersionsNodeHandler;
import cn.aradin.zookeeper.boot.starter.ZookeeperBootAutoConfiguration;
import cn.aradin.zookeeper.boot.starter.handler.INodeHandler;
import cn.aradin.zookeeper.boot.starter.properties.ZookeeperProperties;

/**
 * VersionZookeeper Configuration
 *
 */
@Configuration
@Import(VersionConfiguration.class)
@AutoConfigureAfter(ZookeeperBootAutoConfiguration.class)
public class VersionZookeeperAutoConfiguration {
	
	@Bean
	public INodeHandler versionNodeHandler(VersionProperties versionProperties,
			ZookeeperProperties zookeeperProperties) {
		return new VersionsNodeHandler(versionProperties, zookeeperProperties);
	}
}
