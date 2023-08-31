package cn.aradin.spring.redis.starter.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.connection.DefaultTuple;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisZSetCommands.Tuple;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.TimeoutUtils;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationUtils;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

public abstract class AbstractBucketOperations<K, V> {
	
	Random random = new Random();
	// utility methods for the template internal methods
	abstract class ValueDeserializingRedisCallback implements RedisCallback<V> {
		private Object key;
		public ValueDeserializingRedisCallback(Object key) {
			this.key = key;
		}
		public final V doInRedis(RedisConnection connection) {
			byte[] result = null;
			for(int i=0; i<bucket; i++) {
				byte[] rawKey = rawKey(key, i);
				result = inRedis(rawKey, connection);
				if (result == null) {
					continue;
				}
			}
			return deserializeValue(result);
		}

		@Nullable
		protected abstract byte[] inRedis(byte[] rawKey, RedisConnection connection);
	}

	final int bucket;
	final RedisTemplate<K, V> template;

	AbstractBucketOperations(RedisTemplate<K, V> template, int bucket) {
		this.template = template;
		this.bucket = bucket;
	}

	@SuppressWarnings("rawtypes")
	RedisSerializer keySerializer() {
		return template.getKeySerializer();
	}

	@SuppressWarnings("rawtypes")
	RedisSerializer valueSerializer() {
		return template.getValueSerializer();
	}

	@SuppressWarnings("rawtypes")
	RedisSerializer hashKeySerializer() {
		return template.getHashKeySerializer();
	}

	@SuppressWarnings("rawtypes")
	RedisSerializer hashValueSerializer() {
		return template.getHashValueSerializer();
	}

	@SuppressWarnings("rawtypes")
	RedisSerializer stringSerializer() {
		return template.getStringSerializer();
	}

	@Nullable
	<T> T execute(RedisCallback<T> callback) {
		return template.execute(callback, true);
	}

	public RedisOperations<K, V> getOperations() {
		return template;
	}

	int bucket(Object hashKey) {
		return Math.abs(hashKey.hashCode())%bucket;
	}
	
	byte[] rawRandomKey(Object key) {
		return rawKey(key+"-"+random.nextInt(bucket));
	}
	
	byte[] rawKey(Object key, int index) {
		return rawKey(key+"-"+index);
	}
	
	byte[] rawKey(Object key, Object hashKey) {
		int index = bucket(hashKey);
		return rawKey(key+"-"+index);
	}
	
	@SuppressWarnings("unchecked")
	byte[] rawKey(Object key) {

		Assert.notNull(key, "non null key required");

		if (keySerializer() == null && key instanceof byte[]) {
			return (byte[]) key;
		}

		return keySerializer().serialize(key);
	}

	@SuppressWarnings("unchecked")
	byte[] rawString(String key) {
		return stringSerializer().serialize(key);
	}

	@SuppressWarnings("unchecked")
	byte[] rawValue(Object value) {

		if (valueSerializer() == null && value instanceof byte[]) {
			return (byte[]) value;
		}

		return valueSerializer().serialize(value);
	}

	byte[][] rawValues(Object... values) {

		byte[][] rawValues = new byte[values.length][];
		int i = 0;
		for (Object value : values) {
			rawValues[i++] = rawValue(value);
		}

		return rawValues;
	}

	/**
	 * @param values must not be {@literal empty} nor contain {@literal null}
	 *               values.
	 * @return
	 * @since 1.5
	 */
	byte[][] rawValues(Collection<V> values) {

		Assert.notEmpty(values, "Values must not be 'null' or empty.");
		Assert.noNullElements(values.toArray(), "Values must not contain 'null' value.");

		byte[][] rawValues = new byte[values.size()][];
		int i = 0;
		for (V value : values) {
			rawValues[i++] = rawValue(value);
		}

		return rawValues;
	}

	@SuppressWarnings("unchecked")
	<HK> byte[] rawHashKey(HK hashKey) {
		Assert.notNull(hashKey, "non null hash key required");
		if (hashKeySerializer() == null && hashKey instanceof byte[]) {
			return (byte[]) hashKey;
		}
		return hashKeySerializer().serialize(hashKey);
	}

	@SuppressWarnings("unchecked")
	<HK> byte[][] rawHashKeys(HK... hashKeys) {

		byte[][] rawHashKeys = new byte[hashKeys.length][];
		int i = 0;
		for (HK hashKey : hashKeys) {
			rawHashKeys[i++] = rawHashKey(hashKey);
		}
		return rawHashKeys;
	}

	@SuppressWarnings("unchecked")
	<HV> byte[] rawHashValue(HV value) {

		if (hashValueSerializer() == null && value instanceof byte[]) {
			return (byte[]) value;
		}
		return hashValueSerializer().serialize(value);
	}

	byte[][] rawKeys(K key, K otherKey) {

		byte[][] rawKeys = new byte[2][];

		rawKeys[0] = rawKey(key);
		rawKeys[1] = rawKey(key);
		return rawKeys;
	}

	byte[][] rawKeys(Collection<K> keys) {
		return rawKeys(null, keys);
	}

	byte[][] rawKeys(Collection<K> keys, int bucket) {
		return rawKeys(null, keys, bucket);
	}
	
	byte[][] rawKeys(K key, Collection<K> keys, int bucket) {

		byte[][] rawKeys = new byte[keys.size() + (key != null ? 1 : 0)][];
		int i = 0;
		if (key != null) {
			rawKeys[i++] = rawKey(key, bucket);
		}
		for (K k : keys) {
			rawKeys[i++] = rawKey(k, bucket);
		}

		return rawKeys;
	}
	
	byte[][] rawKeys(K key, Collection<K> keys) {

		byte[][] rawKeys = new byte[keys.size() + (key != null ? 1 : 0)][];

		int i = 0;

		if (key != null) {
			rawKeys[i++] = rawKey(key);
		}

		for (K k : keys) {
			rawKeys[i++] = rawKey(k);
		}

		return rawKeys;
	}

	@SuppressWarnings("unchecked")
	Set<V> deserializeValues(Set<byte[]> rawValues) {
		if (valueSerializer() == null) {
			return (Set<V>) rawValues;
		}
		return SerializationUtils.deserialize(rawValues, valueSerializer());
	}

	@Nullable
	Set<TypedTuple<V>> deserializeTupleValues(@Nullable Set<Tuple> rawValues) {
		if (rawValues == null) {
			return null;
		}
		Set<TypedTuple<V>> set = new LinkedHashSet<>(rawValues.size());
		for (Tuple rawValue : rawValues) {
			set.add(deserializeTuple(rawValue));
		}
		return set;
	}

	List<TypedTuple<V>> deserializeTupleValues(List<Tuple> rawValues) {
		if (rawValues == null) {
			return null;
		}
		List<TypedTuple<V>> set = new ArrayList<>(rawValues.size());
		for (Tuple rawValue : rawValues) {
			set.add(deserializeTuple(rawValue));
		}
		return set;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Nullable
	TypedTuple<V> deserializeTuple(@Nullable Tuple tuple) {
		if (tuple == null) {
			return null;
		}
		Object value = tuple.getValue();
		if (valueSerializer() != null) {
			value = valueSerializer().deserialize(tuple.getValue());
		}
		return new DefaultTypedTuple(value, tuple.getScore());
	}

	@SuppressWarnings("unchecked")
	Set<Tuple> rawTupleValues(Set<TypedTuple<V>> values) {
		if (values == null) {
			return null;
		}
		Set<Tuple> rawTuples = new LinkedHashSet<>(values.size());
		for (TypedTuple<V> value : values) {
			byte[] rawValue;
			if (valueSerializer() == null && value.getValue() instanceof byte[]) {
				rawValue = (byte[]) value.getValue();
			} else {
				rawValue = valueSerializer().serialize(value.getValue());
			}
			rawTuples.add(new DefaultTuple(rawValue, value.getScore()));
		}
		return rawTuples;
	}

	@SuppressWarnings("unchecked")
	List<V> deserializeValues(List<byte[]> rawValues) {
		if (valueSerializer() == null) {
			return (List<V>) rawValues;
		}
		return SerializationUtils.deserialize(rawValues, valueSerializer());
	}

	@SuppressWarnings("unchecked")
	<T> Set<T> deserializeHashKeys(Set<byte[]> rawKeys) {
		if (hashKeySerializer() == null) {
			return (Set<T>) rawKeys;
		}
		return SerializationUtils.deserialize(rawKeys, hashKeySerializer());
	}

	@SuppressWarnings("unchecked")
	<T> List<T> deserializeHashKeys(List<byte[]> rawKeys) {
		if (hashKeySerializer() == null) {
			return (List<T>) rawKeys;
		}
		return SerializationUtils.deserialize(rawKeys, hashKeySerializer());
	}

	@SuppressWarnings("unchecked")
	<T> List<T> deserializeHashValues(List<byte[]> rawValues) {
		if (hashValueSerializer() == null) {
			return (List<T>) rawValues;
		}
		return SerializationUtils.deserialize(rawValues, hashValueSerializer());
	}

	@SuppressWarnings("unchecked")
	<HK, HV> Map<HK, HV> deserializeHashMap(@Nullable Map<byte[], byte[]> entries) {
		// connection in pipeline/multi mode

		if (entries == null) {
			return null;
		}

		Map<HK, HV> map = new LinkedHashMap<>(entries.size());

		for (Map.Entry<byte[], byte[]> entry : entries.entrySet()) {
			map.put((HK) deserializeHashKey(entry.getKey()), (HV) deserializeHashValue(entry.getValue()));
		}

		return map;
	}

	@SuppressWarnings("unchecked")
	K deserializeKey(byte[] value) {
		if (keySerializer() == null) {
			return (K) value;
		}
		return (K) keySerializer().deserialize(value);
	}

	/**
	 * @param keys
	 * @return
	 * @since 1.7
	 */
	Set<K> deserializeKeys(Set<byte[]> keys) {

		if (CollectionUtils.isEmpty(keys)) {
			return Collections.emptySet();
		}
		Set<K> result = new LinkedHashSet<>(keys.size());
		for (byte[] key : keys) {
			result.add(deserializeKey(key));
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	V deserializeValue(byte[] value) {
		if (valueSerializer() == null) {
			return (V) value;
		}
		return (V) valueSerializer().deserialize(value);
	}

	String deserializeString(byte[] value) {
		return (String) stringSerializer().deserialize(value);
	}

	@SuppressWarnings({ "unchecked" })
	<HK> HK deserializeHashKey(byte[] value) {
		if (hashKeySerializer() == null) {
			return (HK) value;
		}
		return (HK) hashKeySerializer().deserialize(value);
	}

	@SuppressWarnings("unchecked")
	<HV> HV deserializeHashValue(byte[] value) {
		if (hashValueSerializer() == null) {
			return (HV) value;
		}
		return (HV) hashValueSerializer().deserialize(value);
	}
	
	public Boolean expire(K key, final long timeout, final TimeUnit unit) {
		Boolean result = true;
		for(int i=0; i<bucket; i++) {
			byte[] rawKey = rawKey(key, i);
			long rawTimeout = TimeoutUtils.toMillis(timeout, unit);
			result = result&execute(connection -> {
				try {
					return connection.pExpire(rawKey, rawTimeout);
				} catch (Exception e) {
					// Driver may not support pExpire or we may be running on Redis 2.4
					return connection.expire(rawKey, TimeoutUtils.toSeconds(timeout, unit));
				}
			});
		}
		return result;
	}
	
	public Boolean expireAt(K key, final Date date) {
		Boolean result = true;
		for(int i=0; i<bucket; i++) {
			byte[] rawKey = rawKey(key, i);
			result = result&execute(connection -> {
				try {
					return connection.pExpireAt(rawKey, date.getTime());
				} catch (Exception e) {
					return connection.expireAt(rawKey, date.getTime() / 1000);
				}
			});
		}
		return result;
	}
	
	public Boolean delete(K key) {
		for(int i=0; i<bucket; i++) {
			byte[] rawKey = rawKey(key, i);
			execute(connection -> connection.del(rawKey));
		}
		return true;
	}
	
	public Long delete(Collection<K> keys) {
		if (CollectionUtils.isEmpty(keys)) {
			return 0L;
		}
		Long count = 0l;
		for(int i=0; i<bucket; i++) {
			byte[][] rawKeys = rawKeys(keys, i);
			count+=execute(connection -> connection.del(rawKeys));
		}
		return count;
	}
}
