package cn.aradin.spring.redis.starter.core;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;

public class BucketHashOperations<K, HK, HV> extends AbstractBucketOperations<K, Object> implements HashOperations<K, HK, HV> {

	@SuppressWarnings("unchecked")
	BucketHashOperations(RedisTemplate<K, ?> template, int bucket) {
		super((RedisTemplate<K, Object>)template);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Long delete(K key, Object... hashKeys) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean hasKey(K key, Object hashKey) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HV get(K key, Object hashKey) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<HV> multiGet(K key, Collection<HK> hashKeys) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long increment(K key, HK hashKey, long delta) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Double increment(K key, HK hashKey, double delta) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HK randomKey(K key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Entry<HK, HV> randomEntry(K key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<HK> randomKeys(K key, long count) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<HK, HV> randomEntries(K key, long count) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<HK> keys(K key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long lengthOfValue(K key, HK hashKey) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long size(K key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void putAll(K key, Map<? extends HK, ? extends HV> m) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void put(K key, HK hashKey, HV value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Boolean putIfAbsent(K key, HK hashKey, HV value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<HV> values(K key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<HK, HV> entries(K key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Cursor<Entry<HK, HV>> scan(K key, ScanOptions options) {
		// TODO Auto-generated method stub
		return null;
	}
}
