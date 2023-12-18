package cn.aradin.spring.redis.starter.properties;

import java.time.Duration;

public class Redis extends org.springframework.boot.autoconfigure.cache.CacheProperties.Redis {
	
	/**
	 * Random time displacement to solve the cache avalanche issue.
	 */
	private Duration timeOffset = Duration.ZERO;

	public Duration getTimeOffset() {
		return timeOffset;
	}

	public void setTimeOffset(Duration timeOffset) {
		this.timeOffset = timeOffset;
	}
}
