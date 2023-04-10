package cn.aradin.spring.actuator.starter.actuate;

import java.util.Collections;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import cn.aradin.spring.actuator.starter.context.DeployContext;

@Endpoint(id = "state", enableByDefault = true)
public class StateEndpoint {

	private final static Logger log = LoggerFactory.getLogger(StateEndpoint.class);
	
	private static final Map<String, String> CONTEXT_MESSAGE = Collections
			.unmodifiableMap(Collections.singletonMap("message", "Context OK."));
	
	public StateEndpoint() {
		// TODO Auto-generated constructor stub
	}
	
	@ReadOperation
	public Map<String, String> state() {
		if (DeployContext.isStopped()||DeployContext.isStopping()) {
			log.error("Context is stopping or already stopped");
			throw new RuntimeException("Context is stopping or already stopped");
		}
		if (log.isDebugEnabled()) {
			log.debug("Context is checking");
		}
		return CONTEXT_MESSAGE;
	}
}
