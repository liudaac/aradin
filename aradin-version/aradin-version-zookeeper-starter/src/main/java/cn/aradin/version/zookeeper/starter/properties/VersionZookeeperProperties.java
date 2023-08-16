package cn.aradin.version.zookeeper.starter.properties;

import java.io.Serializable;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "aradin.version.zookeeper")
public class VersionZookeeperProperties implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2115211896602769800L;
	private String addressId;
	public String getAddressId() {
		return addressId;
	}
	public void setAddressId(String addressId) {
		this.addressId = addressId;
	}
}
