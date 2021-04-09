package cn.aradin.cluster.zookeeper.starter;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import cn.aradin.cluster.core.ClusterConfiguration;
import cn.aradin.zookeeper.boot.starter.ZookeeperBootAutoConfiguration;

/**
 * Hello world!
 *
 */
@Configuration
@Import(ClusterConfiguration.class)
@AutoConfigureAfter(ZookeeperBootAutoConfiguration.class)
public class ClusterZookeeperAutoConfiguration {
}
