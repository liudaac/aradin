package cn.aradin.spring.actuator.starter.actuate;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeansException;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.WriteOperation;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;

import cn.aradin.spring.actuator.starter.extension.IOfflineHandler;

@Endpoint(id = "offline", enableByDefault = true)
public class OfflineEndpoint implements ApplicationContextAware{
	
	private static final Map<String, String> NO_CONTEXT_MESSAGE = Collections
			.unmodifiableMap(Collections.singletonMap("message", "No context to shutdown."));

	private static final Map<String, String> SHUTDOWN_MESSAGE = Collections
			.unmodifiableMap(Collections.singletonMap("message", "Shutting down, bye..."));

	private ConfigurableApplicationContext context;
	
	private final List<IOfflineHandler> offlineHandlers;
	
	public OfflineEndpoint(List<IOfflineHandler> handlers) {
		// TODO Auto-generated constructor stub
		this.offlineHandlers = handlers;
	}
	
	@WriteOperation
	public Map<String, String> shutdown() {
		if (this.context == null) {
			return NO_CONTEXT_MESSAGE;
		}
		try {
			return SHUTDOWN_MESSAGE;
		}
		finally {
			Thread thread = new Thread(this::performShutdown);
			thread.setContextClassLoader(getClass().getClassLoader());
			thread.start();
		}
	}

	private void performShutdown() {
		try {
			Thread.sleep(500L);
		}
		catch (InterruptedException ex) {
			Thread.currentThread().interrupt();
		}
		if (CollectionUtils.isNotEmpty(offlineHandlers)) {
			offlineHandlers.forEach(handler->{
				handler.offline(context);
			});
		}
		this.context.close();
	}

	@Override
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		if (context instanceof ConfigurableApplicationContext) {
			this.context = (ConfigurableApplicationContext) context;
		}
	}
}
