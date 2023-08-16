package cn.aradin.cluster.zookeeper.starter;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import cn.aradin.cluster.core.ClusterConfiguration;
import cn.aradin.cluster.core.manager.IClusterNodeManager;
import cn.aradin.cluster.core.properties.ClusterProperties;
import cn.aradin.cluster.zookeeper.starter.handler.ClusterZookeeperNodeHandler;
import cn.aradin.cluster.zookeeper.starter.properties.ClusterZookeeperProperties;
import cn.aradin.zookeeper.boot.starter.ZookeeperBootAutoConfiguration;
import cn.aradin.zookeeper.boot.starter.handler.INodeHandler;
import cn.aradin.zookeeper.boot.starter.properties.ZookeeperProperties;

/**
 * Hello world!
 *
 */
@Configuration
@Import(ClusterConfiguration.class)
@EnableConfigurationProperties(ClusterZookeeperProperties.class)
@AutoConfigureAfter(ZookeeperBootAutoConfiguration.class)
public class ClusterZookeeperAutoConfiguration {
	
	@Bean
	public INodeHandler clusterNodeHandler(ClusterZookeeperProperties clusterZookeeperProperties, ClusterProperties clusterProperties, 
			ZookeeperProperties zookeeperProperties,
			IClusterNodeManager clusterNodeManager) {
		return new ClusterZookeeperNodeHandler(clusterZookeeperProperties, clusterProperties, zookeeperProperties, clusterNodeManager);
	}
}
