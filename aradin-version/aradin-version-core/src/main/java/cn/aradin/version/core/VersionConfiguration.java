package cn.aradin.version.core;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cn.aradin.version.core.manager.DefaultVersionManager;
import cn.aradin.version.core.manager.IVersionManager;

/**
 * Version Configuration
 *
 */
@Configuration
public class VersionConfiguration {
	
	@Bean
	@ConditionalOnMissingBean
	public IVersionManager versionManager() {
		return new DefaultVersionManager();
	}
}
