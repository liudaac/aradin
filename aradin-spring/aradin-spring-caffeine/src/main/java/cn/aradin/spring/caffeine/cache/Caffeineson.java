package cn.aradin.spring.caffeine.cache;

import java.lang.reflect.Constructor;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import org.springframework.cache.support.AbstractValueAdaptingCache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.RemovalListener;
import com.github.benmanes.caffeine.cache.stats.CacheStats;

import cn.aradin.spring.caffeine.cache.config.CaffeinesonConfig;

public class Caffeineson extends AbstractValueAdaptingCache {

	private Cache<Object, Object> caffeineCache;
	private final String name;
	
	
	public Caffeineson(String name, CaffeinesonConfig caffeineConfig, RemovalListener<Object, Object> listener) {
		super(caffeineConfig.isAllowNullValues());
		caffeineCache = caffeineCache(caffeineConfig, listener);
		this.name = name;
	}
	
	/**
     * 构造caffeine
     * @param caffeineProperties
     * @return
     */
    protected Cache<Object, Object> caffeineCache(CaffeinesonConfig caffeineProperties, RemovalListener<Object, Object> listener){
		Caffeine<Object, Object> cacheBuilder = Caffeine.newBuilder();
		if(caffeineProperties.getExpireAfterAccess() > 0) {
			cacheBuilder.expireAfterAccess(caffeineProperties.getExpireAfterAccess(), TimeUnit.MILLISECONDS);
		}
		if(caffeineProperties.getExpireAfterWrite() > 0) {
			cacheBuilder.expireAfterWrite(caffeineProperties.getExpireAfterWrite(), TimeUnit.MILLISECONDS);
		}
		if(caffeineProperties.getInitialCapacity() > 0) {
			cacheBuilder.initialCapacity(caffeineProperties.getInitialCapacity());
		}
		if(caffeineProperties.getMaximumSize() > 0) {
			cacheBuilder.maximumSize(caffeineProperties.getMaximumSize());
		}
		if(caffeineProperties.getRefreshAfterWrite() > 0) {
			//该设置仅支持存在cacheloader即实现按照key构建缓存值的途径时才允许设置，此处不可用
			cacheBuilder.refreshAfterWrite(caffeineProperties.getRefreshAfterWrite(), TimeUnit.MILLISECONDS);
		}
		if (caffeineProperties.isSoft()) {
			cacheBuilder.softValues();//配置成软引用
		}
		if (listener != null) {
			cacheBuilder.removalListener(listener);
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
					put(key, value);
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
		caffeineCache.put(key, value);
	}

	@Override
	public ValueWrapper putIfAbsent(Object key, Object value) {
		// TODO Auto-generated method stub
		Object prevValue = caffeineCache.getIfPresent(key);
		if (prevValue == null) {
			synchronized (key.toString().intern()) {
				prevValue = caffeineCache.getIfPresent(key);
				if (prevValue == null) {
					caffeineCache.put(key, value);
					prevValue = value;
				}
			}
		}
		return toValueWrapper(prevValue);
	}

	@Override
	public void evict(Object key) {
		// TODO Auto-generated method stub
		caffeineCache.invalidate(key);
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
