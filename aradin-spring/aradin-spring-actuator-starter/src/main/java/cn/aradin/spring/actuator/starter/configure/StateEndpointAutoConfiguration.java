package cn.aradin.spring.actuator.starter.configure;

import org.springframework.boot.actuate.autoconfigure.endpoint.condition.ConditionalOnAvailableEndpoint;
import org.springframework.boot.actuate.autoconfigure.health.HealthEndpointProperties;
import org.springframework.boot.actuate.health.HealthContributorRegistry;
import org.springframework.boot.actuate.health.HealthEndpointGroups;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cn.aradin.spring.actuator.starter.AradinActuatorAutoConfiguration;
import cn.aradin.spring.actuator.starter.actuate.StateEndpoint;

@Configuration(proxyBeanMethods = false)
@ConditionalOnAvailableEndpoint(endpoint = StateEndpoint.class)
@AutoConfigureAfter(AradinActuatorAutoConfiguration.class)
public class StateEndpointAutoConfiguration {
	
	@Bean
	StateEndpoint stateEndpoint(HealthContributorRegistry registry, HealthEndpointGroups groups,
			HealthEndpointProperties properties) {
		return new StateEndpoint(registry, groups, properties.getLogging().getSlowIndicatorThreshold());
	}
}
