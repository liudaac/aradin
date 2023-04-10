package cn.aradin.spring.actuator.starter.actuate;

import java.util.Collections;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;

import cn.aradin.spring.actuator.starter.context.DeployContext;

@Endpoint(id = "inited", enableByDefault = true)
public class InitedEndpoint implements ApplicationContextAware{
	
	private static final Map<String, String> NO_INITED_MESSAGE = Collections
			.unmodifiableMap(Collections.singletonMap("message", "Context not inited"));
	private static final Map<String, String> INITED_MESSAGE = Collections
			.unmodifiableMap(Collections.singletonMap("message", "Context inited"));
	
	private ConfigurableApplicationContext context;
	
	@ReadOperation
	public Map<String, String> inited() {
		if (this.context != null && this.context.isActive() && DeployContext.isStarted()) {
			return INITED_MESSAGE;
		}
		return NO_INITED_MESSAGE;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		// TODO Auto-generated method stub
		if (context instanceof ConfigurableApplicationContext) {
			this.context = (ConfigurableApplicationContext) context;
		}
	}
}