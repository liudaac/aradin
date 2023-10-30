package cn.aradin.spring.session.config.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;
import org.springframework.session.FlushMode;
import org.springframework.session.MapSession;
import org.springframework.session.SaveMode;
import org.springframework.session.Session;
import org.springframework.session.SessionRepository;
import org.springframework.session.data.redis.RedisIndexedSessionRepository;

import cn.aradin.spring.session.config.AradinIndexedHttpSessionConfiguration;
import cn.aradin.spring.session.config.properties.AradinSessionProperties;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@EnableConfigurationProperties(AradinSessionProperties.class)
@Import(AradinIndexedHttpSessionConfiguration.class)
public @interface EnableAradinIndexedHttpSession {
	
	/**
	 * The session timeout in seconds. By default, it is set to 1800 seconds (30 minutes).
	 * This should be a non-negative integer.
	 * @return the seconds a session can be inactive before expiring
	 */
	int maxInactiveIntervalInSeconds() default MapSession.DEFAULT_MAX_INACTIVE_INTERVAL_SECONDS;

	/**
	 * Defines a unique namespace for keys. The value is used to isolate sessions by
	 * changing the prefix from default {@code spring:session:} to
	 * {@code <redisNamespace>:}.
	 * <p>
	 * For example, if you had an application named "Application A" that needed to keep
	 * the sessions isolated from "Application B" you could set two different values for
	 * the applications and they could function within the same Redis instance.
	 * @return the unique namespace for keys
	 */
	String redisNamespace() default RedisIndexedSessionRepository.DEFAULT_NAMESPACE;

	/**
	 * Flush mode for the Redis sessions. The default is {@code ON_SAVE} which only
	 * updates the backing Redis when {@link SessionRepository#save(Session)} is invoked.
	 * In a web environment this happens just before the HTTP response is committed.
	 * <p>
	 * Setting the value to {@code IMMEDIATE} will ensure that the any updates to the
	 * Session are immediately written to the Redis instance.
	 * @return the {@link FlushMode} to use
	 */
	FlushMode flushMode() default FlushMode.ON_SAVE;

	/**
	 * Save mode for the session. The default is {@link SaveMode#ON_SET_ATTRIBUTE}, which
	 * only saves changes made to session.
	 * @return the save mode
	 */
	SaveMode saveMode() default SaveMode.ON_SET_ATTRIBUTE;

	/**
	 * The cron expression for expired session cleanup job. By default runs every minute.
	 * @return the session cleanup cron expression
	 */
	String cleanupCron() default RedisIndexedSessionRepository.DEFAULT_CLEANUP_CRON;
}
