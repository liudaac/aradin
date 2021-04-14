package cn.aradin.version.core.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import cn.aradin.version.core.enums.SyncType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ConfigurationProperties(prefix = "aradin.version")
public class VersionProperties {
	private SyncType syncType = SyncType.zookeeper;
	private String zookeeperAddressId = "versions";
}
