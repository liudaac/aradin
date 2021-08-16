package cn.aradin.spring.salarm.starter.properties;

import java.time.Duration;

import org.springframework.boot.context.properties.ConfigurationProperties;

import cn.aradin.spring.salarm.starter.enums.SalarmLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@ConfigurationProperties(prefix = "aradin.salarm")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SalarmProperties {
	private Duration ttl = Duration.ofSeconds(3600L);// Min interval for sending a same alarm
	private SalarmLevel level = SalarmLevel.warn;// Min level to send
}
