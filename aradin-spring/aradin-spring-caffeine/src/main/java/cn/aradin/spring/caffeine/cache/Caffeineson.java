package cn.aradin.spring.caffeine.cache;

import java.lang.reflect.Constructor;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.support.AbstractValueAdaptingCache;

import com.alibaba.fastjson2.JSONObject;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.RemovalListener;
import com.github.benmanes.caffeine.cache.stats.CacheStats;
import cn.aradin.spring.caffeine.cache.config.CaffeinesonConfig;
import cn.aradin.spring.caffeine.cache.listener.CaffeinesonRemovalListener;
import cn.aradin.version.core.handler.IVersionBroadHandler;

public class Caffeineson extends AbstractValueAdaptingCache {

	private final static Logger log = LoggerFactory.getLogger(Caffeineson.class);
	
	private Cache<Object, Object> caffeineCache;
	private final String name;
	private final boolean versioned;
	private IVersionBroadHandler versionBroadHandler;
	
	public Caffeineson(String name,
			CaffeinesonConfig caffeineConfig,
			IVersionBroadHandler versionBroadHandler) {
		super(caffeineConfig.isAllowNullValues());
		this.name = name;
		this.versioned = caffeineConfig.isVersioned();
		this.versionBroadHandler = versionBroadHandler;
		caffeineCache = caffeineCache(caffeineConfig, new CaffeinesonRemovalListener(name));
	}
	
	/**
	 * construct caffeine
	 * @param config to build a cache
	 * @param listener key-remove event
	 * @return caffeine cache
	 */
    protected Cache<Object, Object> caffeineCache(CaffeinesonConfig config, RemovalListener<Object, Object> listener){
		Caffeine<Object, Object> cacheBuilder = Caffeine.newBuilder();
		if(config.getExpireAfterAccess() > 0) {
			cacheBuilder.expireAfterAccess(config.getExpireAfterAccess(), TimeUnit.MILLISECONDS);
		}
		if(config.getExpireAfterWrite() > 0) {
			cacheBuilder.expireAfterWrite(config.getExpireAfterWrite(), TimeUnit.MILLISECONDS);
		}
		if(config.getInitialCapacity() > 0) {
			cacheBuilder.initialCapacity(config.getInitialCapacity());
		}
		if(config.getMaximumSize() > 0) {
			cacheBuilder.maximumSize(config.getMaximumSize());
		}
		if(config.getRefreshAfterWrite() > 0) {
			//该设置仅支持存在cacheloader即实现按照key构建缓存值的途径时才允许设置，此处不可用
			cacheBuilder.refreshAfterWrite(config.getRefreshAfterWrite(), TimeUnit.MILLISECONDS);
		}
		if (config.isSoft()) {
			cacheBuilder.softValues();//配置成软引用
		}
		if (listener != null) {
			cacheBuilder.removalListener(listener);
		}
		if (config.isRecordStats()) {
			cacheBuilder.recordStats();
//			cacheBuilder.recordStats(ConcurrentStatsCounter::new);
		}
		if (log.isDebugEnabled()) {
			log.debug("初始化Caffeine实例 {},配置信息{}", name, JSONObject.toJSONString(config));
		}
		return cacheBuilder.build();
	}
	
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}

	@Override
	public Object getNativeCache() {
		// TODO Auto-generated method stub
		return caffeineCache;
	}

	public long estimatedSize() {
		return caffeineCache.estimatedSize();
	}
	
	public void cleanUp() {
		caffeineCache.cleanUp();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> T get(Object key, Callable<T> valueLoader) {
		// TODO Auto-generated method stub
		Object value = lookup(key);
		if (value == null) {
			synchronized (key.toString().intern()) {
				try {
					value = lookup(key);
					if (value != null) {
						return (T)value;
					}
					value = valueLoader.call();
					put(key, toStoreValue(value));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					try {
		                Class<?> c = Class.forName("org.springframework.cache.Cache$ValueRetrievalException");
		                Constructor<?> constructor = c.getConstructor(Object.class, Callable.class, Throwable.class);
		                RuntimeException exception = (RuntimeException) constructor.newInstance(key, valueLoader, e.getCause());
		                throw exception;
		           }catch (Exception e1) {
		        	   throw new IllegalStateException(e1);
		           }
				}
			}
		}
		return (T)value;
	}

	@Override
	public void put(Object key, Object value) {
		// TODO Auto-generated method stub
		if (log.isDebugEnabled()) {
			log.debug("Cache {} 更新Key {}", name, key);
		}
		caffeineCache.put(key, toStoreValue(value));
	}

	@Override
	public ValueWrapper putIfAbsent(Object key, Object value) {
		// TODO Auto-generated method stub
		Object prevValue = caffeineCache.getIfPresent(key);
		if (prevValue == null) {
			synchronized (key.toString().intern()) {
				prevValue = caffeineCache.getIfPresent(key);
				if (prevValue == null) {
					caffeineCache.put(key, toStoreValue(value));
					prevValue = value;
				}
			}
		}
		return toValueWrapper(prevValue);
	}

	@Override
	public void evict(Object key) {
		// TODO Auto-generated method stub
		if (log.isDebugEnabled()) {
			log.debug("Cache {} 删除Key {}", name, key);
		}
		caffeineCache.invalidate(key);
		if (versioned) {
			versionBroadHandler.broadcast(name);
		}
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		caffeineCache.invalidateAll();
	}

	@Override
	protected Object lookup(Object key) {
		// TODO Auto-generated method stub
		return caffeineCache.getIfPresent(key);
	}
	
	/**
	 * 获取统计信息
	 * @return
	 */
	public CacheStats stats() {
		return caffeineCache.stats();
	}
}
