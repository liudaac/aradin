package cn.aradin.spring.caffeine.cache.listener;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import com.github.benmanes.caffeine.cache.RemovalCause;
import com.github.benmanes.caffeine.cache.RemovalListener;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CaffeinesonRemovalListener implements RemovalListener<Object, Object> {

	@Override
	public void onRemoval(@Nullable Object key, @Nullable Object value, @NonNull RemovalCause cause) {
		// TODO Auto-generated method stub
		if (log.isInfoEnabled()) {
			log.info("Caffeine key has been removed {} , {} , {} ", key, value, cause.name());
		}
	}
}
