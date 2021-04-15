package org.aradin.spring.caffeine.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CaffeineConfig {
	/** 访问后过期时间，单位毫秒*/
	private long expireAfterAccess = 120000l;
	/** 写入后过期时间，单位毫秒*/
	private long expireAfterWrite = 120000l;
	/** 写入后刷新时间，单位毫秒 暂不支持*/
	private long refreshAfterWrite;
	/** 初始化大小*/
	private int initialCapacity = 1000;
	/** 最大缓存对象个数，超过此数量时之前放入的缓存将失效*/
	private long maximumSize = 1000000l;
	/** 由于权重需要缓存对象来提供，对于使用spring cache这种场景不是很适合，所以暂不支持配置*/
	private long maximumWeight;
	/**是否允许空值*/
	private boolean allowNullValues = true;
	/**是否启用软引用*/
	private boolean isSoft = true;
}
