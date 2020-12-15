package cn.aradin.spring.actuator.starter;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cn.aradin.spring.actuator.starter.extension.DefaultOfflineHandler;
import cn.aradin.spring.actuator.starter.extension.IOfflineHandler;
import cn.aradin.spring.actuator.starter.properties.ActuatorOnlineProperties;

@Configuration
@EnableConfigurationProperties(ActuatorOnlineProperties.class)
public class AradinActuatorAutoConfiguration {
	
	@Bean
	@ConditionalOnMissingBean
	public IOfflineHandler offlineHandler() {
		return new DefaultOfflineHandler();
	}
}
