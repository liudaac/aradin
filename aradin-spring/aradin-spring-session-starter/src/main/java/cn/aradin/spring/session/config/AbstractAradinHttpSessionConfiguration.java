package cn.aradin.spring.session.config;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.data.redis.LettuceClientConfigurationBuilderCustomizer;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.session.FlushMode;
import org.springframework.session.MapSession;
import org.springframework.session.SaveMode;
import org.springframework.session.Session;
import org.springframework.session.SessionRepository;
import org.springframework.session.config.SessionRepositoryCustomizer;
import org.springframework.session.config.annotation.web.http.SpringHttpSessionConfiguration;
import org.springframework.session.data.redis.RedisSessionRepository;
import org.springframework.session.data.redis.config.annotation.SpringSessionRedisConnectionFactory;
import org.springframework.util.Assert;

import cn.aradin.spring.session.config.connection.AradinLettuceConnectionConfiguration;
import cn.aradin.spring.session.config.properties.AradinSessionProperties;
import io.lettuce.core.resource.ClientResources;

/**
 * Base configuration class for Redis based {@link SessionRepository} implementations.
 *
 * @param <T> the {@link SessionRepository} type
 * @author LD
 * @since 1.1.0
 * @see AradinHttpSessionConfiguration
 * @see AradinIndexedHttpSessionConfiguration
 * @see SpringSessionRedisConnectionFactory
 */
@Configuration(proxyBeanMethods = false)
@Import(SpringHttpSessionConfiguration.class)
public abstract class AbstractAradinHttpSessionConfiguration<T extends SessionRepository<? extends Session>> implements BeanClassLoaderAware {
	
	private final static Logger log = LoggerFactory.getLogger(AbstractAradinHttpSessionConfiguration.class);
	
	private Duration maxInactiveInterval = MapSession.DEFAULT_MAX_INACTIVE_INTERVAL;

	private String redisNamespace = RedisSessionRepository.DEFAULT_KEY_NAMESPACE;

	private FlushMode flushMode = FlushMode.ON_SAVE;

	private SaveMode saveMode = SaveMode.ON_SET_ATTRIBUTE;

	private RedisConnectionFactory redisConnectionFactory;

	private RedisSerializer<Object> defaultRedisSerializer;

	private List<SessionRepositoryCustomizer<T>> sessionRepositoryCustomizers;

	private ClassLoader classLoader;

	public abstract T sessionRepository();

	public void setMaxInactiveInterval(Duration maxInactiveInterval) {
		this.maxInactiveInterval = maxInactiveInterval;
	}

	@Deprecated
	public void setMaxInactiveIntervalInSeconds(int maxInactiveIntervalInSeconds) {
		setMaxInactiveInterval(Duration.ofSeconds(maxInactiveIntervalInSeconds));
	}

	protected Duration getMaxInactiveInterval() {
		return this.maxInactiveInterval;
	}

	public void setRedisNamespace(String namespace) {
		Assert.hasText(namespace, "namespace must not be empty");
		this.redisNamespace = namespace;
	}

	protected String getRedisNamespace() {
		return this.redisNamespace;
	}

	public void setFlushMode(FlushMode flushMode) {
		Assert.notNull(flushMode, "flushMode must not be null");
		this.flushMode = flushMode;
	}

	protected FlushMode getFlushMode() {
		return this.flushMode;
	}

	public void setSaveMode(SaveMode saveMode) {
		Assert.notNull(saveMode, "saveMode must not be null");
		this.saveMode = saveMode;
	}

	protected SaveMode getSaveMode() {
		return this.saveMode;
	}

	@Autowired
	public void setRedisConnectionFactory(
			AradinSessionProperties sessionProperties,
			ObjectProvider<LettuceClientConfigurationBuilderCustomizer> builderCustomizers,
			ClientResources clientResources,
			ObjectProvider<RedisConnectionFactory> redisConnectionFactory) {
		log.info("Session redis connectionFactory is initialing");
		RedisConnectionFactory redisConnectionFactoryToUse = null;
		if (sessionProperties.getRedis() != null) {
			log.info("Session-customzied redis connectionFactory initialed");
			AradinLettuceConnectionConfiguration lettuce = new AradinLettuceConnectionConfiguration(sessionProperties.getRedis());
			redisConnectionFactoryToUse = lettuce.redisConnectionFactory(builderCustomizers, clientResources);
		}
		if (redisConnectionFactoryToUse == null) {
			log.info("Session-base redis connectionFactory initialed");
			redisConnectionFactoryToUse = redisConnectionFactory.getObject();
		}
		this.redisConnectionFactory = redisConnectionFactoryToUse;
	}

	protected RedisConnectionFactory getRedisConnectionFactory() {
		return this.redisConnectionFactory;
	}

	@Autowired(required = false)
	@Qualifier("springSessionDefaultRedisSerializer")
	public void setDefaultRedisSerializer(RedisSerializer<Object> defaultRedisSerializer) {
		this.defaultRedisSerializer = defaultRedisSerializer;
	}

	protected RedisSerializer<Object> getDefaultRedisSerializer() {
		return this.defaultRedisSerializer;
	}

	@Autowired(required = false)
	public void setSessionRepositoryCustomizer(
			ObjectProvider<SessionRepositoryCustomizer<T>> sessionRepositoryCustomizers) {
		this.sessionRepositoryCustomizers = sessionRepositoryCustomizers.orderedStream().collect(Collectors.toList());
	}

	protected List<SessionRepositoryCustomizer<T>> getSessionRepositoryCustomizers() {
		return this.sessionRepositoryCustomizers;
	}

	@Override
	public void setBeanClassLoader(ClassLoader classLoader) {
		this.classLoader = classLoader;
	}

	protected RedisTemplate<String, Object> createRedisTemplate() {
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
		redisTemplate.setKeySerializer(RedisSerializer.string());
		redisTemplate.setHashKeySerializer(RedisSerializer.string());
		if (getDefaultRedisSerializer() != null) {
			redisTemplate.setDefaultSerializer(getDefaultRedisSerializer());
		}
		redisTemplate.setConnectionFactory(getRedisConnectionFactory());
		redisTemplate.setBeanClassLoader(this.classLoader);
		redisTemplate.afterPropertiesSet();
		return redisTemplate;
	}
}
