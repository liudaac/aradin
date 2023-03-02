package cn.aradin.spring.actuator.starter.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "aradin.actuator.online")
public class ActuatorOnlineProperties {
	private String shell;//The path of shell for RUNNING with Online

	public String getShell() {
		return shell;
	}

	public void setShell(String shell) {
		this.shell = shell;
	}
}
