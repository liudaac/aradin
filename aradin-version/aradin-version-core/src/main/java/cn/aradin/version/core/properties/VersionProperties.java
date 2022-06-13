package cn.aradin.version.core.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ConfigurationProperties(prefix = "aradin.version")
public class VersionProperties {
	private VersionZookeeper zookeeper;
	private VersionNacos nacos;
}
