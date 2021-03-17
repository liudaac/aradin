package cn.aradin.lucene.solr.starter.support;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@ConfigurationProperties(prefix = "spring.data.solr")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AradinSolrProperties {
	private List<String> zkHosts;
	private String chroot;
}
