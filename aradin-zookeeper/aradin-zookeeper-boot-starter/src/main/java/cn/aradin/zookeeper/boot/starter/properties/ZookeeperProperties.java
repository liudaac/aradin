package cn.aradin.zookeeper.boot.starter.properties;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.google.common.collect.Lists;

@ConfigurationProperties(prefix = "aradin.zookeeper")
public class ZookeeperProperties {
	private boolean enable = true;
	private int sessionTimeout = 5000;
	private int connectionTimeout = 5000;
	private List<Zookeeper> addresses = Lists.newArrayList();
	public boolean isEnable() {
		return enable;
	}
	public void setEnable(boolean enable) {
		this.enable = enable;
	}
	public int getSessionTimeout() {
		return sessionTimeout;
	}
	public void setSessionTimeout(int sessionTimeout) {
		this.sessionTimeout = sessionTimeout;
	}
	public int getConnectionTimeout() {
		return connectionTimeout;
	}
	public void setConnectionTimeout(int connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}
	public List<Zookeeper> getAddresses() {
		return addresses;
	}
	public void setAddresses(List<Zookeeper> addresses) {
		this.addresses = addresses;
	}
}
