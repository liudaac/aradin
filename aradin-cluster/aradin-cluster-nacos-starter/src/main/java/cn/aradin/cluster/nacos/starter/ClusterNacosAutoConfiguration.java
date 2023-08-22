package cn.aradin.cluster.nacos.starter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import cn.aradin.cluster.core.ClusterConfiguration;
import cn.aradin.cluster.core.manager.IClusterNodeManager;
import cn.aradin.cluster.core.properties.ClusterProperties;
import cn.aradin.cluster.nacos.starter.handler.ClusterNacosNodeHandler;
import cn.aradin.cluster.nacos.starter.properties.ClusterNacosProperties;

@Configuration
@Import(ClusterConfiguration.class)
@EnableConfigurationProperties(ClusterNacosProperties.class)
public class ClusterNacosAutoConfiguration {
	
	@Bean
	@ConditionalOnProperty(value = "aradin.cluster.nacos.service-name", havingValue = "")
	public ClusterNacosNodeHandler nacosNodeHandler(@Value("server.port")Integer port, 
			@Value("spring.application.name") String serviceName,
			ClusterProperties clusterProperties, 
			ClusterNacosProperties clusterNacosProperties,
			IClusterNodeManager clusterNodeManager) {
		return new ClusterNacosNodeHandler(clusterNacosProperties, clusterProperties, port, serviceName, clusterNodeManager);
	}
}
