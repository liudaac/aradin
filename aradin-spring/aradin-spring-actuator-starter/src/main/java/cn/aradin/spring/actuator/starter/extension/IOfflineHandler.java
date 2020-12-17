package cn.aradin.spring.actuator.starter.extension;

import org.springframework.context.ApplicationContext;

/**
 * The Extension For Customized Operation On Offline
 * @author daliu
 *
 */
public interface IOfflineHandler {
	
	public void offline(ApplicationContext context);
}
