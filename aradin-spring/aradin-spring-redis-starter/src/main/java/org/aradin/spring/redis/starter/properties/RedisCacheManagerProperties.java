package org.aradin.spring.redis.starter.properties;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@ConfigurationProperties(prefix = "aradin.cache.redis")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RedisCacheManagerProperties {
	private RedisCacheConfiguration defaults = new RedisCacheConfiguration();
	private Map<String, RedisCacheConfiguration> configs = new LinkedHashMap<String, RedisCacheConfiguration>();
}
