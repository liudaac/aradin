package cn.aradin.spring.caffeine.starter.actuate;

import java.util.Map;

import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;

import com.github.benmanes.caffeine.cache.stats.CacheStats;

import cn.aradin.spring.caffeine.manager.stats.CaffeinesonStatsService;

@Endpoint(id = "caffeineson", enableByDefault = true)
public class CaffeinesonEndpoint {

	private CaffeinesonStatsService caffeinesonStatsService;
	
	public CaffeinesonEndpoint(CaffeinesonStatsService caffeinesonStatsService) {
		// TODO Auto-generated constructor stub
		this.caffeinesonStatsService = caffeinesonStatsService;
	}
	
	@ReadOperation
	public Map<String, CacheStats> caffeineson() {
		return caffeinesonStatsService.getStats();
	}
}
