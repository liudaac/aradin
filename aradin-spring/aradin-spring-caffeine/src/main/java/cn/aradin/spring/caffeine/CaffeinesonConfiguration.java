package cn.aradin.spring.caffeine;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.benmanes.caffeine.cache.RemovalListener;

import cn.aradin.spring.caffeine.cache.Caffeineson;
import cn.aradin.spring.caffeine.cache.listener.CaffeinesonRemovalListener;
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
	
	@Bean
	@ConditionalOnMissingBean
	RemovalListener<Object, Object> removalListener() {
		return new CaffeinesonRemovalListener();
	}
	
	@Bean
	@ConditionalOnMissingBean
	Caffeineson Caffeineson(CaffeinesonProperties caffeinesonProperties, CaffeinesonRemovalListener removalListener, IVersionBroadHandler versionBroadHandler) {
		return new Caffeineson("caffeineson", caffeinesonProperties.getGroup(), caffeinesonProperties.isVersioned(), caffeinesonProperties.getDefaults(), removalListener, versionBroadHandler);
	}
	
	@Bean
	VersionCacheManager caffeinesonCacheManager(CaffeinesonProperties caffeinesonProperties, 
			CaffeinesonRemovalListener removalListener,
			IVersionBroadHandler versionBroadHandler) {
		return new CaffeinesonCacheManager(caffeinesonProperties, removalListener, versionBroadHandler);
	}
	
	@Bean
	CaffeinesonStatsService caffeinesonStatsService(CaffeinesonCacheManager caffeinesonCacheManager) {
		return new CaffeinesonStatsService(caffeinesonCacheManager);
	}
	
	@Bean
	@ConditionalOnProperty(name = "aradin.cache.caffeine.versioned", havingValue = "true")
	CaffeinesonVersionHandler caffeinesonVersionHandler(CaffeinesonCacheManager caffeinesonCacheManager, CaffeinesonProperties caffeinesonProperties) {
		return new CaffeinesonVersionHandler(caffeinesonCacheManager, caffeinesonProperties);
	}
}
