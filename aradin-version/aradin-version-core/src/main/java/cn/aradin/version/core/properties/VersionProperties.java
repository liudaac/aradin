package cn.aradin.version.core.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "aradin.version")
public class VersionProperties {
	private VersionZookeeper zookeeper;
	private VersionNacos nacos;
	public VersionZookeeper getZookeeper() {
		return zookeeper;
	}
	public void setZookeeper(VersionZookeeper zookeeper) {
		this.zookeeper = zookeeper;
	}
	public VersionNacos getNacos() {
		return nacos;
	}
	public void setNacos(VersionNacos nacos) {
		this.nacos = nacos;
	}
}
