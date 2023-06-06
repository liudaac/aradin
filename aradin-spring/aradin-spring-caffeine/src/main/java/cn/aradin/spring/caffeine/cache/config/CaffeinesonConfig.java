package cn.aradin.spring.caffeine.cache.config;

public class CaffeinesonConfig {
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
	/**是否开启统计功能 */
	private boolean recordStats = false;
	
	public CaffeinesonConfig(long expireAfterAccess,
			long expireAfterWrite,
			long refreshAfterWrite,
			int initialCapacity,
			long maximumSize,
			long maximumWeight,
			boolean allowNullValues,
			boolean isSoft,
			boolean recordStats) {
		this.expireAfterAccess = expireAfterAccess;
		this.expireAfterWrite = expireAfterWrite;
		this.refreshAfterWrite = refreshAfterWrite;
		this.initialCapacity = initialCapacity;
		this.maximumSize = maximumSize;
		this.maximumWeight = maximumWeight;
		this.allowNullValues = allowNullValues;
		this.isSoft = isSoft;
		this.recordStats = recordStats;
	}
	
	public CaffeinesonConfig() {
		
	}
	
	public long getExpireAfterAccess() {
		return expireAfterAccess;
	}
	public void setExpireAfterAccess(long expireAfterAccess) {
		this.expireAfterAccess = expireAfterAccess;
	}
	public long getExpireAfterWrite() {
		return expireAfterWrite;
	}
	public void setExpireAfterWrite(long expireAfterWrite) {
		this.expireAfterWrite = expireAfterWrite;
	}
	public long getRefreshAfterWrite() {
		return refreshAfterWrite;
	}
	public void setRefreshAfterWrite(long refreshAfterWrite) {
		this.refreshAfterWrite = refreshAfterWrite;
	}
	public int getInitialCapacity() {
		return initialCapacity;
	}
	public void setInitialCapacity(int initialCapacity) {
		this.initialCapacity = initialCapacity;
	}
	public long getMaximumSize() {
		return maximumSize;
	}
	public void setMaximumSize(long maximumSize) {
		this.maximumSize = maximumSize;
	}
	public long getMaximumWeight() {
		return maximumWeight;
	}
	public void setMaximumWeight(long maximumWeight) {
		this.maximumWeight = maximumWeight;
	}
	public boolean isAllowNullValues() {
		return allowNullValues;
	}
	public void setAllowNullValues(boolean allowNullValues) {
		this.allowNullValues = allowNullValues;
	}
	public boolean isSoft() {
		return isSoft;
	}
	public void setSoft(boolean isSoft) {
		this.isSoft = isSoft;
	}
	public boolean isRecordStats() {
		return recordStats;
	}
	public void setRecordStats(boolean recordStats) {
		this.recordStats = recordStats;
	}
}
