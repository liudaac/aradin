package cn.aradin.spring.actuator.starter.actuate;

import java.time.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.health.HealthComponent;
import org.springframework.boot.actuate.health.HealthContributorRegistry;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.boot.actuate.health.HealthEndpointGroups;

import cn.aradin.spring.actuator.starter.context.DeployContext;

@Endpoint(id = "health", enableByDefault = true)
public class StateEndpoint extends HealthEndpoint {

	private final static Logger log = LoggerFactory.getLogger(StateEndpoint.class);
	
	public StateEndpoint(HealthContributorRegistry registry, HealthEndpointGroups groups,
			Duration slowIndicatorLoggingThreshold) {
		super(registry, groups, slowIndicatorLoggingThreshold);
		// TODO Auto-generated constructor stub
	}
	
	@ReadOperation
	public HealthComponent health() {
		if (DeployContext.isStopped()||DeployContext.isStopping()) {
			log.error("Context is stopping or already stopped");
			throw new RuntimeException("Context is stopping or already stopped");
		}
		if (log.isDebugEnabled()) {
			log.debug("Context is checking");
		}
		HealthComponent health = super.health();
		return health;
	}
}
