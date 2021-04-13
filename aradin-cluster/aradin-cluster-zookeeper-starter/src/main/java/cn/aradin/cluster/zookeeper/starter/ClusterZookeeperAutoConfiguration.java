package cn.aradin.cluster.zookeeper.starter;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import cn.aradin.cluster.core.ClusterConfiguration;
import cn.aradin.cluster.core.listener.IClusterNodeManager;
import cn.aradin.cluster.core.properties.ClusterProperties;
import cn.aradin.cluster.zookeeper.starter.handler.ClusterNodeHandler;
import cn.aradin.zookeeper.boot.starter.ZookeeperBootAutoConfiguration;
import cn.aradin.zookeeper.boot.starter.handler.INodeHandler;
import cn.aradin.zookeeper.boot.starter.properties.ZookeeperProperties;

/**
 * Hello world!
 *
 */
@Configuration
@Import(ClusterConfiguration.class)
@AutoConfigureAfter(ZookeeperBootAutoConfiguration.class)
public class ClusterZookeeperAutoConfiguration {
	
	@Bean
	public INodeHandler clusterNodeHandler(ClusterProperties clusterProperties, 
			ZookeeperProperties zookeeperProperties,
			IClusterNodeManager clusterNodeManager) {
		return new ClusterNodeHandler(clusterProperties, zookeeperProperties, clusterNodeManager);
	}
}
