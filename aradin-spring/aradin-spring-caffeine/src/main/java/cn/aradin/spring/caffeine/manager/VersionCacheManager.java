package cn.aradin.spring.caffeine.manager;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

public interface VersionCacheManager extends CacheManager {
	
	void evictVersion(String cacheName, long version);

	void evict(String cacheName);
	
	long version(String cacheName);
	
	void version(String cacheName, long version);
	
	void init(String cacheName, long version);
	
	Cache getCacheIfExist(String cacheName);
}
