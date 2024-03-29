package cn.aradin.cache.caffeine.manager.properties;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.boot.context.properties.ConfigurationProperties;

import cn.aradin.cache.caffeine.cache.config.CaffeinesonConfig;

@ConfigurationProperties(prefix = "aradin.cache.caffeine")
public class CaffeinesonProperties {
	
	private Duration cleanInterval;
	
	/**
	 * 默认配置
	 */
	private CaffeinesonConfig defaults = new CaffeinesonConfig(120000, 120000, 0, 1000, 10000, 10, false, true, false, false);
	
	/**
	 * 每个caffeine的单独配置
	 */
	private Map<String, CaffeinesonConfig> configs = new ConcurrentHashMap<String, CaffeinesonConfig>();

	public Duration getCleanInterval() {
		return cleanInterval;
	}

	public void setCleanInterval(Duration cleanInterval) {
		this.cleanInterval = cleanInterval;
	}

	public CaffeinesonConfig getDefaults() {
		return defaults;
	}

	public void setDefaults(CaffeinesonConfig defaults) {
		this.defaults = defaults;
	}

	public Map<String, CaffeinesonConfig> getConfigs() {
		return configs;
	}

	public void setConfigs(Map<String, CaffeinesonConfig> configs) {
		this.configs = configs;
	}
}
