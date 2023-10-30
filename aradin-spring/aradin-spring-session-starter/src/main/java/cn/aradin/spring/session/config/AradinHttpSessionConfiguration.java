package cn.aradin.spring.session.config;

import java.time.Duration;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.EmbeddedValueResolverAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportAware;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.session.data.redis.RedisSessionRepository;
import org.springframework.util.StringUtils;
import org.springframework.util.StringValueResolver;

import cn.aradin.spring.session.config.annotation.EnableAradinHttpSession;

@Configuration(proxyBeanMethods = false)
public class AradinHttpSessionConfiguration extends AbstractAradinHttpSessionConfiguration<RedisSessionRepository>
		implements EmbeddedValueResolverAware, ImportAware {
	
	private final static Logger log = LoggerFactory.getLogger(AradinHttpSessionConfiguration.class);
	
	private StringValueResolver embeddedValueResolver;
	
	@Bean
	@Override
	public RedisSessionRepository sessionRepository() {
		RedisTemplate<String, Object> redisTemplate = createRedisTemplate();
		RedisSessionRepository sessionRepository = new RedisSessionRepository(redisTemplate);
		sessionRepository.setDefaultMaxInactiveInterval(getMaxInactiveInterval());
		if (StringUtils.hasText(getRedisNamespace())) {
			sessionRepository.setRedisKeyNamespace(getRedisNamespace());
		}
		sessionRepository.setFlushMode(getFlushMode());
		sessionRepository.setSaveMode(getSaveMode());
		getSessionRepositoryCustomizers()
				.forEach((sessionRepositoryCustomizer) -> sessionRepositoryCustomizer.customize(sessionRepository));
		return sessionRepository;
	}

	@Override
	public void setEmbeddedValueResolver(StringValueResolver resolver) {
		this.embeddedValueResolver = resolver;
	}

	@Override
	public void setImportMetadata(AnnotationMetadata importMetadata) {
		Map<String, Object> attributeMap = importMetadata
				.getAnnotationAttributes(EnableAradinHttpSession.class.getName());
		AnnotationAttributes attributes = AnnotationAttributes.fromMap(attributeMap);
		if (attributes == null) {
			return;
		}
		setMaxInactiveInterval(Duration.ofSeconds(attributes.<Integer>getNumber("maxInactiveIntervalInSeconds")));
		String redisNamespaceValue = attributes.getString("redisNamespace");
		if (StringUtils.hasText(redisNamespaceValue)) {
			setRedisNamespace(this.embeddedValueResolver.resolveStringValue(redisNamespaceValue));
			log.warn("REDISNAMESPACE is {}", getRedisNamespace());
		}
		setFlushMode(attributes.getEnum("flushMode"));
		setSaveMode(attributes.getEnum("saveMode"));
	}
}
