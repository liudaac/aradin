package cn.aradin.cluster.zookeeper.starter.properties;

import java.io.Serializable;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "aradin.cluster.zookeeper")
public class ClusterZookeeperProperties implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3081673197626748586L;
	private String addressId;
	public String getAddressId() {
		return addressId;
	}
	public void setAddressId(String addressId) {
		this.addressId = addressId;
	}
}
