package cn.aradin.lucene.solr.starter.support;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.data.solr")
public class AradinSolrProperties {
	private List<String> zkHosts;
	private String chroot;
	public List<String> getZkHosts() {
		return zkHosts;
	}
	public void setZkHosts(List<String> zkHosts) {
		this.zkHosts = zkHosts;
	}
	public String getChroot() {
		return chroot;
	}
	public void setChroot(String chroot) {
		this.chroot = chroot;
	}
}
