package org.aradin.zookeeper.versync.starter;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Configuration;

import cn.aradin.zookeeper.boot.starter.ZookeeperBootAutoConfiguration;

/**
 * Hello world!
 *
 */
@Configuration
@AutoConfigureAfter(ZookeeperBootAutoConfiguration.class)
public class ZookeeperVersyncAutoConfiguration {
}
