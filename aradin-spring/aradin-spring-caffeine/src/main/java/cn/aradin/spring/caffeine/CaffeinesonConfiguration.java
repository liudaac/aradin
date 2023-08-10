package cn.aradin.spring.caffeine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cn.aradin.spring.caffeine.cache.Caffeineson;
import cn.aradin.spring.caffeine.manager.CaffeinesonCacheManager;
import cn.aradin.spring.caffeine.manager.VersionCacheManager;
import cn.aradin.spring.caffeine.manager.properties.CaffeinesonProperties;
import cn.aradin.spring.caffeine.manager.stats.CaffeinesonStatsService;
import cn.aradin.spring.caffeine.manager.version.CaffeinesonVersionHandler;
import cn.aradin.version.core.handler.IVersionBroadHandler;

/**
 * CaffeineManager
 *
 */
@Configuration
@EnableConfigurationProperties(CaffeinesonProperties.class)
public class CaffeinesonConfiguration {
	
	public final static String CACHE_MANAGER = "caffeinesonCacheManager";
	private static final Logger log = LoggerFactory.getLogger(CaffeinesonConfiguration.class);
	
	@Bean
	@ConditionalOnMissingBean
	Caffeineson Caffeineson(CaffeinesonProperties caffeinesonProperties, IVersionBroadHandler versionBroadHandler) {
		return new Caffeineson("caffeineson", caffeinesonProperties.getDefaults(), versionBroadHandler);
	}
	
	@Bean
	VersionCacheManager caffeinesonCacheManager(CaffeinesonProperties caffeinesonProperties,
			IVersionBroadHandler versionBroadHandler) {
		return new CaffeinesonCacheManager(caffeinesonProperties, versionBroadHandler);
	}
	
	@Bean
	CaffeinesonStatsService caffeinesonStatsService(VersionCacheManager caffeinesonCacheManager) {
		return new CaffeinesonStatsService(caffeinesonCacheManager);
	}
	
	@Bean
	@ConditionalOnProperty(name = "aradin.cache.caffeine", havingValue = "")
	CaffeinesonVersionHandler caffeinesonVersionHandler(VersionCacheManager caffeinesonCacheManager, CaffeinesonProperties caffeinesonProperties) {
		log.warn("CaffeinesonVersionHandler Initing");
		return new CaffeinesonVersionHandler(caffeinesonCacheManager, caffeinesonProperties);
	}
}
