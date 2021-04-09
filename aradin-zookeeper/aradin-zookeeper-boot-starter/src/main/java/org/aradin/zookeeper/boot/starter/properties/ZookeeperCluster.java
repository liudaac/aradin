package org.aradin.zookeeper.boot.starter.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ZookeeperCluster {
	private String id;
	private String address;
	private String callbackBean;
}
