package cn.aradin.spring.redis.starter.test;

import java.io.Serializable;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;

import com.google.common.collect.Lists;

import cn.aradin.spring.redis.starter.serializer.FuryRedisSerializer;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FuryBenchTest {
	
	@DisplayName("Fury序列化 基础对象")
	@ParameterizedTest
	@CsvSource({"wqweqweqewqeqweqweqeqweqweqweqwe"})
	@Order(1)
	public void furyWithString(String test) {
		FuryRedisSerializer serializer = new FuryRedisSerializer(null);
		long start1 = System.currentTimeMillis();
		byte[] datas = serializer.serialize(test);
		serializer.deserialize(datas);
		long end1 = System.currentTimeMillis();
		System.out.println("String Fury "+(end1 - start1));
		
		JdkSerializationRedisSerializer jdkSerializer = new JdkSerializationRedisSerializer();
		long start2 = System.currentTimeMillis();
		byte[] datas2 = jdkSerializer.serialize(test);
		jdkSerializer.deserialize(datas2);
		long end2 = System.currentTimeMillis();
		System.out.println("String Jdk "+(end2 - start2));
	}
	
	@DisplayName("Fury先注册后序列化 声明对象")
	@Test
	@Order(2)
	public void furyWithReg() {
		List<Class<?>> clazzs = Lists.newArrayList();
		clazzs.add(FuryTest1.class);
		FuryRedisSerializer serializer = new FuryRedisSerializer(clazzs);
		JdkSerializationRedisSerializer jdkSerializer = new JdkSerializationRedisSerializer();
		
		FuryTest1 furyTest1 = new FuryTest1(1, "1");
		
		long start1 = System.currentTimeMillis();
		byte[] datas = serializer.serialize(furyTest1);
		serializer.deserialize(datas);
		long end1 = System.currentTimeMillis();
		System.out.println("Object Fury "+(end1 - start1));
		
		long start2 = System.currentTimeMillis();
		byte[] datas2 = jdkSerializer.serialize(furyTest1);
		jdkSerializer.deserialize(datas2);
		long end2 = System.currentTimeMillis();
		System.out.println("Object Jdk "+(end2 - start2));
	}
	
	@DisplayName("Fury不注册直接序列化 声明对象")
	@Test
	@Order(3)
	public void furyWithoutReg() {
		List<Class<?>> clazzs = Lists.newArrayList();
		FuryRedisSerializer serializer = new FuryRedisSerializer(clazzs);
		
		FuryTest2 furyTest2 = new FuryTest2(1, "1");
		long start1 = System.currentTimeMillis();
		byte[] datas = serializer.serialize(furyTest2);
		serializer.deserialize(datas);
		long end1 = System.currentTimeMillis();
		System.out.println("Object Fury "+(end1 - start1));
		
		JdkSerializationRedisSerializer jdkSerializer = new JdkSerializationRedisSerializer();
		long start2 = System.currentTimeMillis();
		byte[] datas2 = jdkSerializer.serialize(furyTest2);
		jdkSerializer.deserialize(datas2);
		long end2 = System.currentTimeMillis();
		System.out.println("Object Jdk "+(end2 - start2));
	}
	
	@DisplayName("Fury循环序列化 声明对象")
	@Test
	@Order(4)
	public void furyTimes() {
		List<Class<?>> clazzs = Lists.newArrayList();
		clazzs.add(FuryTest1.class);
		FuryRedisSerializer serializer = new FuryRedisSerializer(clazzs);
		JdkSerializationRedisSerializer jdkSerializer = new JdkSerializationRedisSerializer();
		
		int times = 10;
		long total1 = 0l;
		long total2 = 0l;
		for(int i=0; i<times; i++) {
			FuryTest1 test = new FuryTest1(i, String.valueOf(i));
			long start1 = System.nanoTime();
			byte[] datas = serializer.serialize(test);
			serializer.deserialize(datas);
			long end1 = System.nanoTime();
			total1 += end1 - start1;
			
			long start2 = System.nanoTime();
			byte[] datas2 = jdkSerializer.serialize(test);
			jdkSerializer.deserialize(datas2);
			long end2 = System.nanoTime();
			total2 += end2 - start2;
		}
		System.out.println("AVG1 Fury "+(double)total1/(1000000*times));
		System.out.println("AVG2 Jdk "+(double)total2/(1000000*times));
	}
}

@SuppressWarnings("serial")
class FuryTest1 implements Serializable {
	Integer key;
	String value;
	public FuryTest1(Integer key, String value) {
		this.key = key;
		this.value = value;
	}
}

@SuppressWarnings("serial")
class FuryTest2 implements Serializable {
	Integer key;
	String value;
	public FuryTest2(Integer key, String value) {
		this.key = key;
		this.value = value;
	}
}
