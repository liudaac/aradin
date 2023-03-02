package cn.aradin.cluster.core.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import cn.aradin.spring.core.enums.RegisterType;

@ConfigurationProperties(prefix = "aradin.cluster")
public class ClusterProperties {
	private String nodeName;//Current Node Name
	private boolean register = true;
	private boolean preferIpAddress = false;
	private RegisterType registerType;
	private String zookeeperAddressId;
	private Integer maxNode = 32;//Max Node Num To Support
	public String getNodeName() {
		return nodeName;
	}
	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}
	public boolean isRegister() {
		return register;
	}
	public void setRegister(boolean register) {
		this.register = register;
	}
	public boolean isPreferIpAddress() {
		return preferIpAddress;
	}
	public void setPreferIpAddress(boolean preferIpAddress) {
		this.preferIpAddress = preferIpAddress;
	}
	public RegisterType getRegisterType() {
		return registerType;
	}
	public void setRegisterType(RegisterType registerType) {
		this.registerType = registerType;
	}
	public String getZookeeperAddressId() {
		return zookeeperAddressId;
	}
	public void setZookeeperAddressId(String zookeeperAddressId) {
		this.zookeeperAddressId = zookeeperAddressId;
	}
	public Integer getMaxNode() {
		return maxNode;
	}
	public void setMaxNode(Integer maxNode) {
		this.maxNode = maxNode;
	}
}
