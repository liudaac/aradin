package cn.aradin.spring.redis.starter;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.cache.CacheManagerCustomizer;
import org.springframework.boot.autoconfigure.cache.CacheManagerCustomizers;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.autoconfigure.cache.CacheProperties.Redis;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.CacheAspectSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheManager.RedisCacheManagerBuilder;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSONObject;

import cn.aradin.spring.redis.starter.properties.RedisCacheManagerProperties;

/**
 * {@link org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration}
 * 通过SelectImporter
 * 引入所有cachetype的默认Configuration，然后根据各个Configuration的Condition决定初始化哪一个
 * org.springframework.boot.autoconfigure.cache.RedisCacheConfiguration
 * 
 * @author liuda
 *
 */
@Configuration
@ConditionalOnClass(CacheManager.class)
@ConditionalOnBean(CacheAspectSupport.class)
@EnableConfigurationProperties({ RedisCacheManagerProperties.class })
@AutoConfigureAfter({ AradinRedisAutoConfiguration.class })
public class AradinRedisManagerAutoConfiguration {
	
	private final static Logger log = LoggerFactory.getLogger(AradinRedisManagerAutoConfiguration.class);
	
	public final static String CACHE_MANAGER = "cacheManager";

	@Bean
	@ConditionalOnMissingBean
	public CacheManagerCustomizers cacheManagerCustomizers(ObjectProvider<CacheManagerCustomizer<?>> customizers) {
		log.debug("CacheManagerCustomizers Initial");
		return new CacheManagerCustomizers(customizers.orderedStream().collect(Collectors.toList()));
	}

	@Bean
	public CacheManagerValidator cacheAutoConfigurationValidator(
			RedisCacheManagerProperties redisCacheManagerProperties, ObjectProvider<CacheManager> cacheManager) {
		log.debug("CacheManagerValidator Initial");
		return new CacheManagerValidator(cacheManager);
	}

	/**
	 * 原生实现，由于原生存在对CacheManager的单实例限制
	 * 
	 * @param redisConnectionFactory      redisConnectionFactory
	 * @param resourceLoader              resourceLoader
	 * @param redisCacheManagerProperties redisCacheManagerProperties
	 * @param cacheManagerCustomizers     cacheManagerCustomizers
	 * @return The primary cache Manager
	 */
	@Bean
	@Primary
	public RedisCacheManager cacheManager(RedisConnectionFactory redisConnectionFactory, ResourceLoader resourceLoader,
			RedisCacheManagerProperties redisCacheManagerProperties, CacheManagerCustomizers cacheManagerCustomizers) {
		log.debug("RedisCacheManager Initial");
		CacheProperties cacheProperties = new CacheProperties();
		cacheProperties.getRedis().setTimeToLive(redisCacheManagerProperties.getDefaults().getTtl());
		cacheProperties.getRedis().setCacheNullValues(redisCacheManagerProperties.getDefaults().isCacheNullValues());
		cacheProperties.getRedis().setKeyPrefix(redisCacheManagerProperties.getDefaults().getKeyPrefix());
		cacheProperties.getRedis().setUseKeyPrefix(redisCacheManagerProperties.getDefaults().isUsePrefix());
		RedisCacheManagerBuilder builder = org.springframework.data.redis.cache.RedisCacheManager
				.builder(redisConnectionFactory)
				.cacheDefaults(determineConfiguration(resourceLoader.getClassLoader(), cacheProperties));
		List<String> cacheNames = cacheProperties.getCacheNames();
		if (!cacheNames.isEmpty()) {
			builder.initialCacheNames(new LinkedHashSet<>(cacheNames));
		}
		// 改造
		log.debug("RedisCacheManager {}", JSONObject.toJSONString(redisCacheManagerProperties.getConfigs()));
		if (!redisCacheManagerProperties.getConfigs().isEmpty()) {
			Map<String, RedisCacheConfiguration> configMap = new HashMap<String, RedisCacheConfiguration>();
			for (String cacheName : redisCacheManagerProperties.getConfigs().keySet()) {
				cn.aradin.spring.redis.starter.properties.RedisCacheConfiguration redisCacheConfig = redisCacheManagerProperties
						.getConfigs().get(cacheName);
				if (redisCacheConfig.isUsePrefix() && redisCacheConfig.isCacheNullValues()) {
					configMap.put(cacheName, RedisCacheConfiguration.defaultCacheConfig()
							.entryTtl(redisCacheConfig.getTtl()).prefixCacheNameWith(redisCacheConfig.getKeyPrefix()));
				} else if (redisCacheConfig.isCacheNullValues()) {
					configMap.put(cacheName, RedisCacheConfiguration.defaultCacheConfig()
							.entryTtl(redisCacheConfig.getTtl()).disableKeyPrefix());
				} else {
					configMap.put(cacheName, RedisCacheConfiguration.defaultCacheConfig()
							.entryTtl(redisCacheConfig.getTtl()).disableKeyPrefix().disableCachingNullValues());
				}
			}
			builder.withInitialCacheConfigurations(configMap);
		}
		return cacheManagerCustomizers.customize(builder.build());
	}

	private org.springframework.data.redis.cache.RedisCacheConfiguration determineConfiguration(ClassLoader classLoader,
			CacheProperties cacheProperties) {
		Redis redisProperties = cacheProperties.getRedis();
		org.springframework.data.redis.cache.RedisCacheConfiguration config = org.springframework.data.redis.cache.RedisCacheConfiguration
				.defaultCacheConfig();
		config = config.serializeValuesWith(
				SerializationPair.fromSerializer(new JdkSerializationRedisSerializer(classLoader)));
		if (redisProperties.getTimeToLive() != null) {
			config = config.entryTtl(redisProperties.getTimeToLive());
		}
		if (redisProperties.getKeyPrefix() != null) {
			config = config.prefixCacheNameWith(redisProperties.getKeyPrefix());
		}
		if (!redisProperties.isCacheNullValues()) {
			config = config.disableCachingNullValues();
		}
		if (!redisProperties.isUseKeyPrefix()) {
			config = config.disableKeyPrefix();
		}
		return config;
	}

	/**
	 * Bean used to validate that a CacheManager exists and provide a more
	 * meaningful exception.
	 */
	static class CacheManagerValidator implements InitializingBean {

		private final ObjectProvider<CacheManager> cacheManager;

		CacheManagerValidator(ObjectProvider<CacheManager> cacheManager) {
			this.cacheManager = cacheManager;
		}

		@Override
		public void afterPropertiesSet() {
			Assert.notNull(this.cacheManager.getIfAvailable(), () -> "No cache manager could "
					+ "be auto-configured, check your configuration (caching " + "type is 'Aradin Redis')");
		}

	}
}
