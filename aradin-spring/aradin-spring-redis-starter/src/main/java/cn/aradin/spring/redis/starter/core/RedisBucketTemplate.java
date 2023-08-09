package cn.aradin.spring.redis.starter.core;

import java.util.Collection;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;

public class RedisBucketTemplate<V> extends RedisTemplate<String, V> {
	
	public <HK, HV> HashOperations<String, HK, HV> opsForHash(int bucket) {
		return new BucketHashOperations<HK, HV>(this, bucket);
	}
	
	public SetOperations<String, V> opsForSet(int bucket) {
		return new BucketSetOperations<String, V>(this, bucket);
	}
	
	@Override
	public Boolean expire(String key, final long timeout, final TimeUnit unit) {
		throw new UnsupportedOperationException("Bucket expire at key-level is not supported. You need operate it at bucket-level from operations-class.");
	}

	@Override
	public Boolean expireAt(String key, final Date date) {
		throw new UnsupportedOperationException("Bucket expireAt is not supported. You need operate it at bucket-level from operations-class.");
	}
	
	@Override
	public Boolean delete(String key) {
		throw new UnsupportedOperationException("Bucket delete at key-level is not supported. You need operate it at bucket-level from operations-class.");
	}
	
	@Override
	public Long delete(Collection<String> keys) {
		throw new UnsupportedOperationException("Bucket delete at key-level is not supported. You need operate it at bucket-level from operations-class.");
	}
}
