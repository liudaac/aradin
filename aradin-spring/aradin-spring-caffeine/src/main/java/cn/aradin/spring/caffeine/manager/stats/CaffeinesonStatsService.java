package cn.aradin.spring.caffeine.manager.stats;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.github.benmanes.caffeine.cache.stats.CacheStats;

import cn.aradin.spring.caffeine.cache.Caffeineson;
import cn.aradin.spring.caffeine.manager.VersionCacheManager;

public class CaffeinesonStatsService {
	
	private VersionCacheManager cacheManager;
	
	public CaffeinesonStatsService(VersionCacheManager cacheManager) {
		// TODO Auto-generated constructor stub
		this.cacheManager = cacheManager;
	}
	
	public Map<String, CacheStats> getStats() {
		Collection<String> cacheNames = cacheManager.getCacheNames();
		if (cacheNames != null) {
			Map<String, CacheStats> statMap = new HashMap<String, CacheStats>();
			for(String cacheName:cacheNames) {
				cacheName = cacheName.substring(cacheName.lastIndexOf("#")+1);
				Caffeineson cache = (Caffeineson)cacheManager.getCache(cacheName);
				if (cache != null) {
					statMap.put(cacheName, cache.stats());
				}
			}
			return statMap;
		}
		return null;
	}
}
