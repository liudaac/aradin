package cn.aradin.spring.redis.starter.core;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.SetOperations;

import com.google.common.collect.Lists;

import cn.aradin.spring.redis.starter.core.annotation.NotSuggest;

public class BucketSetOperations<K, V> extends AbstractBucketOperations<K, V> implements SetOperations<K, V>{

	BucketSetOperations(RedisTemplate<K, V> template, int bucket) {
		super(template, bucket);
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings("unchecked")
	@Override
	public Long add(K key, V... values) {
		// TODO Auto-generated method stub
		Long count = 0l;
		if (values.length == 1) {
			byte[] rawKey = rawKey(key, values[0]);
			byte[] rawValue = rawValue(values[0]);
			count += execute(connection -> connection.sAdd(rawKey, rawValue));
		}else {
			Map<Integer, Collection<V>> bucketValues = new HashMap<>();
			for(V value:values) {
				if (value != null) {
					Integer bucket = bucket(value);
					if (bucketValues.get(bucket) == null) {
						bucketValues.put(bucket, Lists.newArrayList());
					}
					bucketValues.get(bucket).add(value);
				}
			}
			for(Entry<Integer, Collection<V>> entry:bucketValues.entrySet()) {
				byte[] rawKey = rawKey(key, entry.getKey());
				byte[][] rawValues = rawValues(entry.getValue().toArray());
				count += execute(connection -> connection.sAdd(rawKey, rawValues));
			}
		}
		return count;
	}

	@Override
	public Set<V> difference(K key, K otherKey) {
		// TODO Auto-generated method stub
		return difference(Arrays.asList(key, otherKey));
	}

	@Override
	public Set<V> difference(K key, Collection<K> otherKeys) {
		// TODO Auto-generated method stub
		Set<byte[]> rawValues = new HashSet<>();
		for(int i=0; i<bucket; i++) {
			byte[][] rawKeys = rawKeys(key, otherKeys, bucket);
			Set<byte[]> rawValue = execute(connection -> connection.sDiff(rawKeys));
			if (CollectionUtils.isNotEmpty(rawValue)) {
				rawValues.addAll(rawValue);
			}
		}
		return deserializeValues(rawValues);
	}

	@Override
	public Set<V> difference(Collection<K> keys) {
		// TODO Auto-generated method stub
		Set<byte[]> rawValues = new HashSet<>();
		for(int i=0; i<bucket; i++) {
			byte[][] rawKeys = rawKeys(keys, bucket);
			Set<byte[]> rawValue = execute(connection -> connection.sDiff(rawKeys));
			if (CollectionUtils.isNotEmpty(rawValue)) {
				rawValues.addAll(rawValue);
			}
		}
		return deserializeValues(rawValues);
	}

	@Override
	public Long differenceAndStore(K key, K otherKey, K destKey) {
		// TODO Auto-generated method stub
		return differenceAndStore(Arrays.asList(key, otherKey), destKey);
	}

	@Override
	public Long differenceAndStore(K key, Collection<K> otherKeys, K destKey) {
		// TODO Auto-generated method stub
		Long count = 0l;
		for(int i=0; i<bucket; i++) {
			byte[][] rawKeys = rawKeys(key, otherKeys, bucket);
			byte[] rawDestKey = rawKey(destKey, bucket);
			count += execute(connection -> connection.sDiffStore(rawDestKey, rawKeys));
		}
		return count;
	}

	@Override
	public Long differenceAndStore(Collection<K> keys, K destKey) {
		// TODO Auto-generated method stub
		Long count = 0l;
		for(int i=0; i<bucket; i++) {
			byte[][] rawKeys = rawKeys(keys, bucket);
			byte[] rawDestKey = rawKey(destKey, bucket);
			count += execute(connection -> connection.sDiffStore(rawDestKey, rawKeys));
		}
		return count;
	}

	@Override
	public Set<V> intersect(K key, K otherKey) {
		// TODO Auto-generated method stub
		return intersect(Arrays.asList(key, otherKey));
	}

	@Override
	public Set<V> intersect(K key, Collection<K> otherKeys) {
		// TODO Auto-generated method stub
		Set<byte[]> rawValues = new HashSet<>();
		for(int i=0; i<bucket; i++) {
			byte[][] rawKeys = rawKeys(key, otherKeys, bucket);
			Set<byte[]> rawValue = execute(connection -> connection.sInter(rawKeys));
			if (CollectionUtils.isNotEmpty(rawValue)) {
				rawValues.addAll(rawValue);
			}
		}
		return deserializeValues(rawValues);
	}

	@Override
	public Set<V> intersect(Collection<K> keys) {
		// TODO Auto-generated method stub
		Set<byte[]> rawValues = new HashSet<>();
		for(int i=0; i<bucket; i++) {
			byte[][] rawKeys = rawKeys(keys, bucket);
			Set<byte[]> rawValue = execute(connection -> connection.sInter(rawKeys));
			if (CollectionUtils.isNotEmpty(rawValue)) {
				rawValues.addAll(rawValue);
			}
		}
		return deserializeValues(rawValues);
	}

	@Override
	public Long intersectAndStore(K key, K otherKey, K destKey) {
		// TODO Auto-generated method stub
		return intersectAndStore(Arrays.asList(key, otherKey), destKey);
	}

	@Override
	public Long intersectAndStore(K key, Collection<K> otherKeys, K destKey) {
		// TODO Auto-generated method stub
		Long count = 0l;
		for(int i=0; i<bucket; i++) {
			byte[][] rawKeys = rawKeys(key, otherKeys, bucket);
			byte[] rawDestKey = rawKey(destKey, bucket);
			count += execute(connection -> connection.sInterStore(rawDestKey, rawKeys));
		}
		return count;
	}

	@Override
	public Long intersectAndStore(Collection<K> keys, K destKey) {
		// TODO Auto-generated method stub
		Long count = 0l;
		for(int i=0; i<bucket; i++) {
			byte[][] rawKeys = rawKeys(keys, bucket);
			byte[] rawDestKey = rawKey(destKey, bucket);
			count += execute(connection -> connection.sInterStore(rawDestKey, rawKeys));
		}
		return count;
	}
	
	@Override
	public Boolean isMember(K key, Object o) {
		// TODO Auto-generated method stub
		byte[] rawKey = rawKey(key, o);
		byte[] rawValue = rawValue(o);

		return execute(connection -> connection.sIsMember(rawKey, rawValue));
	}

	@Override
	@NotSuggest
	public Map<Object, Boolean> isMember(K key, Object... objects) {
		// TODO Auto-generated method stub
		return execute(connection -> {
			Map<Object, Boolean> isMember = new LinkedHashMap<>(objects.length);
			for(Object object:objects) {
				byte[] rawKey = rawKey(key, object);
				Boolean result = connection.sIsMember(rawKey, rawValue(object));
				if (result == null || !result) {
					isMember.put(object, false);
				}else {
					isMember.put(object, true);
				}
			}
			return isMember;
		});
	}
	
	@Override
	public Set<V> members(K key) {
		// TODO Auto-generated method stub
		Set<byte[]> rawValues = new HashSet<>();
		for(int i=0; i<bucket; i++) {
			byte[] rawKey = rawKey(key, bucket);
			Set<byte[]> rawValue = execute(connection -> connection.sMembers(rawKey));
			if (CollectionUtils.isNotEmpty(rawValue)) {
				rawValues.addAll(rawValue);
			}
		}
		return deserializeValues(rawValues);
	}
	
	@Override
	public Boolean move(K key, V value, K destKey) {
		// TODO Auto-generated method stub
		byte[] rawKey = rawKey(key, value);
		byte[] rawDestKey = rawKey(destKey, value);
		byte[] rawValue = rawValue(value);

		return execute(connection -> connection.sMove(rawKey, rawDestKey, rawValue));
	}
	
	@Override
	public V randomMember(K key) {
		// TODO Auto-generated method stub
		return execute(new ValueDeserializingRedisCallback(key) {
			@Override
			protected byte[] inRedis(byte[] rawKey, RedisConnection connection) {
				return connection.sRandMember(rawKey);
			}
		});
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
	public Long size(K key) {
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
