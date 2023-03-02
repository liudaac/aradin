package cn.aradin.spring.caffeine.cache.listener;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.benmanes.caffeine.cache.RemovalCause;
import com.github.benmanes.caffeine.cache.RemovalListener;

public class CaffeinesonRemovalListener implements RemovalListener<Object, Object> {

	private final static Logger log = LoggerFactory.getLogger(CaffeinesonRemovalListener.class);
	
	@Override
	public void onRemoval(@Nullable Object key, @Nullable Object value, @NonNull RemovalCause cause) {
		// TODO Auto-generated method stub
		if (log.isInfoEnabled()) {
			log.info("Caffeine key has been removed {} , {} , {} ", key, value, cause.name());
		}
	}
}
