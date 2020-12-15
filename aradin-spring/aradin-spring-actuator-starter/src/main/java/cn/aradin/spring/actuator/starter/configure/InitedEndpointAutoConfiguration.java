package cn.aradin.spring.actuator.starter.configure;

import org.springframework.boot.actuate.autoconfigure.endpoint.condition.ConditionalOnAvailableEndpoint;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cn.aradin.spring.actuator.starter.AradinActuatorAutoConfiguration;
import cn.aradin.spring.actuator.starter.actuate.InitedEndpoint;

@Configuration(proxyBeanMethods = false)
@ConditionalOnAvailableEndpoint(endpoint = InitedEndpoint.class)
@AutoConfigureAfter(AradinActuatorAutoConfiguration.class)
public class InitedEndpointAutoConfiguration {
	
	@Bean
	@ConditionalOnMissingBean
	public InitedEndpoint initedEndpoint() {
		return new InitedEndpoint();
	}
}
