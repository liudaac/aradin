package cn.aradin.cache.caffeine.manager.stats;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.github.benmanes.caffeine.cache.stats.CacheStats;

import cn.aradin.cache.caffeine.cache.Caffeineson;
import cn.aradin.cache.caffeine.manager.VersionCacheManager;

public class CaffeinesonStatsService {
	
	private VersionCacheManager cacheManager;
	
	public CaffeinesonStatsService(VersionCacheManager cacheManager) {
		// TODO Auto-generated constructor stub
		this.cacheManager = cacheManager;
	}
	
	public Map<String, Map<String, Object>> getStats() {
		Collection<String> cacheNames = cacheManager.getCacheNames();
		if (cacheNames != null) {
			Map<String, Map<String, Object>> statMap = new HashMap<String, Map<String, Object>>();
			for(String cacheName:cacheNames) {
				cacheName = cacheName.substring(cacheName.lastIndexOf("#")+1);
				Caffeineson cache = (Caffeineson)cacheManager.getCache(cacheName);
				if (cache != null) {
					CacheStats stats = cache.stats();
					Map<String, Object> stat = new HashMap<>();
					stat.put("size", cache.estimatedSize());
					stat.put("evictionCount", stats.evictionCount());
					stat.put("evictionWeight", stats.evictionWeight());
					stat.put("hitCount", stats.hitCount());
					stat.put("missCount", stats.missCount());
					stat.put("hitRate", stats.hitRate());
					stat.put("loadCount", stats.loadCount());
					stat.put("loadFailureCount", stats.loadFailureCount());
					stat.put("loadSuccessCount", stats.loadSuccessCount());
					stat.put("requestCount", stats.requestCount());
					stat.put("totalLoadTime", stats.totalLoadTime());
					stat.put("averageLoadPenalty", stats.averageLoadPenalty());
					stat.put("missRate", stats.missRate());
					statMap.put(cacheName, stat);
				}
			}
			return statMap;
		}
		return null;
	}
}
