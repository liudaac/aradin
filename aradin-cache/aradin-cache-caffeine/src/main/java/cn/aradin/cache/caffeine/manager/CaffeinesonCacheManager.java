package cn.aradin.cache.caffeine.manager;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;

import cn.aradin.cache.caffeine.cache.Caffeineson;
import cn.aradin.cache.caffeine.cache.config.CaffeinesonConfig;
import cn.aradin.cache.caffeine.manager.properties.CaffeinesonProperties;
import cn.aradin.version.core.handler.IVersionBroadHandler;

public class CaffeinesonCacheManager implements VersionCacheManager{

	private static final Logger log = LoggerFactory.getLogger(CaffeinesonCacheManager.class);
	
	ConcurrentHashMap<String, Long> versionMap = new ConcurrentHashMap<String, Long>();
	
	ConcurrentHashMap<String, Caffeineson> instanceMap = new ConcurrentHashMap<String, Caffeineson>();
	
	Map<String, CaffeinesonConfig> configs = new ConcurrentHashMap<String, CaffeinesonConfig>();
	
	CaffeinesonConfig defaultConfig = new CaffeinesonConfig();
	
	IVersionBroadHandler versionBroadHandler;
	
	String versionGroup;
	
	private Timer timer;
	
	public CaffeinesonCacheManager(CaffeinesonProperties caffeinesonProperties,
			IVersionBroadHandler versionBroadHandler) {
		// TODO Auto-generated constructor stub
		this.configs = caffeinesonProperties.getConfigs();
		this.defaultConfig = caffeinesonProperties.getDefaults();
		this.versionBroadHandler = versionBroadHandler;
		if (caffeinesonProperties.getCleanInterval() != null) {
			log.info("Start cleanUp timer for interval {} ms", caffeinesonProperties.getCleanInterval().toMillis());
			timer = new Timer();
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					if (instanceMap != null && instanceMap.size() > 0) {
						Iterator<String> iterator = instanceMap.keySet().iterator();
						while (iterator.hasNext()) {
							String name = iterator.next();
							Caffeineson cache = instanceMap.get(name);
							if (cache != null) {
								name = name.substring(name.lastIndexOf("#")+1);
								CaffeinesonConfig config = configs.get(name);
								if (cache.estimatedSize() > config.getInitialCapacity()) {
									log.info("CleanUp cache {}", name);
									try {
										cache.cleanUp();
									} catch (Exception e) {
										// TODO: handle exception
										log.error("Cache {} cleanUp failed for the reason: {}", name, e.getMessage());
									}
								}
							}
						}
					}
				}
			}, 0, caffeinesonProperties.getCleanInterval().toMillis());
		}
	}
	
	protected CaffeinesonConfig createCaffeinesonConfig() {
		log.info("创建默认Caffeine配置");
		return defaultConfig;
	}
	
	protected Caffeineson buildCache(String name) {
		long version = version(name);
		String exact_name = version+"##"+name;
		Caffeineson cache = new Caffeineson(name, configs.get(name), versionBroadHandler);
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
		return instanceMap.keySet();
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
			versionMap.put(cacheName, version);
		}
	}

	@Override
	public Cache getCacheIfExist(String cacheName) {
		// TODO Auto-generated method stub
		return instanceMap.get(cacheName);
	}

}
