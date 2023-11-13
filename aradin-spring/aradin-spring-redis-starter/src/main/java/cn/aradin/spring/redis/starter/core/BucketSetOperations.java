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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.SetOperations;
import com.google.common.collect.Lists;

import cn.aradin.spring.redis.starter.core.annotation.NotSuggest;
import cn.aradin.spring.redis.starter.core.annotation.NotSupport;
import cn.aradin.spring.redis.starter.core.enums.RedisModel;

@NotSupport(RedisModel.CLUSTER)
public class BucketSetOperations<K, V> extends AbstractBucketOperations<K, V> implements SetOperations<K, V> {

	BucketSetOperations(RedisTemplate<K, V> template, int bucket) {
		super(template, bucket);
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings("unchecked")
	@Override
	public Long add(K key, V... values) {
		// TODO Auto-generated method stub
		Long counts = 0l;
		if (values.length == 1) {
			byte[] rawKey = rawKey(key, values[0]);
			byte[] rawValue = rawValue(values[0]);
			Long count = execute(connection -> connection.sAdd(rawKey, rawValue));
			if (count != null) {
				counts += count;
			}
		} else {
			Map<Integer, Collection<V>> bucketValues = new HashMap<>();
			for (V value : values) {
				if (value != null) {
					Integer bucket = bucket(value);
					if (bucketValues.get(bucket) == null) {
						bucketValues.put(bucket, Lists.newArrayList());
					}
					bucketValues.get(bucket).add(value);
				}
			}
			for (Entry<Integer, Collection<V>> entry : bucketValues.entrySet()) {
				byte[] rawKey = rawKey(key, entry.getKey().intValue());
				byte[][] rawValues = rawValues(entry.getValue().toArray());
				Long count = execute(connection -> connection.sAdd(rawKey, rawValues));
				if (count != null) {
					counts += count;
				}
			}
		}
		return counts;
	}

	@Override
	@NotSupport(RedisModel.CLUSTER)
	public Set<V> difference(K key, K otherKey) {
		// TODO Auto-generated method stub
		return difference(Arrays.asList(key, otherKey));
	}

	@Override
	@NotSupport(RedisModel.CLUSTER)
	public Set<V> difference(K key, Collection<K> otherKeys) {
		// TODO Auto-generated method stub
		Set<byte[]> rawValues = new HashSet<>();
		for (int i = 0; i < bucket; i++) {
			byte[][] rawKeys = rawKeys(key, otherKeys, i);
			Set<byte[]> rawValue = execute(connection -> connection.sDiff(rawKeys));
			if (CollectionUtils.isNotEmpty(rawValue)) {
				rawValues.addAll(rawValue);
			}
		}
		return deserializeValues(rawValues);
	}

	@Override
	@NotSupport(RedisModel.CLUSTER)
	public Set<V> difference(Collection<K> keys) {
		// TODO Auto-generated method stub
		Set<byte[]> rawValues = new HashSet<>();
		for (int i = 0; i < bucket; i++) {
			byte[][] rawKeys = rawKeys(keys, i);
			Set<byte[]> rawValue = execute(connection -> connection.sDiff(rawKeys));
			if (CollectionUtils.isNotEmpty(rawValue)) {
				rawValues.addAll(rawValue);
			}
		}
		return deserializeValues(rawValues);
	}

	@Override
	@NotSupport(RedisModel.CLUSTER)
	public Long differenceAndStore(K key, K otherKey, K destKey) {
		// TODO Auto-generated method stub
		return differenceAndStore(Arrays.asList(key, otherKey), destKey);
	}

	@Override
	@NotSupport(RedisModel.CLUSTER)
	public Long differenceAndStore(K key, Collection<K> otherKeys, K destKey) {
		// TODO Auto-generated method stub
		Long counts = 0l;
		for (int i = 0; i < bucket; i++) {
			byte[][] rawKeys = rawKeys(key, otherKeys, i);
			byte[] rawDestKey = rawKey(destKey, i);
			Long count = execute(connection -> connection.sDiffStore(rawDestKey, rawKeys));
			if (count != null) {
				counts += count;
			}
		}
		return counts;
	}

	@Override
	@NotSupport(RedisModel.CLUSTER)
	public Long differenceAndStore(Collection<K> keys, K destKey) {
		// TODO Auto-generated method stub
		Long counts = 0l;
		for (int i = 0; i < bucket; i++) {
			byte[][] rawKeys = rawKeys(keys, i);
			byte[] rawDestKey = rawKey(destKey, i);
			Long count = execute(connection -> connection.sDiffStore(rawDestKey, rawKeys));
			if (count != null) {
				counts += count;
			}
		}
		return counts;
	}

	@Override
	@NotSupport(RedisModel.CLUSTER)
	public Set<V> intersect(K key, K otherKey) {
		// TODO Auto-generated method stub
		return intersect(Arrays.asList(key, otherKey));
	}

	@Override
	@NotSupport(RedisModel.CLUSTER)
	public Set<V> intersect(K key, Collection<K> otherKeys) {
		// TODO Auto-generated method stub
		Set<byte[]> rawValues = new HashSet<>();
		for (int i = 0; i < bucket; i++) {
			byte[][] rawKeys = rawKeys(key, otherKeys, i);
			Set<byte[]> rawValue = execute(connection -> connection.sInter(rawKeys));
			if (CollectionUtils.isNotEmpty(rawValue)) {
				rawValues.addAll(rawValue);
			}
		}
		return deserializeValues(rawValues);
	}

	@Override
	@NotSupport(RedisModel.CLUSTER)
	public Set<V> intersect(Collection<K> keys) {
		// TODO Auto-generated method stub
		Set<byte[]> rawValues = new HashSet<>();
		for (int i = 0; i < bucket; i++) {
			byte[][] rawKeys = rawKeys(keys, i);
			Set<byte[]> rawValue = execute(connection -> connection.sInter(rawKeys));
			if (CollectionUtils.isNotEmpty(rawValue)) {
				rawValues.addAll(rawValue);
			}
		}
		return deserializeValues(rawValues);
	}

	@Override
	@NotSupport(RedisModel.CLUSTER)
	public Long intersectAndStore(K key, K otherKey, K destKey) {
		// TODO Auto-generated method stub
		return intersectAndStore(Arrays.asList(key, otherKey), destKey);
	}

	@Override
	@NotSupport(RedisModel.CLUSTER)
	public Long intersectAndStore(K key, Collection<K> otherKeys, K destKey) {
		// TODO Auto-generated method stub
		Long counts = 0l;
		for (int i = 0; i < bucket; i++) {
			byte[][] rawKeys = rawKeys(key, otherKeys, i);
			byte[] rawDestKey = rawKey(destKey, i);
			Long count = execute(connection -> connection.sInterStore(rawDestKey, rawKeys));
			if (count != null) {
				counts += count;
			}
		}
		return counts;
	}

	@Override
	@NotSupport(RedisModel.CLUSTER)
	public Long intersectAndStore(Collection<K> keys, K destKey) {
		// TODO Auto-generated method stub
		Long counts = 0l;
		for (int i = 0; i < bucket; i++) {
			byte[][] rawKeys = rawKeys(keys, i);
			byte[] rawDestKey = rawKey(destKey, i);
			Long count = execute(connection -> connection.sInterStore(rawDestKey, rawKeys));
			if (count != null) {
				counts += count;
			}
		}
		return counts;
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
			for (Object object : objects) {
				byte[] rawKey = rawKey(key, object);
				Boolean result = connection.sIsMember(rawKey, rawValue(object));
				if (result == null || !result) {
					isMember.put(object, false);
				} else {
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
		for (int i = 0; i < bucket; i++) {
			byte[] rawKey = rawKey(key, i);
			Set<byte[]> rawValue = execute(connection -> connection.sMembers(rawKey));
			if (CollectionUtils.isNotEmpty(rawValue)) {
				rawValues.addAll(rawValue);
			}
		}
		return deserializeValues(rawValues);
	}
	
	public Set<V> members(K key, int bucket) {
		byte[] rawKey = rawKey(key, bucket);
		Set<byte[]> rawValue = execute(connection -> connection.setCommands().sMembers(rawKey));
		return deserializeValues(rawValue);
	}

	@Override
	@NotSupport(RedisModel.CLUSTER)
	public Boolean move(K key, V value, K destKey) {
		// TODO Auto-generated method stub
		byte[] rawKey = rawKey(key, value);
		byte[] rawDestKey = rawKey(destKey, value);
		byte[] rawValue = rawValue(value);

		return execute(connection -> connection.sMove(rawKey, rawDestKey, rawValue));
	}

	@Override
	@NotSuggest
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
	@Deprecated
	public Set<V> distinctRandomMembers(K key, long count) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Bucket distinctRandomMembers is not supported");
	}

	@Override
	@Deprecated
	public List<V> randomMembers(K key, long count) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Bucket randomMembers is not supported");
	}

	@Override
	public Long remove(K key, Object... values) {
		// TODO Auto-generated method stub
		Long counts = 0l;
		for (Object value : values) {
			byte[] rawKey = rawKey(key, value);
			byte[][] rawValues = rawValues(values);
			Long count = execute(connection -> connection.sRem(rawKey, rawValues));
			if (count != null) {
				counts += count;
			}
		}
		return counts;
	}

	@Override
	public V pop(K key) {
		// TODO Auto-generated method stub
		return execute(new ValueDeserializingRedisCallback(key) {
			@Override
			protected byte[] inRedis(byte[] rawKey, RedisConnection connection) {
				return connection.sPop(rawKey);
			}
		});
	}

	@Override
	@NotSuggest
	public List<V> pop(K key, long count) {
		// TODO Auto-generated method stub
		List<V> values = null;
		while (count-- > 0) {
			V value = pop(key);
			if (value == null) {
				break;
			} else {
				if (values == null) {
					values = Lists.newArrayList();
				}
				values.add(value);
			}
		}
		return values;
	}

	@Override
	public Long size(K key) {
		// TODO Auto-generated method stub
		Long sizes = 0l;
		for(int i=0; i<bucket; i++) {
			byte[] rawKey = rawKey(key, i);
			Long size = execute(connection -> connection.sCard(rawKey));
			if (size != null) {
				sizes += size;
			}
		}
		return sizes;
	}

	@Override
	@NotSupport(RedisModel.CLUSTER)
	public Set<V> union(K key, K otherKey) {
		// TODO Auto-generated method stub
		return union(Arrays.asList(key, otherKey));
	}

	@Override
	@NotSupport(RedisModel.CLUSTER)
	public Set<V> union(K key, Collection<K> otherKeys) {
		// TODO Auto-generated method stub
		Set<byte[]> rawValues = new HashSet<>();
		for(int i=0; i<bucket; i++) {
			byte[][] rawKeys = rawKeys(key, otherKeys, i);
			Set<byte[]> rawValue = execute(connection -> connection.sUnion(rawKeys));
			if (CollectionUtils.isNotEmpty(rawValue)) {
				rawValues.addAll(rawValue);
			}
		}
		return deserializeValues(rawValues);
	}

	@Override
	@NotSupport(RedisModel.CLUSTER)
	public Set<V> union(Collection<K> keys) {
		// TODO Auto-generated method stub
		Set<byte[]> rawValues = new HashSet<>();
		for(int i=0; i<bucket; i++) {
			byte[][] rawKeys = rawKeys(keys, i);
			Set<byte[]> rawValue = execute(connection -> connection.sUnion(rawKeys));
			if (CollectionUtils.isNotEmpty(rawValue)) {
				rawValues.addAll(rawValue);
			}
		}
		return deserializeValues(rawValues);
	}

	@Override
	@NotSupport(RedisModel.CLUSTER)
	public Long unionAndStore(K key, K otherKey, K destKey) {
		// TODO Auto-generated method stub
		return unionAndStore(Arrays.asList(key, otherKey), destKey);
	}

	@Override
	@NotSupport(RedisModel.CLUSTER)
	public Long unionAndStore(K key, Collection<K> otherKeys, K destKey) {
		// TODO Auto-generated method stub
		Long counts = 0l;
		for(int i=0; i<bucket; i++) {
			byte[][] rawKeys = rawKeys(key, otherKeys, i);
			byte[] rawDestKey = rawKey(destKey, i);
			Long count = execute(connection -> connection.sUnionStore(rawDestKey, rawKeys));
			if (count != null) {
				counts += count;
			}
		}
		return counts;
	}

	@Override
	@NotSupport(RedisModel.CLUSTER)
	public Long unionAndStore(Collection<K> keys, K destKey) {
		// TODO Auto-generated method stub
		Long counts = 0l;
		for(int i=0; i<bucket; i++) {
			byte[][] rawKeys = rawKeys(keys, i);
			byte[] rawDestKey = rawKey(destKey, i);
			Long count = execute(connection -> connection.sUnionStore(rawDestKey, rawKeys));
			if (count != null) {
				counts += count;
			}
		}
		return counts;
	}

	@Override
	public Cursor<V> scan(K key, ScanOptions options) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Bucket scan is not supported");
	}
}
