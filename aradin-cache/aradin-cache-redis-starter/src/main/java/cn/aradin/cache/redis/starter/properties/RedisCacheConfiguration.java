package cn.aradin.cache.redis.starter.properties;

import java.time.Duration;

public class RedisCacheConfiguration {
	private Duration ttl = Duration.ofSeconds(3600L);
	private boolean cacheNullValues = true;
	private String keyPrefix = "aradin_";
	private boolean usePrefix = true;
	private Duration ttlOffset = Duration.ZERO;
	public Duration getTtl() {
		return ttl;
	}
	public void setTtl(Duration ttl) {
		this.ttl = ttl;
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
	public boolean isUsePrefix() {
		return usePrefix;
	}
	public void setUsePrefix(boolean usePrefix) {
		this.usePrefix = usePrefix;
	}
	public Duration getTtlOffset() {
		return ttlOffset;
	}
	public void setTtlOffset(Duration ttlOffset) {
		this.ttlOffset = ttlOffset;
	}
}
