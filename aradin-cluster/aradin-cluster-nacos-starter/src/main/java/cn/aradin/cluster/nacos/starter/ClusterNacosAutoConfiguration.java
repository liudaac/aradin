package cn.aradin.cluster.nacos.starter;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import cn.aradin.cluster.core.ClusterConfiguration;

@Configuration
@Import(ClusterConfiguration.class)
public class ClusterNacosAutoConfiguration {
}
