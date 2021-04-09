package cn.aradin.cluster.core;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import cn.aradin.cluster.core.properties.ClusterProperties;

/**
 * Hello world!
 *
 */
@Configuration
@EnableConfigurationProperties(ClusterProperties.class)
public class ClusterConfiguration {
}
