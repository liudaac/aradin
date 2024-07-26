package cn.aradin.cache.redis.starter.properties;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "aradin.cache.redis")
public class RedisCacheManagerProperties {
	private RedisCacheConfiguration defaults = new RedisCacheConfiguration();
	private Map<String, RedisCacheConfiguration> configs = new LinkedHashMap<String, RedisCacheConfiguration>();
	public RedisCacheConfiguration getDefaults() {
		return defaults;
	}
	public void setDefaults(RedisCacheConfiguration defaults) {
		this.defaults = defaults;
	}
	public Map<String, RedisCacheConfiguration> getConfigs() {
		return configs;
	}
	public void setConfigs(Map<String, RedisCacheConfiguration> configs) {
		this.configs = configs;
	}
}
