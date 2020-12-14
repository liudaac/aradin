package org.aradin.spring.redis.starter.properties;

import java.time.Duration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RedisCacheConfiguration {
	private Duration ttl = Duration.ofSeconds(3600L);
	private boolean cacheNullValues = true;
	private String keyPrefix = "aradin_";
	private boolean usePrefix = true;
}
