package cn.aradin.spring.caffeine.manager.version;

import org.apache.commons.lang3.StringUtils;

import cn.aradin.spring.caffeine.manager.VersionCacheManager;
import cn.aradin.spring.caffeine.manager.properties.CaffeinesonProperties;
import cn.aradin.version.core.handler.IVersionHandler;

/**
 * Version handler for caffeine
 * @author daliu
 *
 */
public class CaffeinesonVersionHandler implements IVersionHandler {

	private VersionCacheManager caffeinesonCacheManager;
	private CaffeinesonProperties caffeinesonProperties;
	
	public CaffeinesonVersionHandler(VersionCacheManager caffeinesonCacheManager,
			CaffeinesonProperties caffeinesonProperties) {
		// TODO Auto-generated constructor stub
		this.caffeinesonCacheManager = caffeinesonCacheManager;
		this.caffeinesonProperties = caffeinesonProperties;
	}
	
	@Override
	public String get(String group, String key) {
		// TODO Auto-generated method stub
		return String.valueOf(caffeinesonCacheManager.version(key));
	}

	@Override
	public boolean support(String group, String key) {
		// TODO Auto-generated method stub
		if ("defaults".equals(key) 
				|| (caffeinesonProperties.getConfigs() != null && caffeinesonProperties.getConfigs().containsKey(key) && caffeinesonProperties.getConfigs().get(key).isVersioned())) {
			return true;
		}
		return false;
	}

	@Override
	public void version(String group, String key, String version) {
		// TODO Auto-generated method stub
		if (support(group, key) && StringUtils.isNumeric(version)) {
			caffeinesonCacheManager.version(key, Long.parseLong(version));
		}
	}

	@Override
	public void changed(String group, String key, String version) {
		// TODO Auto-generated method stub
		version(group, key, version);
	}

}
