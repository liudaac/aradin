package cn.aradin.spring.caffeine.manager;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;

import com.github.benmanes.caffeine.cache.RemovalListener;

import cn.aradin.spring.caffeine.cache.Caffeineson;
import cn.aradin.spring.caffeine.cache.config.CaffeinesonConfig;
import cn.aradin.spring.caffeine.manager.properties.CaffeinesonProperties;
import cn.aradin.version.core.handler.IVersionBroadHandler;

public class CaffeinesonCacheManager implements VersionCacheManager{

	private static final Logger log = LoggerFactory.getLogger(CaffeinesonCacheManager.class);
	
	ConcurrentHashMap<String, Long> versionMap = new ConcurrentHashMap<String, Long>();
	
	ConcurrentHashMap<String, Caffeineson> instanceMap = new ConcurrentHashMap<String, Caffeineson>();
	
	Map<String, CaffeinesonConfig> configs = new ConcurrentHashMap<String, CaffeinesonConfig>();
	
	CaffeinesonConfig defaultConfig = new CaffeinesonConfig();
	
	RemovalListener<Object, Object> listener;
	
	boolean versioned = false;//是否接受版本控制
	
	IVersionBroadHandler versionBroadHandler;
	
	String versionGroup;
	
	public CaffeinesonCacheManager(CaffeinesonProperties caffeinesonProperties,
			RemovalListener<Object, Object> listener,
			IVersionBroadHandler versionBroadHandler) {
		// TODO Auto-generated constructor stub
		this.configs = caffeinesonProperties.getConfigs();
		this.defaultConfig = caffeinesonProperties.getDefaults();
		this.listener = listener;
		this.versioned = caffeinesonProperties.isVersioned();
		this.versionBroadHandler = versionBroadHandler;
		this.versionGroup = caffeinesonProperties.getGroup();
	}
	
	protected CaffeinesonConfig createCaffeinesonConfig() {
		return defaultConfig;
	}
	
	protected Caffeineson buildCache(String name) {
		long version = version(name);
		String exact_name = version+"##"+name;
		Caffeineson cache = new Caffeineson(name, versionGroup, versioned, configs.get(name), versionBroadHandler);
		Caffeineson oldCache = instanceMap.putIfAbsent(exact_name, cache);
		if (oldCache != null) {
			cache = oldCache;
		}
		return cache;
	}
	
	@Override
	public Cache getCache(String name) {
		// TODO Auto-generated method stub
		long version = version(name);
		String exact_name = version+"##"+name;
		Caffeineson cache = instanceMap.get(exact_name);
		if (cache != null) {
			return cache;
		}
		CaffeinesonConfig caffeineconfig = configs.get(name);
		if (caffeineconfig == null) {
			caffeineconfig = createCaffeinesonConfig();
			configs.put(name, caffeineconfig);
		}
		return buildCache(name);
	}

	@Override
	public Collection<String> getCacheNames() {
		// TODO Auto-generated method stub
		return versionMap.keySet();
	}

	@Override
	public void evictVersion(String cacheName, long version) {
		// TODO Auto-generated method stub
		String exact_name = version+"##"+cacheName;
		Caffeineson cache = (Caffeineson)getCacheIfExist(exact_name);
		if (cache != null) {
			cache.clear();
			instanceMap.remove(exact_name);
		}
	}

	@Override
	public void evict(String cacheName) {
		// TODO Auto-generated method stub
		long version = versionMap.get(cacheName);
		evictVersion(cacheName, version);
	}

	@Override
	public long version(String cacheName) {
		// TODO Auto-generated method stub
		Long version = versionMap.get(cacheName);
		return version==null?0L:version;
	}

	@Override
	public void version(String cacheName, long version) {
		// TODO Auto-generated method stub
		long lastVersion = version(cacheName);
		if (log.isDebugEnabled()) {
			log.debug("更新版本 {} 到 {}", lastVersion, version);
		}
		evictVersion(cacheName, lastVersion);
		versionMap.put(cacheName, version);
	}

	@Override
	public void init(String cacheName, long version) {
		// TODO Auto-generated method stub
		if (log.isDebugEnabled()) {
			log.debug("初始化版本 {}", version);
		}
		Long cur_version = versionMap.get(cacheName);
		if (cur_version == null) {
			versionMap.put(cacheName, cur_version);
		}
	}

	@Override
	public Cache getCacheIfExist(String cacheName) {
		// TODO Auto-generated method stub
		return instanceMap.get(cacheName);
	}

}
