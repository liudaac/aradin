package cn.aradin.spring.caffeine.manager.properties;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.boot.context.properties.ConfigurationProperties;

import cn.aradin.spring.caffeine.cache.config.CaffeinesonConfig;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@ConfigurationProperties(prefix = "aradin.cache.caffeine")
@Data
@AllArgsConstructor
@NoArgsConstructor
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
}
