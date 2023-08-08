package cn.aradin.spring.redis.starter.core;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.data.redis.connection.convert.Converters;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

public class BucketHashOperations<HK, HV> extends AbstractBucketOperations<String, Object> implements HashOperations<String, HK, HV> {

	@SuppressWarnings("unchecked")
	BucketHashOperations(RedisTemplate<String, ?> template, int bucket) {
		super((RedisTemplate<String, Object>)template, bucket);
	}

	@SuppressWarnings("unchecked")
	@Override
	public HV get(String key, Object hashKey) {
		// TODO Auto-generated method stub
		byte[] rawKey = rawKey(key, hashKey);
		byte[] rawHashKey = rawHashKey(hashKey);
		byte[] rawHashValue = execute(connection -> connection.hGet(rawKey, rawHashKey));

		return (HV) rawHashValue != null ? deserializeHashValue(rawHashValue) : null;
	}
	
	@Override
	public Boolean hasKey(String key, Object hashKey) {
		// TODO Auto-generated method stub
		byte[] rawKey = rawKey(key, hashKey);
		byte[] rawHashKey = rawHashKey(hashKey);
		return execute(connection -> connection.hExists(rawKey, rawHashKey));
	}
	
	@Override
	public Long increment(String key, HK hashKey, long delta) {
		// TODO Auto-generated method stub
		byte[] rawKey = rawKey(key, hashKey);
		byte[] rawHashKey = rawHashKey(hashKey);
		return execute(connection -> connection.hIncrBy(rawKey, rawHashKey, delta));
	}

	@Override
	public Double increment(String key, HK hashKey, double delta) {
		// TODO Auto-generated method stub
		byte[] rawKey = rawKey(key, hashKey);
		byte[] rawHashKey = rawHashKey(hashKey);
		return execute(connection -> connection.hIncrBy(rawKey, rawHashKey, delta));
	}
	
	@Nullable
	@Override
	public HK randomKey(String key) {
		// TODO Auto-generated method stub
		byte[] rawKey = rawRandomKey(key);
		return deserializeHashKey(execute(connection -> connection.hRandField(rawKey)));
	}
	
	@Nullable
	@Override
	public Entry<HK, HV> randomEntry(String key) {
		// TODO Auto-generated method stub
		byte[] rawKey = rawRandomKey(key);
		Entry<byte[], byte[]> rawEntry = execute(connection -> connection.hRandFieldWithValues(rawKey));
		return rawEntry == null ? null
				: Converters.entryOf(deserializeHashKey(rawEntry.getKey()), deserializeHashValue(rawEntry.getValue()));
	}
	
	@Nullable
	@Override
	public List<HK> randomKeys(String key, long count) {
		// TODO Auto-generated method stub
		byte[] rawKey = rawRandomKey(key);
		List<byte[]> rawValues = execute(connection -> connection.hRandField(rawKey, count));
		return deserializeHashKeys(rawValues);
	}
	
	@Nullable
	@Override
	public Map<HK, HV> randomEntries(String key, long count) {
		// TODO Auto-generated method stub
		Assert.isTrue(count > 0, "Count must not be negative");
		byte[] rawKey = rawRandomKey(key);
		List<Entry<byte[], byte[]>> rawEntries = execute(connection -> connection.hRandFieldWithValues(rawKey, count));

		if (rawEntries == null) {
			return null;
		}

		Map<byte[], byte[]> rawMap = new LinkedHashMap<>(rawEntries.size());
		rawEntries.forEach(entry -> rawMap.put(entry.getKey(), entry.getValue()));
		return deserializeHashMap(rawMap);
	}
	
	@Override
	public Set<HK> keys(String key) {
		// TODO Auto-generated method stub
		Set<byte[]> allRawValues = new HashSet<>();
		for(int i=0; i<bucket; i++) {
			byte[] rawKey = rawKey(key);
			Set<byte[]> rawValues = execute(connection -> connection.hKeys(rawKey));
			if (rawValues != null) {
				allRawValues.addAll(rawValues);
			}
		}
		return allRawValues != null ? deserializeHashKeys(allRawValues) : Collections.emptySet();
	}
	
	@Override
	public Long size(String key) {
		// TODO Auto-generated method stub
		Long size = 0l;
		for(int i=0; i<bucket; i++) {
			byte[] rawKey = rawKey(key, bucket);
			size += execute(connection -> connection.hLen(rawKey));
		}
		return size;
	}

	@Override
	public Long lengthOfValue(String key, HK hashKey) {
		// TODO Auto-generated method stub
		byte[] rawKey = rawKey(key, hashKey);
		byte[] rawHashKey = rawHashKey(hashKey);
		return execute(connection -> connection.hStrLen(rawKey, rawHashKey));
	}
	
	@Override
	public void putAll(String key, Map<? extends HK, ? extends HV> m) {
		if (m.isEmpty()) {
			return;
		}

		Map<Integer, Map<byte[], byte[]>> buckets = new LinkedHashMap<>(bucket);
		for (Map.Entry<? extends HK, ? extends HV> entry : m.entrySet()) {
			Integer bucket = bucket(entry.getKey());
			if (buckets.get(bucket) == null) {
				buckets.put(bucket, new LinkedHashMap<>());
			}
			buckets.get(bucket).put(rawHashKey(entry.getKey()), rawHashValue(entry.getValue()));
		}

		execute(connection -> {
			for(Entry<Integer, Map<byte[], byte[]>> bucket:buckets.entrySet()) {
				connection.hMSet(rawKey(key, bucket.getKey()), bucket.getValue());
			}
			return null;
		});
	}
	
	@Override
	public Long delete(String key, Object... hashKeys) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<HV> multiGet(String key, Collection<HK> hashKeys) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void put(String key, HK hashKey, HV value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Boolean putIfAbsent(String key, HK hashKey, HV value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<HV> values(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<HK, HV> entries(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Cursor<Entry<HK, HV>> scan(String key, ScanOptions options) {
		// TODO Auto-generated method stub
		return null;
	}
}
