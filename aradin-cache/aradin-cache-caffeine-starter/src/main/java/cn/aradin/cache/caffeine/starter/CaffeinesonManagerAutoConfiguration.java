package cn.aradin.cache.caffeine.starter;

import org.springframework.boot.actuate.autoconfigure.endpoint.condition.ConditionalOnAvailableEndpoint;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import cn.aradin.cache.caffeine.CaffeinesonConfiguration;
import cn.aradin.cache.caffeine.manager.stats.CaffeinesonStatsService;
import cn.aradin.cache.caffeine.starter.actuate.CaffeinesonEndpoint;
import cn.aradin.version.core.VersionConfiguration;

@EnableCaching
@Configuration
@Import({CaffeinesonConfiguration.class, VersionConfiguration.class})
public class CaffeinesonManagerAutoConfiguration {
	
	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnAvailableEndpoint(endpoint = CaffeinesonEndpoint.class)
	public CaffeinesonEndpoint caffeinesonEndpoint(CaffeinesonStatsService caffeinesonStatsService) {
		return new CaffeinesonEndpoint(caffeinesonStatsService);
	}
}
