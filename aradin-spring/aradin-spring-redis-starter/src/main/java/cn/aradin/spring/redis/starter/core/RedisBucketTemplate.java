package cn.aradin.spring.redis.starter.core;

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
}
