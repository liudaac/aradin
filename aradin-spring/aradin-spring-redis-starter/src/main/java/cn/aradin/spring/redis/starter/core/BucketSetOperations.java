package cn.aradin.spring.redis.starter.core;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.SetOperations;

public class BucketSetOperations<K, V> extends AbstractBucketOperations<K, V> implements SetOperations<K, V>{

	BucketSetOperations(RedisTemplate<K, V> template, int bucket) {
		super(template);
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings("unchecked")
	@Override
	public Long add(K key, V... values) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long remove(K key, Object... values) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public V pop(K key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<V> pop(K key, long count) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean move(K key, V value, K destKey) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long size(K key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean isMember(K key, Object o) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<Object, Boolean> isMember(K key, Object... objects) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<V> intersect(K key, K otherKey) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<V> intersect(K key, Collection<K> otherKeys) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<V> intersect(Collection<K> keys) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long intersectAndStore(K key, K otherKey, K destKey) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long intersectAndStore(K key, Collection<K> otherKeys, K destKey) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long intersectAndStore(Collection<K> keys, K destKey) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<V> union(K key, K otherKey) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<V> union(K key, Collection<K> otherKeys) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<V> union(Collection<K> keys) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long unionAndStore(K key, K otherKey, K destKey) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long unionAndStore(K key, Collection<K> otherKeys, K destKey) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long unionAndStore(Collection<K> keys, K destKey) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<V> difference(K key, K otherKey) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<V> difference(K key, Collection<K> otherKeys) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<V> difference(Collection<K> keys) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long differenceAndStore(K key, K otherKey, K destKey) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long differenceAndStore(K key, Collection<K> otherKeys, K destKey) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long differenceAndStore(Collection<K> keys, K destKey) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<V> members(K key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public V randomMember(K key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<V> distinctRandomMembers(K key, long count) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<V> randomMembers(K key, long count) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Cursor<V> scan(K key, ScanOptions options) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RedisOperations<K, V> getOperations() {
		// TODO Auto-generated method stub
		return null;
	}

}
