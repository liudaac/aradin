package cn.aradin.version.core.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import cn.aradin.spring.core.enums.RegisterType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ConfigurationProperties(prefix = "aradin.version")
public class VersionProperties {
	private RegisterType registerType = RegisterType.zookeeper;
	private String zookeeperAddressId = "versions";
}
