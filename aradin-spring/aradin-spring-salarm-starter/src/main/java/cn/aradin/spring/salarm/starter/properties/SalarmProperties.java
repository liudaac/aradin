package cn.aradin.spring.salarm.starter.properties;

import java.time.Duration;

import org.springframework.boot.context.properties.ConfigurationProperties;

import cn.aradin.spring.salarm.starter.enums.SalarmLevel;

@ConfigurationProperties(prefix = "aradin.salarm")
public class SalarmProperties {
	private Duration ttl = Duration.ofSeconds(3600L);// Min interval for sending a same alarm
	private SalarmLevel level = SalarmLevel.warn;// Min level to send
	public Duration getTtl() {
		return ttl;
	}
	public void setTtl(Duration ttl) {
		this.ttl = ttl;
	}
	public SalarmLevel getLevel() {
		return level;
	}
	public void setLevel(SalarmLevel level) {
		this.level = level;
	}
}
