package cn.aradin.spring.redis.starter.serializer;

import java.util.Collection;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import io.fury.Fury;
import io.fury.ThreadLocalFury;
import io.fury.ThreadSafeFury;
import io.fury.config.CompatibleMode;
import io.fury.config.Language;

/**
 * {@link https://github.com/apache/incubator-fury}
 * 首先Fury是线程不安全的，因为共享了单个缓存池，生产环境下还是首选线程安全模式的ThreadSafeFury 
 * 但根据Issue https://github.com/apache/incubator-fury/issues/1335
 * 当前默认的实现类为ThreadLocalFury，但在线程创建频繁的条件下，
 * Fury实例的创建可能会成为瓶颈，从而造成额外的延时问题
 * 但是ThreadPoolFury 目前存在悲观锁问题，性能无法保证
 * 综上，Fury的序列化当前只能作为体验功能
 * 
 * 目前的测试用例cn.aradin.spring.redis.starter.test.FuryBenchTest
 * 对基础结构序列化效率有提升，自定义对象首次序列化操作效率较低，需要预热
 * @author daliu
 */
public class FuryRedisSerializer implements RedisSerializer<Object> {

	private final ThreadSafeFury fury;
	
	public FuryRedisSerializer(Collection<Class<?>> clazzs) {
		fury = new ThreadLocalFury(classloader -> {
			Fury f = Fury.builder()
					.withLanguage(Language.JAVA)
					.withRefTracking(false)
					.withCompatibleMode(CompatibleMode.SCHEMA_CONSISTENT)
					.withAsyncCompilation(true)
					.requireClassRegistration(CollectionUtils.isNotEmpty(clazzs))
					.withClassLoader(classloader)
					.build();
			if (clazzs != null) {
				clazzs.forEach(clazz -> f.register(clazz));
			}
			return f;
		});
	}
	
	@Override
	public byte[] serialize(Object value) throws SerializationException {
		// TODO Auto-generated method stub
		byte[] bytes = fury.serialize(value);
		return bytes;
	}

	@Override
	public Object deserialize(byte[] bytes) throws SerializationException {
		// TODO Auto-generated method stub
		return fury.deserialize(bytes);
	}
}
