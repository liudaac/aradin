package cn.aradin.spring.redis.starter;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

@Configuration
@EnableCaching
@ConditionalOnBean(RedisConnectionFactory.class)
@AutoConfigureAfter(RedisAutoConfiguration.class)
@EnableConfigurationProperties({ RedisProperties.class })
public class AradinRedisAutoConfiguration {

	@ConditionalOnMissingBean(name = "redisSerializeTemplate")
	@Bean(name = "redisSerializeTemplate")
	public RedisTemplate<String, Object> redisSerializeTemplate(RedisConnectionFactory redisConnectionFactory) {
		RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
		template.setConnectionFactory(redisConnectionFactory);
//		template.setEnableDefaultSerializer(false);
		template.setKeySerializer(RedisSerializer.string());
		return template;
	}
}