package cn.aradin.cluster.nacos.starter;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import cn.aradin.cluster.core.ClusterConfiguration;
import cn.aradin.cluster.nacos.starter.properties.ClusterNacosProperties;

@Configuration
@Import(ClusterConfiguration.class)
@EnableConfigurationProperties(ClusterNacosProperties.class)
public class ClusterNacosAutoConfiguration {
	
}
