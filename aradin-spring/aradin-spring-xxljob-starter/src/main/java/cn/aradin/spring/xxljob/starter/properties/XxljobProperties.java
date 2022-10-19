package cn.aradin.spring.xxljob.starter.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "xxl.job")
public class XxljobProperties {
	private XxljobAdmin admin;
	private String accessToken;
	private XxljobExecutor executor;
	public XxljobAdmin getAdmin() {
		return admin;
	}
	public void setAdmin(XxljobAdmin admin) {
		this.admin = admin;
	}
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public XxljobExecutor getExecutor() {
		return executor;
	}
	public void setExecutor(XxljobExecutor executor) {
		this.executor = executor;
	}
}
