package cn.aradin.spring.session.config.properties;

import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "aradin.session")
public class AradinSessionProperties {
	private RedisProperties redis;
	public RedisProperties getRedis() {
		return redis;
	}
	public void setRedis(RedisProperties redis) {
		this.redis = redis;
	}
}
