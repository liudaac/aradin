package cn.aradin.zookeeper.boot.starter;

import java.util.List;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import cn.aradin.zookeeper.boot.starter.handler.INodeHandler;
import cn.aradin.zookeeper.boot.starter.manager.ZookeeperClientManager;
import cn.aradin.zookeeper.boot.starter.properties.ZookeeperProperties;
import cn.aradin.zookeeper.boot.starter.support.ZookeeperEventDispatcher;

/**
 * Zookeeper Clients Start Configuration
 * @author daliu
 *
 */
@Configuration
@EnableConfigurationProperties(ZookeeperProperties.class)
public class ZookeeperBootAutoConfiguration {

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