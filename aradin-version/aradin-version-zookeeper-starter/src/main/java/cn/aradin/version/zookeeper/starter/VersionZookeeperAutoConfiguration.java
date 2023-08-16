package cn.aradin.version.zookeeper.starter;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import cn.aradin.version.core.VersionConfiguration;
import cn.aradin.version.core.dispatcher.VersionDispatcher;
import cn.aradin.version.core.gentor.IVersionGentor;
import cn.aradin.version.core.handler.IVersionBroadHandler;
import cn.aradin.version.zookeeper.starter.handler.VersionZookeeperBroadHandler;
import cn.aradin.version.zookeeper.starter.handler.VersionsNodeHandler;
import cn.aradin.version.zookeeper.starter.properties.VersionZookeeperProperties;
import cn.aradin.zookeeper.boot.starter.ZookeeperBootAutoConfiguration;
import cn.aradin.zookeeper.boot.starter.handler.INodeHandler;
import cn.aradin.zookeeper.boot.starter.properties.ZookeeperProperties;

/**
 * VersionZookeeper Configuration
 * PathPattern like /${addressid}/${group}/${key}
 */
@Configuration
@Import(VersionConfiguration.class)
@EnableConfigurationProperties(VersionZookeeperProperties.class)
@AutoConfigureAfter(ZookeeperBootAutoConfiguration.class)
public class VersionZookeeperAutoConfiguration {

	@Bean
	@ConditionalOnProperty(value = "aradin.version.zookeeper.address-id", havingValue = "")
	public INodeHandler versionNodeHandler(VersionZookeeperProperties versionZookeeperProperties, ZookeeperProperties zookeeperProperties,
			VersionDispatcher versionDispatcher) {
		return new VersionsNodeHandler(versionZookeeperProperties, zookeeperProperties, versionDispatcher);
	}
	
	@Bean
	@ConditionalOnProperty(value = "aradin.version.zookeeper.address-id", havingValue = "")
	public IVersionBroadHandler versionBroadHandler(VersionZookeeperProperties versionZookeeperProperties,
			IVersionGentor versionGentor) {
		return new VersionZookeeperBroadHandler(versionZookeeperProperties, versionGentor);
	}
}
