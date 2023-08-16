package cn.aradin.version.nacos.starter.properties;

import java.io.Serializable;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.google.common.collect.Lists;

@ConfigurationProperties(prefix = "aradin.version.nacos")
public class VersionNacosProperties implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1534472659114596923L;
	private String username;
	private String password;
	private String serverAddr;
	private String namespace;
	private String group;
	private List<String> dataIds = Lists.newArrayList();
	private boolean listen = false;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getServerAddr() {
		return serverAddr;
	}
	public void setServerAddr(String serverAddr) {
		this.serverAddr = serverAddr;
	}
	public String getNamespace() {
		return namespace;
	}
	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}
	public List<String> getDataIds() {
		return dataIds;
	}
	public void setDataIds(List<String> dataIds) {
		this.dataIds = dataIds;
	}
	public boolean isListen() {
		return listen;
	}
	public void setListen(boolean listen) {
		this.listen = listen;
	}
}
