package cn.aradin.spring.redis.starter.core;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.redis.core.RedisTemplate;

import cn.aradin.spring.core.annotation.UnSafe;
import cn.aradin.spring.redis.starter.core.annotation.NotSupport;
import cn.aradin.spring.redis.starter.core.enums.RedisModel;

@UnSafe("Multi-keys operation in local memory")
@NotSupport({RedisModel.SINGLE, RedisModel.MASTER_SLAVE})
public class ClusterBucketSetOperations<K, V> extends BucketSetOperations<K, V> {

	ClusterBucketSetOperations(RedisTemplate<K, V> template, int bucket) {
		super(template, bucket);
		// TODO Auto-generated constructor stub
	}

	private Collection<byte[]> difference(byte[][] rawKeys) {
		return execute(connection -> {
			Set<byte[]> result = new HashSet<>();
			if (rawKeys.length > 1) {
				Set<byte[]> base = connection.setCommands().sMembers(rawKeys[0]);
				for(int j=1; j<rawKeys.length; j++) {
					Set<byte[]> set = connection.setCommands().sMembers(rawKeys[j]);
					if (CollectionUtils.isNotEmpty(set)) {
						result.addAll(CollectionUtils.subtract(set, base));
					}
				}
			}
			return result;
		});
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
		for (int i = 0; i < bucket; i++) {
			byte[][] rawKeys = rawKeys(key, otherKeys, i);
			Collection<byte[]> rawValue = difference(rawKeys);
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
		for (int i = 0; i < bucket; i++) {
			byte[][] rawKeys = rawKeys(keys, i);
			Collection<byte[]> rawValue = difference(rawKeys);
			if (CollectionUtils.isNotEmpty(rawValue)) {
				rawValues.addAll(rawValue);
			}
		}
		return deserializeValues(rawValues);
	}

	private Long differenceAndStore(byte[][] rawKeys, byte[] rawDestKey) {
		return execute(connection -> {
			Set<byte[]> result = new HashSet<>();
			if (rawKeys.length > 1) {
				Set<byte[]> base = connection.setCommands().sMembers(rawKeys[0]);
				for(int j=1; j<rawKeys.length; j++) {
					Set<byte[]> set = connection.setCommands().sMembers(rawKeys[j]);
					if (CollectionUtils.isNotEmpty(set)) {
						result.addAll(CollectionUtils.subtract(set, base));
					}
				}
				if (CollectionUtils.isNotEmpty(result)) {
					byte[][] rawValues = new byte[result.size()][];
					int j = 0;
					for(byte[] rawValue:result) {
						rawValues[j++] = rawValue;
					}
					connection.setCommands().sAdd(rawDestKey, rawValues);
				}
			}
			return (long)result.size();
		});
	}
	
	@Override
	public Long differenceAndStore(K key, K otherKey, K destKey) {
		// TODO Auto-generated method stub
		return differenceAndStore(Arrays.asList(key, otherKey), destKey);
	}

	@Override
	public Long differenceAndStore(K key, Collection<K> otherKeys, K destKey) {
		// TODO Auto-generated method stub
		Long counts = 0l;
		for (int i = 0; i < bucket; i++) {
			byte[] rawDestKey = rawKey(destKey, i);
			byte[][] rawKeys = rawKeys(key, otherKeys, i);
			Long count = differenceAndStore(rawKeys, rawDestKey);
			counts += count;
		}
		return counts;
	}

	@Override
	public Long differenceAndStore(Collection<K> keys, K destKey) {
		// TODO Auto-generated method stub
		Long counts = 0l;
		for (int i = 0; i < bucket; i++) {
			byte[][] rawKeys = rawKeys(keys, i);
			byte[] rawDestKey = rawKey(destKey, i);
			Long count = differenceAndStore(rawKeys, rawDestKey);
			if (count != null) {
				counts += count;
			}
		}
		return counts;
	}

	@Override
	public Set<V> intersect(K key, K otherKey) {
		// TODO Auto-generated method stub
		return intersect(Arrays.asList(key, otherKey));
	}

	private Collection<byte[]> intersect(byte[][] rawKeys) {
		return execute(connection -> {
			Collection<byte[]> base = null;
			for(int j=0; j<rawKeys.length; j++) {
				Set<byte[]> set = connection.setCommands().sMembers(rawKeys[j]);
				if (base == null) {
					base = set;
				}else {
					base = CollectionUtils.intersection(base, set);
				}
				if (CollectionUtils.isEmpty(base)) {
					break;
				}
			}
			return base;
		});
	}
	
	@Override
	public Set<V> intersect(K key, Collection<K> otherKeys) {
		// TODO Auto-generated method stub
		Set<byte[]> rawValues = new HashSet<>();
		for (int i = 0; i < bucket; i++) {
			byte[][] rawKeys = rawKeys(key, otherKeys, i);
			Collection<byte[]> rawValue = intersect(rawKeys);
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
		for (int i = 0; i < bucket; i++) {
			byte[][] rawKeys = rawKeys(keys, i);
			Collection<byte[]> rawValue = intersect(rawKeys);
			if (CollectionUtils.isNotEmpty(rawValue)) {
				rawValues.addAll(rawValue);
			}
		}
		return deserializeValues(rawValues);
	}

	private Long intersectAndStore(byte[][] rawKeys, byte[] rawDestKey) {
		return execute(connection -> {
			Long count = 0l;
			Collection<byte[]> base = null;
			for(int j=0; j<rawKeys.length; j++) {
				Set<byte[]> set = connection.setCommands().sMembers(rawKeys[j]);
				if (base == null) {
					base = set;
				}else if (set != null) {
					base = CollectionUtils.intersection(base, set);
				}
				if (CollectionUtils.isEmpty(base)) {
					break;
				}
			}
			if (CollectionUtils.isNotEmpty(base)) {
				byte[][] rawValues = new byte[base.size()][];
				int j = 0;
				for(byte[] rawValue:base) {
					rawValues[j++] = rawValue;
				}
				count += connection.setCommands().sAdd(rawDestKey, rawValues);
			}
			return count;
		});
	}
	
	@Override
	public Long intersectAndStore(K key, K otherKey, K destKey) {
		// TODO Auto-generated method stub
		return intersectAndStore(Arrays.asList(key, otherKey), destKey);
	}

	@Override
	public Long intersectAndStore(K key, Collection<K> otherKeys, K destKey) {
		// TODO Auto-generated method stub
		Long counts = 0l;
		for (int i = 0; i < bucket; i++) {
			byte[][] rawKeys = rawKeys(key, otherKeys, i);
			byte[] rawDestKey = rawKey(destKey, i);
			Long count = intersectAndStore(rawKeys, rawDestKey);
			if (count != null) {
				counts += count;
			}
		}
		return counts;
	}

	@Override
	public Long intersectAndStore(Collection<K> keys, K destKey) {
		// TODO Auto-generated method stub
		Long counts = 0l;
		for (int i = 0; i < bucket; i++) {
			byte[][] rawKeys = rawKeys(keys, i);
			byte[] rawDestKey = rawKey(destKey, i);
			Long count = intersectAndStore(rawKeys, rawDestKey);
			if (count != null) {
				counts += count;
			}
		}
		return counts;
	}

	@Override
	public Boolean move(K key, V value, K destKey) {
		// TODO Auto-generated method stub
		byte[] rawKey = rawKey(key, value);
		byte[] rawDestKey = rawKey(destKey, value);
		byte[] rawValue = rawValue(value);

		return execute(connection -> {
			connection.setCommands().sRem(rawKey, rawValue);
			connection.setCommands().sAdd(rawDestKey, rawValue);
			return true;
		});
	}

	private Collection<byte[]> union(byte[][] rawKeys) {
		return execute(connection -> {
			Collection<byte[]> base = null;
			for(int i=0; i<rawKeys.length; i++) {
				Set<byte[]> set = connection.setCommands().sMembers(rawKeys[i]);
				if (base == null) {
					base = set;
				}else if (set != null){
					base = CollectionUtils.union(base, set);
				}
			}
			return base;
		});
	}
	
	@Override
	public Set<V> union(K key, K otherKey) {
		// TODO Auto-generated method stub
		return union(Arrays.asList(key, otherKey));
	}

	@Override
	public Set<V> union(K key, Collection<K> otherKeys) {
		// TODO Auto-generated method stub
		Set<byte[]> rawValues = new HashSet<>();
		for(int i=0; i<bucket; i++) {
			byte[][] rawKeys = rawKeys(key, otherKeys, i);
			Collection<byte[]> rawValue = union(rawKeys);
			if (CollectionUtils.isNotEmpty(rawValue)) {
				rawValues.addAll(rawValue);
			}
		}
		return deserializeValues(rawValues);
	}

	@Override
	public Set<V> union(Collection<K> keys) {
		// TODO Auto-generated method stub
		Set<byte[]> rawValues = new HashSet<>();
		for(int i=0; i<bucket; i++) {
			byte[][] rawKeys = rawKeys(keys, i);
			Collection<byte[]> rawValue = union(rawKeys);
			if (CollectionUtils.isNotEmpty(rawValue)) {
				rawValues.addAll(rawValue);
			}
		}
		return deserializeValues(rawValues);
	}

	private Long unionAndStore(byte[][] rawKeys, byte[] rawDestKey) {
		return execute(connection -> {
			Long count = 0l;
			Collection<byte[]> base = null;
			for(int i=0; i<rawKeys.length; i++) {
				Set<byte[]> set = connection.setCommands().sMembers(rawKeys[i]);
				if (base == null) {
					base = set;
				}else if (set != null){
					base = CollectionUtils.union(base, set);
				}
			}
			if (CollectionUtils.isNotEmpty(base)) {
				byte[][] rawValues = new byte[base.size()][];
				int j = 0;
				for(byte[] rawValue:base) {
					rawValues[j++] = rawValue;
				}
				count += connection.setCommands().sAdd(rawDestKey, rawValues);
			}
			return count;
		});
	}
	
	@Override
	public Long unionAndStore(K key, K otherKey, K destKey) {
		// TODO Auto-generated method stub
		return unionAndStore(Arrays.asList(key, otherKey), destKey);
	}

	@Override
	public Long unionAndStore(K key, Collection<K> otherKeys, K destKey) {
		// TODO Auto-generated method stub
		Long counts = 0l;
		for(int i=0; i<bucket; i++) {
			byte[][] rawKeys = rawKeys(key, otherKeys, i);
			byte[] rawDestKey = rawKey(destKey, i);
			Long count = unionAndStore(rawKeys, rawDestKey);
			if (count != null) {
				counts += count;
			}
		}
		return counts;
	}

	@Override
	public Long unionAndStore(Collection<K> keys, K destKey) {
		// TODO Auto-generated method stub
		Long counts = 0l;
		for(int i=0; i<bucket; i++) {
			byte[][] rawKeys = rawKeys(keys, i);
			byte[] rawDestKey = rawKey(destKey, i);
			Long count = unionAndStore(rawKeys, rawDestKey);
			if (count != null) {
				counts += count;
			}
		}
		return counts;
	}
}
