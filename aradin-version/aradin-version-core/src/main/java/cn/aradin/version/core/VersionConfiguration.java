package cn.aradin.version.core;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cn.aradin.version.core.handler.DefaultVersionHandler;
import cn.aradin.version.core.handler.IVersionHandler;

/**
 * Version Configuration
 *
 */
@Configuration
public class VersionConfiguration {
	
	@Bean
	@ConditionalOnMissingBean
	public IVersionHandler defaultVersionHandler() {
		return new DefaultVersionHandler();
	}
}
