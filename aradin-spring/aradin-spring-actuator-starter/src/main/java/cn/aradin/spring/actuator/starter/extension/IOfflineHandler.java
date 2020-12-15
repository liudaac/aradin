package cn.aradin.spring.actuator.starter.extension;

import org.springframework.context.ApplicationContext;

public interface IOfflineHandler {
	
	public void offline(ApplicationContext context);
}
