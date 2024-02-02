package cn.aradin.spring.redis.starter.serializer;

import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import cn.aradin.spring.core.net.http.code.AradinCodedEnum;
import cn.aradin.spring.core.net.http.error.HttpError;
import io.fury.Fury;
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
 * 目前的测试用例看，对基础结构序列化效率有提升，自定义对象效率较低，达不到官方benchmark的效果
 * @author daliu
 */
public class FuryRedisSerializer implements RedisSerializer<Object> {

	private ThreadSafeFury fury = Fury.builder()
			  .withLanguage(Language.JAVA)
			  .withRefTracking(false)
			  .withCompatibleMode(CompatibleMode.SCHEMA_CONSISTENT)
			  .withAsyncCompilation(true)
			  .requireClassRegistration(false)
			  .buildThreadSafeFury();
	
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
	
	@SuppressWarnings("rawtypes")
	public static void main(String[] args) {
		String test = "wqweqweqewqeqweqweqeqweqweqweqwe";
		FuryRedisSerializer serializer = new FuryRedisSerializer();
		long start1 = System.currentTimeMillis();
		byte[] datas = serializer.serialize(test);
		String out1 = (String)serializer.deserialize(datas);
		long end1 = System.currentTimeMillis();
		System.out.println(out1 + "    "+(end1 - start1));
		
		JdkSerializationRedisSerializer jdkSerializer = new JdkSerializationRedisSerializer();
		long start2 = System.currentTimeMillis();
		byte[] datas2 = jdkSerializer.serialize(test);
		String out2 = (String)jdkSerializer.deserialize(datas2);
		long end2 = System.currentTimeMillis();
		System.out.println(out2 + "    "+(end2 - start2));
		
		HttpError httpError = HttpError.instance(AradinCodedEnum.OK, test);
		start1 = System.currentTimeMillis();
		datas = serializer.serialize(httpError);
		HttpError httpError1 = (HttpError)serializer.deserialize(datas);
		end1 = System.currentTimeMillis();
		
		System.out.println(httpError1.getCoded() + "    "+(end1 - start1));
		
		start2 = System.currentTimeMillis();
		datas2 = jdkSerializer.serialize(httpError);
		HttpError httpError2 = (HttpError)jdkSerializer.deserialize(datas2);
		end2 = System.currentTimeMillis();
		System.out.println(httpError2.getCoded() + "    "+(end2 - start2));
		
		int times = 10;
		long total1 = 0l;
		long total2 = 0l;
		for(int i=0; i<times; i++) {
			httpError = HttpError.instance(AradinCodedEnum.OK, String.valueOf(System.currentTimeMillis()));
			start1 = System.nanoTime();
			datas = serializer.serialize(httpError);
			serializer.deserialize(datas);
			end1 = System.nanoTime();
			total1 += end1 - start1;
			
			start2 = System.nanoTime();
			datas2 = jdkSerializer.serialize(httpError);
			jdkSerializer.deserialize(datas2);
			end2 = System.nanoTime();
			total2 += end2 - start2;
		}
		System.out.println("AVG1 "+(double)total1/(1000000*times));
		System.out.println("AVG2 "+(double)total2/(1000000*times));
	}
}
