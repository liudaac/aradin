package cn.aradin.cluster.nacos.starter.handler;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.listener.Event;
import com.alibaba.nacos.api.naming.listener.EventListener;
import com.alibaba.nacos.api.naming.listener.NamingEvent;
import com.alibaba.nacos.api.naming.pojo.Instance;

import cn.aradin.cluster.core.manager.IClusterNodeManager;
import cn.aradin.cluster.core.properties.ClusterProperties;
import cn.aradin.cluster.nacos.starter.properties.ClusterNacosProperties;

public class ClusterNacosNodeHandler implements EventListener {
	
	private final static Logger log = LoggerFactory.getLogger(ClusterNacosNodeHandler.class);
	
	private final NamingService namingService;
	private final IClusterNodeManager clusterNodeManager;
	
	public ClusterNacosNodeHandler(ClusterNacosProperties clusterNacosProperties, 
			ClusterProperties clusterProperties,
			Integer port,
			String serviceName,
			IClusterNodeManager clusterNodeManager) {
		this.clusterNodeManager = clusterNodeManager;
		Properties properties = new Properties();
		properties.put(PropertyKeyConst.SERVER_ADDR, clusterNacosProperties.getServerAddr());
		properties.put(PropertyKeyConst.USERNAME, clusterNacosProperties.getUsername());
		properties.put(PropertyKeyConst.PASSWORD, clusterNacosProperties.getPassword());
		properties.put(PropertyKeyConst.NAMESPACE, clusterNacosProperties.getNamespace());
		try {
			namingService = NacosFactory.createNamingService(properties);
			if (clusterProperties.isRegister()) {
				register(clusterNacosProperties.getGroup(), serviceName, clusterProperties.getNodeName(), port, clusterProperties.getMaxNode());
			}
			namingService.subscribe(serviceName, clusterNacosProperties.getGroup(), this);
		} catch (NacosException e) {
			// TODO Auto-generated catch block
			log.error("Cluster of nacos started failed, Please check your configs.");
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e.getMessage());
		}
	}

	@Override
	public void onEvent(Event event) {
		// TODO Auto-generated method stub
		if (event instanceof NamingEvent) {
			NamingEvent namingEvent = (NamingEvent)event;
			List<Instance> instances = namingEvent.getInstances();
			if (CollectionUtils.isNotEmpty(instances)) {
				Map<Integer, String> nodes = new HashMap<Integer, String>();
				for(Instance instance:instances) {
					Integer index = Integer.parseInt(instance.getClusterName());
					if (clusterNodeManager.currentIndex() != index) {
						nodes.putIfAbsent(index, instance.getIp());
					}
				}
				clusterNodeManager.nodeInit(nodes);
			}
		}
	}
	
	private void register(String group, String serviceName, String ip, Integer port, Integer maxNode) throws NacosException, InterruptedException {
		for(int i=0; i<maxNode; i++) {
			List<String> cluster = Arrays.asList(String.valueOf(i));
			List<Instance> instances = namingService.getAllInstances(serviceName, cluster);
			if (CollectionUtils.isEmpty(instances)) {
				Instance instance = new Instance();
				instance.setInstanceId(ip);
				instance.setIp(ip);
				instance.setPort(port);
				instance.setEnabled(true);
				instance.setClusterName(String.valueOf(i));
				namingService.registerInstance(serviceName, group, instance);
				instances = namingService.getAllInstances(serviceName, cluster);
				if (instances.size() > 1) {
					//说明发生了重复注册
					instances = namingService.getAllInstances(serviceName, cluster);
					if (!instances.get(0).getInstanceId().equals(instance.getInstanceId())) {
						log.warn("Repeat with exsit-node {}", instances.get(0).getInstanceId());
						namingService.deregisterInstance(serviceName, group, instance);
						continue;
					}
				}
				//说明注册已成功
				clusterNodeManager.setCurrentIndex(i);
				return;
			}
		}
		throw new RuntimeException("OutOfNodeNum " + maxNode);
	}
}
