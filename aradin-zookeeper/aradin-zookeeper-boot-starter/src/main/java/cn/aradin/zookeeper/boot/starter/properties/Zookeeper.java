package cn.aradin.zookeeper.boot.starter.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Zookeeper {
	private String id;
	private String address;
}
