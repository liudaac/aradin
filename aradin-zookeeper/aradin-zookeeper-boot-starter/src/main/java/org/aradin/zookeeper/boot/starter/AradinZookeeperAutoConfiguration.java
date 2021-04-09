package org.aradin.zookeeper.boot.starter;

import java.util.List;

import org.aradin.zookeeper.boot.starter.handler.INodeHandler;
import org.aradin.zookeeper.boot.starter.manager.ZookeeperClientManager;
import org.aradin.zookeeper.boot.starter.properties.ZookeeperProperties;
import org.aradin.zookeeper.boot.starter.support.ZookeeperEventDispatcher;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

/**
 * Zookeeper Clients Start Configuration
 * @author daliu
 *
 */
@DependsOn("aradinBeanFactory")
@Configuration
@EnableConfigurationProperties(ZookeeperProperties.class)
public class AradinZookeeperAutoConfiguration {

	/**
	 * Declare dispatcher for zk events
	 * @param nodeHandlers event handlers
	 * @return
	 */
	@Bean
	public ZookeeperEventDispatcher ZookeeperEventDispatcher(List<INodeHandler> nodeHandlers) {
		return new ZookeeperEventDispatcher(nodeHandlers);
	}
	
	/**
	 * Declare clients in zk configs
	 * @param zookeeperProperties Zk configs
	 * @param zookeeperEventDispatcher Event dispatcher 
	 * @return
	 */
	@Bean
	public ZookeeperClientManager zookeeperClientManager(ZookeeperProperties zookeeperProperties,
			ZookeeperEventDispatcher zookeeperEventDispatcher) {
		return new ZookeeperClientManager(zookeeperProperties, zookeeperEventDispatcher);
	}
}