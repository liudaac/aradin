package cn.aradin.spring.actuator.starter.configure;

import org.springframework.boot.actuate.autoconfigure.endpoint.condition.ConditionalOnAvailableEndpoint;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cn.aradin.spring.actuator.starter.AradinActuatorAutoConfiguration;
import cn.aradin.spring.actuator.starter.actuate.OnlineEndpoint;
import cn.aradin.spring.actuator.starter.properties.ActuatorOnlineProperties;

@Configuration(proxyBeanMethods = false)
@ConditionalOnAvailableEndpoint(endpoint = OnlineEndpoint.class)
@AutoConfigureAfter(AradinActuatorAutoConfiguration.class)
public class OnlineEndpointAutoConfiguration {
	
	@Bean
	@ConditionalOnMissingBean
	public OnlineEndpoint onlineEndpoint(ActuatorOnlineProperties actuatorOnlineProperties) {
		return new OnlineEndpoint(actuatorOnlineProperties);
	}
}
