package cn.aradin.lucene.solr.starter;

import java.util.Optional;

import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.impl.CloudSolrClient.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.solr.repository.config.EnableSolrRepositories;

import cn.aradin.lucene.solr.starter.support.AradinSolrProperties;

/**
 * Solr Config
 *
 */
@Configuration
@EnableConfigurationProperties(AradinSolrProperties.class)
@EnableSolrRepositories(basePackages = {"cn.aradin.**.solr"})
public class AradinSolrAutoConfiguration {
	
	@Autowired
	private AradinSolrProperties aradinSolrProperties;
	
	@Bean
	public CloudSolrClient solrClient() {
		Builder clientBuilder = new Builder(aradinSolrProperties.getZkHosts(), Optional.ofNullable(aradinSolrProperties.getChroot()));
		return clientBuilder.build();
	}
}
