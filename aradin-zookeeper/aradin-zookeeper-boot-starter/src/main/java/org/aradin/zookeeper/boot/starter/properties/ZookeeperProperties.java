package org.aradin.zookeeper.boot.starter.properties;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.google.common.collect.Lists;

import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ConfigurationProperties(prefix = "aradin.zookeeper")
public class ZookeeperProperties {
	private boolean enable = false;
	private int sessionTimeout = 5000;
	private int connectionTimeout = 5000;
	private List<ZookeeperCluster> clusters = Lists.newArrayList();
}
