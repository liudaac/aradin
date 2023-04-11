package cn.aradin.spring.actuator.starter.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "aradin.actuator.offline")
public class ActuatorOfflineProperties {
	private Long shutWait;//优雅停机等待时间
	public Long getShutWait() {
		return shutWait;
	}
	public void setShutWait(Long shutWait) {
		this.shutWait = shutWait;
	}
}
