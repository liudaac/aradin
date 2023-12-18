package cn.aradin.spring.redis.starter.properties;

import java.time.Duration;

public class Redis {

	/**
	 * Entry expiration. By default the entries never expire.
	 */
	private Duration timeToLive;
	
	/**
	 * Random time displacement to solve the cache avalanche issue.
	 */
	private Duration timeOffset = Duration.ZERO;

	/**
	 * Allow caching null values.
	 */
	private boolean cacheNullValues = true;

	/**
	 * Key prefix.
	 */
	private String keyPrefix;

	/**
	 * Whether to use the key prefix when writing to Redis.
	 */
	private boolean useKeyPrefix = true;

	/**
	 * Whether to enable cache statistics.
	 */
	private boolean enableStatistics;

	public Duration getTimeToLive() {
		return timeToLive;
	}

	public void setTimeToLive(Duration timeToLive) {
		this.timeToLive = timeToLive;
	}

	public Duration getTimeOffset() {
		return timeOffset;
	}

	public void setTimeOffset(Duration timeOffset) {
		this.timeOffset = timeOffset;
	}

	public boolean isCacheNullValues() {
		return cacheNullValues;
	}

	public void setCacheNullValues(boolean cacheNullValues) {
		this.cacheNullValues = cacheNullValues;
	}

	public String getKeyPrefix() {
		return keyPrefix;
	}

	public void setKeyPrefix(String keyPrefix) {
		this.keyPrefix = keyPrefix;
	}

	public boolean isUseKeyPrefix() {
		return useKeyPrefix;
	}

	public void setUseKeyPrefix(boolean useKeyPrefix) {
		this.useKeyPrefix = useKeyPrefix;
	}

	public boolean isEnableStatistics() {
		return enableStatistics;
	}

	public void setEnableStatistics(boolean enableStatistics) {
		this.enableStatistics = enableStatistics;
	}
}
