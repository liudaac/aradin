package cn.aradin.spring.velocity.starter;

import static cn.aradin.spring.velocity.template.VelocityTemplateAvailabilityProvider.*;

import java.io.IOException;
import java.util.Properties;

import javax.annotation.PostConstruct;
import jakarta.servlet.Servlet;

import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.VelocityException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnNotWebApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.template.TemplateLocation;
import org.springframework.boot.autoconfigure.web.ConditionalOnEnabledResourceChain;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.resource.ResourceUrlEncodingFilter;

import cn.aradin.spring.velocity.servlet.EmbeddedVelocityViewResolver;
import cn.aradin.spring.velocity.starter.properties.VelocityProperties;
import cn.aradin.spring.velocity.ui.VelocityEngineFactory;
import cn.aradin.spring.velocity.ui.VelocityEngineFactoryBean;
import cn.aradin.spring.velocity.view.VelocityConfig;
import cn.aradin.spring.velocity.view.VelocityConfigurer;

@Configuration
@ConditionalOnClass({ VelocityEngine.class, VelocityEngineFactory.class })
@AutoConfigureAfter(WebMvcAutoConfiguration.class)
@EnableConfigurationProperties(VelocityProperties.class)
public class AradinVelocityAutoConfiguration {

	private final static Logger log = LoggerFactory.getLogger(AradinVelocityAutoConfiguration.class);
	
	private final ApplicationContext applicationContext;

	private final VelocityProperties properties;

	public AradinVelocityAutoConfiguration(ApplicationContext applicationContext, VelocityProperties properties) {
		this.applicationContext = applicationContext;
		this.properties = properties;
	}

	@PostConstruct
	public void checkTemplateLocationExists() {
		if (this.properties.isCheckTemplateLocation()) {
			TemplateLocation location = new TemplateLocation(this.properties.getResourceLoaderPath());
			if (!location.exists(this.applicationContext)) {
				log.warn("Cannot find template location: " + location
						+ " (please add some templates, check your Velocity " + "configuration, or set spring.velocity."
						+ "checkTemplateLocation=false)");
			}
		}
	}

	protected static class VelocityConfiguration {

		@Autowired
		protected VelocityProperties properties;

		protected void applyProperties(VelocityEngineFactory factory) {
			if (!this.properties.getResourceLoaderPath().contains("classpath:/templates")) {
				this.properties.setResourceLoaderPath(this.properties.getResourceLoaderPath()+","+DEFAULT_RESOURCE_LOADER_PATH);
			}
			factory.setResourceLoaderPath(this.properties.getResourceLoaderPath());
			factory.setPreferFileSystemAccess(this.properties.isPreferFileSystemAccess());
			Properties velocityProperties = new Properties();
			velocityProperties.setProperty("input.encoding", this.properties.getCharsetName());
			velocityProperties.setProperty("output.encoding", this.properties.getCharsetName());
			velocityProperties.putAll(this.properties.getProperties());
			factory.setVelocityProperties(velocityProperties);
		}

	}

	@Configuration
	@ConditionalOnNotWebApplication
	public static class VelocityNonWebConfiguration extends VelocityConfiguration {

		@Bean
		@ConditionalOnMissingBean
		public VelocityEngineFactoryBean velocityConfiguration() {
			VelocityEngineFactoryBean velocityEngineFactoryBean = new VelocityEngineFactoryBean();
			applyProperties(velocityEngineFactoryBean);
			return velocityEngineFactoryBean;
		}

	}

	@Configuration
	@ConditionalOnClass(Servlet.class)
	@ConditionalOnWebApplication
	public static class VelocityWebConfiguration extends VelocityConfiguration {

		@Bean
		@ConditionalOnMissingBean(VelocityConfig.class)
		public VelocityConfigurer velocityConfigurer() {
			VelocityConfigurer configurer = new VelocityConfigurer();
			applyProperties(configurer);
			return configurer;
		}

		@Bean
		public VelocityEngine velocityEngine(VelocityConfigurer configurer)
				throws VelocityException, IOException {
			return configurer.getVelocityEngine();
		}

		@Bean
		@ConditionalOnMissingBean(name = "velocityViewResolver")
		@ConditionalOnProperty(name = "spring.velocity.enabled", matchIfMissing = true)
		public EmbeddedVelocityViewResolver velocityViewResolver() {
			EmbeddedVelocityViewResolver resolver = new EmbeddedVelocityViewResolver();
			this.properties.applyToMvcViewResolver(resolver);
			return resolver;
		}

		@Bean
		@ConditionalOnMissingBean
		@ConditionalOnEnabledResourceChain
		public ResourceUrlEncodingFilter resourceUrlEncodingFilter() {
			return new ResourceUrlEncodingFilter();
		}

	}
}
