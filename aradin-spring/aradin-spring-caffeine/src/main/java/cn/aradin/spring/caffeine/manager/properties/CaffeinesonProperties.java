package cn.aradin.spring.caffeine.manager.properties;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.boot.context.properties.ConfigurationProperties;

import cn.aradin.spring.caffeine.cache.config.CaffeinesonConfig;

@ConfigurationProperties(prefix = "aradin.cache.caffeine")
public class CaffeinesonProperties {
	
	private String group = "caffeine";
	
	private boolean versioned = false;
	
	/**
	 * 默认配置
	 */
	private CaffeinesonConfig defaults = new CaffeinesonConfig(120000, 120000, 0, 1000, 10000, 10, false, true);
	
	/**
	 * 每个caffeine的单独配置
	 */
	private Map<String, CaffeinesonConfig> configs = new ConcurrentHashMap<String, CaffeinesonConfig>();

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public boolean isVersioned() {
		return versioned;
	}

	public void setVersioned(boolean versioned) {
		this.versioned = versioned;
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
