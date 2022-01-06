package cn.aradin.version.core;

import java.util.List;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cn.aradin.version.core.dispatcher.VersionDispatcher;
import cn.aradin.version.core.gentor.DefaultVersionGentor;
import cn.aradin.version.core.gentor.IVersionGentor;
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
	
	@Bean
	@ConditionalOnMissingBean
	public IVersionGentor defaultVersionGentor() {
		return new DefaultVersionGentor();
	}
	
	@Bean
	@ConditionalOnMissingBean
	public VersionDispatcher versionDispatcher(List<IVersionHandler> versionHandlers) {
		return new VersionDispatcher(versionHandlers);
	}
}
