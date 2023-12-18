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
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
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
import org.springframework.data.redis.cache.FixedDurationTtlFunction;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheManager.RedisCacheManagerBuilder;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;
import org.springframework.util.Assert;

import com.alibaba.fastjson2.JSONObject;

import cn.aradin.spring.redis.starter.cache.RandomDurationTtlFunction;
import cn.aradin.spring.redis.starter.properties.RedisCacheProperties;

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
@EnableConfigurationProperties({ RedisCacheProperties.class })
@AutoConfigureAfter({ AradinRedisAutoConfiguration.class })
public class AradinRedisManagerAutoConfiguration {
	
	private final static Logger log = LoggerFactory.getLogger(AradinRedisManagerAutoConfiguration.class);
	
	public final static String CACHE_MANAGER = "cacheManager";

	@Bean
	@ConditionalOnMissingBean
	public CacheManagerCustomizers cacheManagerCustomizers(ObjectProvider<CacheManagerCustomizer<?>> customizers) {
		return new CacheManagerCustomizers(customizers.orderedStream().collect(Collectors.toList()));
	}

	@Bean
	public CacheManagerValidator cacheAutoConfigurationValidator(
			RedisCacheProperties redisCacheProperties, ObjectProvider<CacheManager> cacheManager) {
		return new CacheManagerValidator(cacheManager);
	}
	
	@Bean
	public RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer(RedisCacheProperties redisCacheProperties,
			ResourceLoader resourceLoader) {
		return new RedisCacheManagerBuilderCustomizer() {
			@Override
			public void customize(RedisCacheManagerBuilder builder) {
				// TODO Auto-generated method stub
				if (!redisCacheProperties.getCaches().isEmpty()) {
					if (log.isDebugEnabled()) {
						log.debug("RedisCacheManager {}", JSONObject.toJSONString(redisCacheProperties.getCaches()));
					}
					Map<String, RedisCacheConfiguration> configMap = new HashMap<String, RedisCacheConfiguration>();
					for(String cacheName:redisCacheProperties.getCaches().keySet()) {
						cn.aradin.spring.redis.starter.properties.Redis redis = redisCacheProperties.getCaches().get(cacheName);
						configMap.put(cacheName, createConfiguration(redis, resourceLoader.getClassLoader()));
					}
					builder.withInitialCacheConfigurations(configMap);
				}
			}
		};
	}
	
	/**
	 * 原生实现，由于原生存在对CacheManager的单实例限制
	 * @return The primary cache Manager
	 */
	@Bean
	@Primary
	public RedisCacheManager cacheManager(CacheProperties cacheProperties, CacheManagerCustomizers cacheManagerCustomizers,
			ObjectProvider<org.springframework.data.redis.cache.RedisCacheConfiguration> redisCacheConfiguration,
			ObjectProvider<RedisCacheManagerBuilderCustomizer> redisCacheManagerBuilderCustomizers,
			RedisConnectionFactory redisConnectionFactory, ResourceLoader resourceLoader) {
		RedisCacheManagerBuilder builder = RedisCacheManager.builder(redisConnectionFactory)
			.cacheDefaults(
					determineConfiguration(cacheProperties, redisCacheConfiguration, resourceLoader.getClassLoader()));
		List<String> cacheNames = cacheProperties.getCacheNames();
		if (!cacheNames.isEmpty()) {
			builder.initialCacheNames(new LinkedHashSet<>(cacheNames));
		}
		if (cacheProperties.getRedis().isEnableStatistics()) {
			builder.enableStatistics();
		}
		redisCacheManagerBuilderCustomizers.orderedStream().forEach((customizer) -> customizer.customize(builder));
		return cacheManagerCustomizers.customize(builder.build());
	}
	
	private org.springframework.data.redis.cache.RedisCacheConfiguration determineConfiguration(
			CacheProperties cacheProperties,
			ObjectProvider<org.springframework.data.redis.cache.RedisCacheConfiguration> redisCacheConfiguration,
			ClassLoader classLoader) {
		return redisCacheConfiguration.getIfAvailable(() -> createConfiguration(cacheProperties, classLoader));
	}

	private org.springframework.data.redis.cache.RedisCacheConfiguration createConfiguration(
			CacheProperties cacheProperties, ClassLoader classLoader) {
		Redis redisProperties = cacheProperties.getRedis();
		org.springframework.data.redis.cache.RedisCacheConfiguration config = org.springframework.data.redis.cache.RedisCacheConfiguration
			.defaultCacheConfig();
		config = config
			.serializeValuesWith(SerializationPair.fromSerializer(new JdkSerializationRedisSerializer(classLoader)));
		if (redisProperties.getTimeToLive() != null) {
			config = config.entryTtl(new FixedDurationTtlFunction(redisProperties.getTimeToLive()));
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
	
	private org.springframework.data.redis.cache.RedisCacheConfiguration createConfiguration(
			cn.aradin.spring.redis.starter.properties.Redis redisProperties, ClassLoader classLoader) {
		org.springframework.data.redis.cache.RedisCacheConfiguration config = org.springframework.data.redis.cache.RedisCacheConfiguration
				.defaultCacheConfig();
		config = config
			.serializeValuesWith(SerializationPair.fromSerializer(new JdkSerializationRedisSerializer(classLoader)));
		if (redisProperties.getTimeToLive() != null) {
			config = config.entryTtl(new RandomDurationTtlFunction(redisProperties.getTimeToLive(), redisProperties.getTimeOffset()));
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
