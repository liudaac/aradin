package cn.aradin.spring.actuator.starter.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ConfigurationProperties(prefix = "aradin.actuator.online")
public class ActuatorOnlineProperties {
	private String shell;//The path of shell for RUNNING with Online
}
