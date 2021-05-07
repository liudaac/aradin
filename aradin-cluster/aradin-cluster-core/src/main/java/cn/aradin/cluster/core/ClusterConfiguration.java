package cn.aradin.cluster.core;

import org.springframework.boot.actuate.autoconfigure.endpoint.condition.ConditionalOnAvailableEndpoint;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cn.aradin.cluster.core.actuate.ClusterEndpoint;
import cn.aradin.cluster.core.manager.DefaultClusterNodeManager;
import cn.aradin.cluster.core.manager.IClusterNodeManager;
import cn.aradin.cluster.core.properties.ClusterProperties;

/**
 * Hello world!
 *
 */
@Configuration
@EnableConfigurationProperties(ClusterProperties.class)
public class ClusterConfiguration {
	
	@Bean
	@ConditionalOnMissingBean
	public IClusterNodeManager clusterNodeListener(ClusterProperties clusterProperties) {
		return new DefaultClusterNodeManager(clusterProperties);
	}
	
	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnAvailableEndpoint(endpoint = ClusterEndpoint.class)
	public ClusterEndpoint clusterEndpoint(IClusterNodeManager clusterNodeManager) {
		return new ClusterEndpoint(clusterNodeManager);
	}
}
