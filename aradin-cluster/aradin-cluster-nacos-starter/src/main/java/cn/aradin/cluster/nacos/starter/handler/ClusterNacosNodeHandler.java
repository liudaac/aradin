package cn.aradin.cluster.nacos.starter.handler;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.util.Assert;

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

public class ClusterNacosNodeHandler implements EventListener,ApplicationListener<ContextClosedEvent> {
	
	private final static Logger log = LoggerFactory.getLogger(ClusterNacosNodeHandler.class);
	
	private final String serviceName;
	private final String group;
	private final NamingService namingService;
	private final IClusterNodeManager clusterNodeManager;
	private final ClusterProperties clusterProperties;
	private Instance instance;
	
	public ClusterNacosNodeHandler(ClusterNacosProperties clusterNacosProperties, 
			ClusterProperties clusterProperties,
			Integer port,
			String serviceName,
			IClusterNodeManager clusterNodeManager) {
		Assert.notNull(serviceName, "ServiceName cannot be null");
		Assert.notNull(clusterNacosProperties.getGroup(), "aradin.cluster.nacos.group cannot be null");
		Assert.notNull(clusterNacosProperties.getServerAddr(), "aradin.cluster.nacos.server-addr cannot be null");
		Assert.notNull(clusterNacosProperties.getUsername(), "aradin.cluster.nacos.username cannot be null");
		Assert.notNull(clusterNacosProperties.getPassword(), "aradin.cluster.nacos.password cannot be null");
		Assert.notNull(clusterNacosProperties.getNamespace(), "aradin.cluster.nacos.namespace cannot be null");
		if (StringUtils.isNotBlank(clusterNacosProperties.getServiceName())) {
			this.serviceName = clusterNacosProperties.getServiceName();
		}else {
			this.serviceName = serviceName;
		}
		this.group = clusterNacosProperties.getGroup();
		this.clusterNodeManager = clusterNodeManager;
		this.clusterProperties = clusterProperties;
		Properties properties = new Properties();
		properties.put(PropertyKeyConst.SERVER_ADDR, clusterNacosProperties.getServerAddr());
		properties.put(PropertyKeyConst.USERNAME, clusterNacosProperties.getUsername());
		properties.put(PropertyKeyConst.PASSWORD, clusterNacosProperties.getPassword());
		properties.put(PropertyKeyConst.NAMESPACE, clusterNacosProperties.getNamespace());
		try {
			namingService = NacosFactory.createNamingService(properties);
			namingService.subscribe(serviceName, group, this);
			if (clusterProperties.isRegister()) {
				register(group, serviceName, clusterProperties.getNodeName(), port, clusterProperties.getMaxNode(), -1);
			}
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
				boolean flag = false;
				for(Instance instance:instances) {
					Integer index = Integer.parseInt(instance.getClusterName());
					if (clusterNodeManager.currentIndex() != index) {
						nodes.putIfAbsent(index, instance.getIp());
					}else if (!instance.getInstanceId().equals(clusterNodeManager.currentNode())) {
						if (!flag) {
							log.warn("Found repeat node with current {}, {}. Your cluster will do reRegister immediately.", clusterNodeManager.currentNode(), instance.getInstanceId());
							try {
								namingService.deregisterInstance(serviceName, group, this.instance);
							} catch (NacosException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							try {
								Thread.sleep(1000l);
								register(group, serviceName, this.instance.getInstanceId(), this.instance.getPort(), clusterProperties.getMaxNode(), index);
							} catch (NacosException | InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							return;
						}
					}else {
						nodes.put(clusterNodeManager.currentIndex(), clusterNodeManager.currentNode());
						flag = true;
					}
				}
				
				clusterNodeManager.nodeInit(nodes);
			}
		}
	}
	
	private void register(String group, String serviceName, String ip, Integer port, Integer maxNode, int ignoreIndex) throws NacosException, InterruptedException {
		for(int i=0; i<maxNode; i++) {
			if (i == ignoreIndex) {
				continue;
			}
			List<String> cluster = Arrays.asList(String.valueOf(i));
			List<Instance> instances = namingService.getAllInstances(serviceName, group, cluster, false);
			if (CollectionUtils.isEmpty(instances)) {
				instance = new Instance();
				instance.setInstanceId(ip);
				instance.setIp(ip);
				instance.setPort(port);
				instance.setEnabled(true);
				instance.setHealthy(true);
				instance.setClusterName(String.valueOf(i));
				clusterNodeManager.setCurrentIndex(i);
				clusterNodeManager.nodeAdded(i, ip);
				namingService.registerInstance(serviceName, group, instance);
				return;
			}
		}
		throw new RuntimeException("OutOfNodeNum " + maxNode);
	}

	@Override
	public void onApplicationEvent(ContextClosedEvent event) {
		// TODO Auto-generated method stub
		try {
			log.warn("Cluster is deregistering.");
			namingService.deregisterInstance(serviceName, group, instance);
		} catch (NacosException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
