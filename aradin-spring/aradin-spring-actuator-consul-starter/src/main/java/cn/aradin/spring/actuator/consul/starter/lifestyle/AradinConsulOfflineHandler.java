package cn.aradin.spring.actuator.consul.starter.lifestyle;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.cloud.consul.serviceregistry.ConsulRegistration;
import org.springframework.context.ApplicationContext;
import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.agent.model.Member;
import com.ecwid.consul.v1.agent.model.Service;

import cn.aradin.spring.actuator.starter.extension.IOfflineHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AradinConsulOfflineHandler implements IOfflineHandler {

	private final ConsulClient consulClient;
	
	private final ConsulRegistration consulRegistration;
	
	public AradinConsulOfflineHandler(ConsulClient consulClient, ConsulRegistration consulRegistration) {
		// TODO Auto-generated constructor stub
		this.consulClient = consulClient;
		this.consulRegistration = consulRegistration;
	}
	
	@Override
	public void offline(ApplicationContext context) {
		// TODO Auto-generated method stub
		String currentInstanceId = consulRegistration.getInstanceId();
        List<Member> members = consulClient.getAgentMembers().getValue();
        for (Member member : members) {
            String address = member.getAddress();
            ConsulClient clearClient = new ConsulClient(address);
            try {
                Map<String, Service> serviceMap = clearClient.getAgentServices().getValue();
                for (Entry<String, Service> entry : serviceMap.entrySet()) {
                    Service service = entry.getValue();
                    String instanceId = service.getId();
                    if (currentInstanceId.equals(instanceId)) {
                        log.warn("在{}客户端上的服务 :{}，准备清理...................", address, currentInstanceId);
                        clearClient.agentServiceDeregister(currentInstanceId);
                    }
                }
            } catch (Exception e) {
                log.error("异常信息: {}", e);
            }
        }

	}
}
