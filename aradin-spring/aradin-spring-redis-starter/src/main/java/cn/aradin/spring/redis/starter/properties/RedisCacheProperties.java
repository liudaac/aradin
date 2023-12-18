package cn.aradin.spring.redis.starter.properties;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.cache.redis")
public class RedisCacheProperties extends Redis {
	
	private Map<String, Redis> caches = new HashMap<String, Redis>();

	public Map<String, Redis> getCaches() {
		return caches;
	}

	public void setCaches(Map<String, Redis> caches) {
		this.caches = caches;
	}
}
