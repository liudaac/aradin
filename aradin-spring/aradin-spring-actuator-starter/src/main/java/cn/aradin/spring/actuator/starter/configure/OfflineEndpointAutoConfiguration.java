package cn.aradin.spring.actuator.starter.configure;

import java.util.List;

import org.springframework.boot.actuate.autoconfigure.endpoint.condition.ConditionalOnAvailableEndpoint;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cn.aradin.spring.actuator.starter.AradinActuatorAutoConfiguration;
import cn.aradin.spring.actuator.starter.actuate.OfflineEndpoint;
import cn.aradin.spring.actuator.starter.extension.IOfflineHandler;
import cn.aradin.spring.actuator.starter.properties.ActuatorOfflineProperties;

@Configuration(proxyBeanMethods = false)
@ConditionalOnAvailableEndpoint(endpoint = OfflineEndpoint.class)
@AutoConfigureAfter(AradinActuatorAutoConfiguration.class)
public class OfflineEndpointAutoConfiguration {
	
	@Bean
	@ConditionalOnMissingBean
	public OfflineEndpoint offlineEndpoint(ActuatorOfflineProperties offlineProperties, List<IOfflineHandler> offlineHandlers) {
		return new OfflineEndpoint(offlineProperties, offlineHandlers);
	}
}
