package cn.aradin.cluster.nacos.starter.handler;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;

import cn.aradin.cluster.core.properties.ClusterProperties;
import cn.aradin.cluster.nacos.starter.properties.ClusterNacosProperties;

public class ClusterNacosNodeHandler {
	
	private final static Logger log = LoggerFactory.getLogger(ClusterNacosNodeHandler.class);
	
	private final NamingService namingService;
	
	public ClusterNacosNodeHandler(ClusterNacosProperties clusterNacosProperties, 
			ClusterProperties clusterProperties) {
		Properties properties = new Properties();
		properties.put(PropertyKeyConst.SERVER_ADDR, clusterNacosProperties.getServerAddr());
		properties.put(PropertyKeyConst.USERNAME, clusterNacosProperties.getUsername());
		properties.put(PropertyKeyConst.PASSWORD, clusterNacosProperties.getPassword());
		properties.put(PropertyKeyConst.NAMESPACE, clusterNacosProperties.getNamespace());
		try {
			namingService = NacosFactory.createNamingService(properties);
			if (clusterProperties.isRegister()) {
				Instance instance = new Instance();
				instance.setIp(clusterProperties.getNodeName());
				instance.setPort(0);
			}
		} catch (NacosException e) {
			// TODO Auto-generated catch block
			log.error("Cluster of nacos started failed, Please check your configs.");
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}
}
