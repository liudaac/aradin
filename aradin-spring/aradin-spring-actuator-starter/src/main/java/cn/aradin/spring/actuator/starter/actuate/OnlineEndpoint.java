package cn.aradin.spring.actuator.starter.actuate;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.registry.client.metadata.ServiceInstanceMetadataUtils;
import org.apache.dubbo.rpc.model.ApplicationModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;

import cn.aradin.spring.actuator.starter.context.DeployContext;
import cn.aradin.spring.actuator.starter.extension.IOnlineHandler;
import cn.aradin.spring.actuator.starter.properties.ActuatorOnlineProperties;

@Endpoint(id = "online", enableByDefault = true)
public class OnlineEndpoint {
	
	private static final Map<String, String> ONLINE_MESSAGE = Collections
			.unmodifiableMap(Collections.singletonMap("message", "Online Success"));
	private final static Logger log = LoggerFactory.getLogger(OnlineEndpoint.class);
	
	private final ActuatorOnlineProperties actuatorOnlineProperties;
	
	private final List<IOnlineHandler> onlineHandlers;
	
	public OnlineEndpoint(ActuatorOnlineProperties actuatorOnlineProperties, List<IOnlineHandler> onlineHandlers) {
		// TODO Auto-generated constructor stub
		this.actuatorOnlineProperties = actuatorOnlineProperties;
		this.onlineHandlers = onlineHandlers;
	}
	
	@ReadOperation
	public Map<String, String> online() {
		if (DeployContext.isPending()) {
			DeployContext.setStarting();
			if (StringUtils.isNotBlank(actuatorOnlineProperties.getShell())) {
				String bashCommand = "sh "+actuatorOnlineProperties.getShell();
		    	Runtime runtime = Runtime.getRuntime();
				try {
					Process process = runtime.exec(bashCommand);
					int status = process.waitFor();
					if (status != 0) {
						if (log.isWarnEnabled()) {
							log.warn("Online-sh Error {}", status);
						}
					}else {
						if (log.isDebugEnabled()) {
							log.debug("Online-sh start OK");
						}
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
			try {
				if (CollectionUtils.isNotEmpty(onlineHandlers)) {
					onlineHandlers.forEach(handler->{
						handler.online();
					});
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
		if (DeployContext.isStopping()) {
			throw new RuntimeException("Application is stopping, cannot do online-process");
		}
		if (DeployContext.isStopped()) {
			try {
				DeployContext.setStarting();
				Class.forName("org.apache.dubbo.registry.client.metadata.ServiceInstanceMetadataUtils");
				ApplicationModel applicationModel = ApplicationModel.defaultModel();
				ServiceInstanceMetadataUtils.registerMetadataAndInstance(applicationModel);
			} catch (ClassNotFoundException e) {
				// TODO: handle exception
				log.info("Dubbo class is not exist, no need to register");
			}
		}
		DeployContext.setStarted();
        return ONLINE_MESSAGE;
	}
}
