package cn.aradin.spring.redis.starter.core;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;

public class RedisBucketTemplate<K, V> extends RedisTemplate<K, V> {

	
	
	public <HK, HV> HashOperations<K, HK, HV> opsForHash(int bucket) {
		return new BucketHashOperations<K, HK, HV>(this, bucket);
	}
	
	public SetOperations<K, V> opsForSet(int bucket) {
		return new BucketSetOperations<K, V>(this, bucket);
	}
}
