package cn.aradin.spring.core.enums;

public enum CommonValue {
	
	COMMON_VALUE_YES(1,"yes"),
	COMMON_VALUE_NO(0,"no");
	
	private Integer value;
	private String alias;
	
	private CommonValue(Integer value, String alias) {
		// TODO Auto-generated constructor stub
		this.value = value;
		this.alias = alias;
	}
	
	public Integer value() {
		return this.value;
	}
	
	public String alias() {
		return this.alias;
	}
}
