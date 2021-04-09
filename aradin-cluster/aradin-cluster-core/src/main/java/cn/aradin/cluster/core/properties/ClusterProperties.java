package cn.aradin.cluster.core.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import cn.aradin.cluster.core.enums.RegisterType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ConfigurationProperties(prefix = "aradin.cluster")
public class ClusterProperties {
	private String name;
	private boolean register = false;
	private boolean preferIpAddress = false;
	private RegisterType registerType;
}
