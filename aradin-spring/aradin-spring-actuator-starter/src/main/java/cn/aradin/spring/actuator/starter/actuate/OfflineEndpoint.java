package cn.aradin.spring.actuator.starter.actuate;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.dubbo.registry.client.metadata.ServiceInstanceMetadataUtils;
import org.apache.dubbo.rpc.model.ApplicationModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.beans.BeansException;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.cloud.client.serviceregistry.ServiceRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;

import cn.aradin.spring.actuator.starter.context.DeployContext;
import cn.aradin.spring.actuator.starter.extension.IOfflineHandler;
import cn.aradin.spring.actuator.starter.properties.ActuatorOfflineProperties;

@Endpoint(id = "offline", enableByDefault = true)
public class OfflineEndpoint implements ApplicationContextAware{
	
	private final static Logger log = LoggerFactory.getLogger(OfflineEndpoint.class);
	
	private static final Map<String, String> NO_CONTEXT_MESSAGE = Collections
			.unmodifiableMap(Collections.singletonMap("message", "No context to offline."));

	private static final Map<String, String> OFFLINE_MESSAGE = Collections
			.unmodifiableMap(Collections.singletonMap("message", "Offlining, bye..."));

	private ConfigurableApplicationContext context;
	
	private final List<IOfflineHandler> offlineHandlers;
	private final ActuatorOfflineProperties offlineProperties;
	
	
	public OfflineEndpoint(ActuatorOfflineProperties offlineProperties, List<IOfflineHandler> handlers) {
		// TODO Auto-generated constructor stub
		this.offlineProperties = offlineProperties;
		this.offlineHandlers = handlers;
	}
	
	@ReadOperation
	public Map<String, String> offline() {
		if (this.context == null) {
			return NO_CONTEXT_MESSAGE;
		}
		if (!DeployContext.isStopping()
				&&!DeployContext.isStopped()) {
			DeployContext.setStopping();
			try {
				performHandlers();
				performDeregistry();
			} finally {
				// TODO: handle finally clause
				Thread thread = new Thread(this::performDestroy);
				thread.setContextClassLoader(getClass().getClassLoader());
				thread.start();
			}
		}
		return OFFLINE_MESSAGE;
	}

	private void performHandlers() {
		if (CollectionUtils.isNotEmpty(offlineHandlers)) {
			offlineHandlers.forEach(handler->{
				try {
					handler.offline(context);
				} catch (Exception e) {
					// TODO: handle exception
					log.warn("Offline handler failed as {}", e.getMessage());
				}
			});
		}
	}
	
	@SuppressWarnings({"unchecked","rawtypes"})
	private void performDeregistry() {
		try {
			Class.forName("org.springframework.kafka.config.KafkaListenerEndpointRegistry");
			log.info("Kafka listener deregistering");
			KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry = context.getBean(KafkaListenerEndpointRegistry.class);
			if (kafkaListenerEndpointRegistry != null) {
				kafkaListenerEndpointRegistry.stop();
			}
		} catch (ClassNotFoundException e) {
			// TODO: handle exception
			log.warn("Kafka listener is not exist, no need to stop");
		} catch (Exception e) {
			// TODO: handle exception
			log.error("Kafka listener stoped with error {}", e.getMessage());
		}
		try {
			Class.forName("org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry");
			log.info("Rabbit listener deregistering");
			RabbitListenerEndpointRegistry rabbitListenerEndpointRegistry = context.getBean(RabbitListenerEndpointRegistry.class);
			if (rabbitListenerEndpointRegistry != null) {
				rabbitListenerEndpointRegistry.stop();
			}
		} catch (ClassNotFoundException e) {
			// TODO: handle exception
			log.warn("Rabbit listener is not exist, no need to stop");
		} catch (Exception e) {
			// TODO: handle exception
			log.error("Rabbit listener stoped with error {}", e.getMessage());
		}
		try {
			Class.forName("org.apache.dubbo.registry.client.metadata.ServiceInstanceMetadataUtils");
			log.info("Dubbo deregistering");
			ApplicationModel applicationModel = ApplicationModel.defaultModel();
			ServiceInstanceMetadataUtils.unregisterMetadataAndInstance(applicationModel);
		} catch (ClassNotFoundException e) {
			// TODO: handle exception
			log.warn("Dubbo class is not exist, no need to unregister");
		} catch (Exception e) {
			// TODO: handle exception
			log.error("Dubbo deregistered with error {}", e.getMessage());
		}
		try {
			Class.forName("org.springframework.cloud.client.serviceregistry.ServiceRegistryAutoConfiguration");
			Registration registration = context.getBean(Registration.class);
			if (registration != null) {
				ServiceRegistry serviceRegistry = context.getBean(ServiceRegistry.class);
				if (serviceRegistry != null) {
					log.info("Springcloud deregistering");
					serviceRegistry.deregister(registration);
				}
			}
		} catch (ClassNotFoundException e) {
			// TODO: handle exception
			log.warn("SpringCloud Register class is not exist, no need to unregister");
		} catch (Exception e) {
			// TODO: handle exception
			log.error("SpringCloud deregistered with error {}", e.getMessage());
		}
	}
	
	private void performDestroy() {
		try {
			Long wait = offlineProperties.getShutWait();
			if (wait == null || wait <= 0) {
				wait = 2000l;
			}
			Thread.sleep(wait);
		}
		catch (InterruptedException ex) {
			Thread.currentThread().interrupt();
		}
		this.context.close();
		DeployContext.setStopped();
	}
	
	@Override
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		if (context instanceof ConfigurableApplicationContext) {
			this.context = (ConfigurableApplicationContext) context;
		}
	}
}
