package cn.aradin.spring.actuator.starter.actuate;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;

import cn.aradin.spring.actuator.starter.properties.ActuatorOnlineProperties;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Endpoint(id = "online", enableByDefault = true)
public class OnlineEndpoint {
	
	private static final Map<String, String> ONLINE_MESSAGE = Collections
			.unmodifiableMap(Collections.singletonMap("message", "Online Success"));
	
	private final ActuatorOnlineProperties actuatorOnlineProperties;
	
	public OnlineEndpoint(ActuatorOnlineProperties actuatorOnlineProperties) {
		// TODO Auto-generated constructor stub
		this.actuatorOnlineProperties = actuatorOnlineProperties;
	}
	
	@ReadOperation
	public Map<String, String> online() {
		if (StringUtils.isNotBlank(actuatorOnlineProperties.getShell())) {
			String bashCommand = "sh "+actuatorOnlineProperties.getShell();
	    	Runtime runtime = Runtime.getRuntime();
			try {
				Process process = runtime.exec(bashCommand);
				int status = process.waitFor();
				if (status != 0) {
					if (log.isWarnEnabled()) {
						log.warn("Filebeat Start Error {}", status);
					}
				}else {
					if (log.isDebugEnabled()) {
						log.debug("Filebeat start OK");
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
        return ONLINE_MESSAGE;
	}
}
