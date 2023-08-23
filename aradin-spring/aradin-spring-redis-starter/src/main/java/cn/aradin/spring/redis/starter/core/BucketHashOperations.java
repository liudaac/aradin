package cn.aradin.spring.redis.starter.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.redis.connection.convert.Converters;
import org.springframework.data.redis.core.ConvertingCursor;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import cn.aradin.spring.redis.starter.core.annotation.NotSuggest;

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
	@NotSuggest
	public HK randomKey(String key) {
		// TODO Auto-generated method stub
		byte[] rawKey = rawRandomKey(key);
		return deserializeHashKey(execute(connection -> connection.hRandField(rawKey)));
	}
	
	@Nullable
	@Override
	@NotSuggest
	public Entry<HK, HV> randomEntry(String key) {
		// TODO Auto-generated method stub
		byte[] rawKey = rawRandomKey(key);
		Entry<byte[], byte[]> rawEntry = execute(connection -> connection.hRandFieldWithValues(rawKey));
		return rawEntry == null ? null
				: Converters.entryOf(deserializeHashKey(rawEntry.getKey()), deserializeHashValue(rawEntry.getValue()));
	}
	
	@Nullable
	@Override
	@Deprecated
	public List<HK> randomKeys(String key, long count) {
		// TODO Auto-generated method stub
		byte[] rawKey = rawRandomKey(key);
		List<byte[]> rawValues = execute(connection -> connection.hRandField(rawKey, count));
		return deserializeHashKeys(rawValues);
	}
	
	@Nullable
	@Override
	@Deprecated
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
			byte[] rawKey = rawKey(key, i);
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
		Long sizes = 0l;
		for(int i=0; i<bucket; i++) {
			byte[] rawKey = rawKey(key, i);
			Long size = execute(connection -> connection.hLen(rawKey));
			if (size != null) {
				sizes+=size;
			}
		}
		return sizes;
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
				connection.hMSet(rawKey(key, bucket.getKey().intValue()), bucket.getValue());
			}
			return null;
		});
	}
	
	@Override
	public List<HV> multiGet(String key, Collection<HK> hashKeys) {
		// TODO Auto-generated method stub
		if (hashKeys.isEmpty()) {
			return Collections.emptyList();
		}
		Map<Integer, Collection<byte[]>> bucketMap = new HashMap<>();
		for(HK hashKey:hashKeys) {
			Integer bucket = bucket(hashKey);
			if (bucketMap.get(bucket) == null) {
				bucketMap.put(bucket, new ArrayList<byte[]>());
			}
			bucketMap.get(bucket).add(rawHashKey(hashKey));
		}
		List<byte[]> rawValues = new ArrayList<byte[]>();
		for(Entry<Integer, Collection<byte[]>> entry:bucketMap.entrySet()) {
			byte[] rawKey = rawKey(key, entry.getKey().intValue());
			byte[][] rawHashKeys = new byte[entry.getValue().size()][];
			int counter = 0;
			for (byte[] rawHashKey : entry.getValue()) {
				rawHashKeys[counter++] = rawHashKey;
			}
			List<byte[]> rawValue = execute(connection -> connection.hMGet(rawKey, rawHashKeys));
			if (CollectionUtils.isNotEmpty(rawValue)) {
				rawValues.addAll(rawValue);
			}
		}
		return deserializeHashValues(rawValues);
	}
	
	@Override
	public void put(String key, HK hashKey, HV value) {
		// TODO Auto-generated method stub
		byte[] rawKey = rawKey(key, hashKey);
		byte[] rawHashKey = rawHashKey(hashKey);
		byte[] rawHashValue = rawHashValue(value);

		execute(connection -> {
			connection.hSet(rawKey, rawHashKey, rawHashValue);
			return null;
		});
	}
	
	@Override
	public Boolean putIfAbsent(String key, HK hashKey, HV value) {
		// TODO Auto-generated method stub
		byte[] rawKey = rawKey(key, hashKey);
		byte[] rawHashKey = rawHashKey(hashKey);
		byte[] rawHashValue = rawHashValue(value);

		return execute(connection -> connection.hSetNX(rawKey, rawHashKey, rawHashValue));
	}
	
	@Override
	public List<HV> values(String key) {
		// TODO Auto-generated method stub
		List<byte[]> rawValues = new ArrayList<byte[]>();
		for(int i=0; i<bucket; i++) {
			byte[] rawKey = rawKey(key, i);
			List<byte[]> rawValue = execute(connection -> connection.hVals(rawKey));
			if (CollectionUtils.isNotEmpty(rawValue)) {
				rawValues.addAll(rawValue);
			}
		}
		return rawValues != null ? deserializeHashValues(rawValues) : Collections.emptyList();
	}
	
	/**
	 * Not suggest array-operation for the ios
	 */
	@Override
	public Long delete(String key, Object... hashKeys) {
		// TODO Auto-generated method stub
		Long counts = 0l;
		for(Object hashKey:hashKeys) {
			byte[] rawKey = rawKey(key, hashKey);
			byte[] rawHashKey = rawHashKey(hashKey);
			Long count = execute(connection -> connection.hDel(rawKey, rawHashKey));
			if (count != null) {
				counts += count;
			}
		}
		return counts;
	}
	
	@Override
	public Map<HK, HV> entries(String key) {
		// TODO Auto-generated method stub
		Map<byte[], byte[]> entries = null;
		for(int i=0; i<bucket; i++) {
			byte[] rawKey = rawKey(key, i);
			Map<byte[], byte[]> entry = execute(connection -> connection.hGetAll(rawKey));
			if (entry != null) {
				if (entries == null) {
					entries = entry;
				}else {
					entries.putAll(entry);
				}
			}
		}
		return entries != null ? deserializeHashMap(entries) : Collections.emptyMap();
	}

	@Override
	public Cursor<Entry<HK, HV>> scan(String key, ScanOptions options) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Scan with no bucket is not supported. Please use scan(String key, ScanOptions options, int bucket) instead.");
	}
	
	/**
	 * Multi bucket scan need traveral each bucket to avoid using too many connections
	 * @param key
	 * @param options
	 * @param bucket from 0 to ${bucketsize}-1
	 * @return
	 */
	public Cursor<Entry<HK, HV>> scan(String key, ScanOptions options, int bucket) {
		// TODO Auto-generated method stub
		if (bucket >= this.bucket) {
			return null;
		}
		byte[] rawKey = rawKey(key, bucket);
		return template.executeWithStickyConnection(
				(RedisCallback<Cursor<Entry<HK, HV>>>) connection -> new ConvertingCursor<>(connection.hScan(rawKey, options),
						new Converter<Entry<byte[], byte[]>, Entry<HK, HV>>() {
							@Override
							public Entry<HK, HV> convert(final Entry<byte[], byte[]> source) {
								return Converters.entryOf(deserializeHashKey(source.getKey()), deserializeHashValue(source.getValue()));
							}
						}));
	}
}
