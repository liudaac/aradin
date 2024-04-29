package cn.aradin.spring.salarm.starter;

import java.util.List;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import cn.aradin.cache.redis.starter.AradinRedisAutoConfiguration;
import cn.aradin.spring.salarm.starter.handler.DefaultSalarmHandlerImpl;
import cn.aradin.spring.salarm.starter.handler.ISalarmHandler;
import cn.aradin.spring.salarm.starter.notifier.ISalarm;
import cn.aradin.spring.salarm.starter.notifier.SalarmImpl;
import cn.aradin.spring.salarm.starter.properties.SalarmProperties;

/**
 * Module For Smart Alarm
 *
 */
@Configuration
@AutoConfigureAfter(AradinRedisAutoConfiguration.class)
@EnableConfigurationProperties(SalarmProperties.class)
public class AradinSalarmAutoConfiguration {
	
	/**
	 * Default implement of {@link ISalarmHandler} only if there is no other implement for this interface
	 * @return
	 */
	@Bean
	@ConditionalOnMissingBean
	public ISalarmHandler defautlSalarmHandler() {
		return new DefaultSalarmHandlerImpl();
	}
	
	/**
	 * The entrance for developers to use salarm send-methods
	 * @param redisTemplate redis Bean
	 * @param salarmProperties The base config for salarm
	 * @param handlers Customed handlers for salarm
	 * @return
	 */
	@Bean
	@ConditionalOnBean(RedisTemplate.class)
	public ISalarm salarm(RedisTemplate<Object, Object> redisTemplate, 
			SalarmProperties salarmProperties, 
			List<ISalarmHandler> handlers) {
		return new SalarmImpl(redisTemplate, salarmProperties, handlers);
	}
}