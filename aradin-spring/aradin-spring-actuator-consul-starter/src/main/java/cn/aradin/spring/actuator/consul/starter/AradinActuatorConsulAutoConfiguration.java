package cn.aradin.spring.actuator.consul.starter;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.cloud.consul.ConditionalOnConsulEnabled;
import org.springframework.cloud.consul.serviceregistry.ConsulRegistration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ecwid.consul.v1.ConsulClient;

import cn.aradin.spring.actuator.consul.starter.lifestyle.AradinConsulOfflineHandler;
import cn.aradin.spring.actuator.starter.AradinActuatorAutoConfiguration;
import cn.aradin.spring.actuator.starter.extension.IOfflineHandler;

@Configuration
@ConditionalOnBean({ConsulClient.class, ConsulRegistration.class})
@AutoConfigureBefore(AradinActuatorAutoConfiguration.class)
public class AradinActuatorConsulAutoConfiguration {
	
	@Bean
	@ConditionalOnConsulEnabled
	public IOfflineHandler consulOfflineHandler(ConsulClient consulClient, ConsulRegistration consulRegistration) {
		return new AradinConsulOfflineHandler(consulClient, consulRegistration);
	}
}
