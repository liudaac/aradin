package cn.aradin.cache.caffeine.starter.actuate;

import java.util.Map;

import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;

import cn.aradin.cache.caffeine.manager.stats.CaffeinesonStatsService;

@Endpoint(id = "caffeineson", enableByDefault = true)
public class CaffeinesonEndpoint {

	private CaffeinesonStatsService caffeinesonStatsService;
	
	public CaffeinesonEndpoint(CaffeinesonStatsService caffeinesonStatsService) {
		// TODO Auto-generated constructor stub
		this.caffeinesonStatsService = caffeinesonStatsService;
	}
	
	@ReadOperation
	public Map<String, Map<String, Object>> caffeineson() {
		return caffeinesonStatsService.getStats();
	}
}
