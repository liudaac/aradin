package cn.aradin.spring.core.bean.support;

import java.io.IOException;
import java.util.Properties;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.DefaultPropertySourceFactory;
import org.springframework.core.io.support.EncodedResource;

import lombok.extern.slf4j.Slf4j;

/**
 * Support Importing YML Property File To Configuration Class
 * @author daliu
 *
 */
@Slf4j
public class AradinPropertySourceFactory extends DefaultPropertySourceFactory {
	
	@Override
	public PropertySource<?> createPropertySource(String name, EncodedResource resource) throws IOException {
		String sourceName = name != null ? name : resource.getResource().getFilename();
		if (log.isDebugEnabled()) {
			log.debug("Register Property {}", sourceName);
		}
		if (!resource.getResource().exists()) {
			if (log.isDebugEnabled()) {
				log.debug("Resource Not Exsit{}", sourceName);
			}
			return new PropertiesPropertySource(sourceName, new Properties());
		} else if (sourceName.endsWith(".yml") || sourceName.endsWith(".yaml")) {
			if (log.isDebugEnabled()) {
				log.debug("Found YML {}", sourceName);
			}
			Properties propertiesFromYaml = loadYml(resource);
			return new PropertiesPropertySource(sourceName, propertiesFromYaml);
		} else {
			if (log.isDebugEnabled()) {
				log.debug("Found Property {}", sourceName);
			}
			return super.createPropertySource(name, resource);
		}
	}

	private Properties loadYml(EncodedResource resource) throws IOException {
		YamlPropertiesFactoryBean factory = new YamlPropertiesFactoryBean();
		factory.setResources(resource.getResource());
		factory.afterPropertiesSet();
		return factory.getObject();
	}
}
