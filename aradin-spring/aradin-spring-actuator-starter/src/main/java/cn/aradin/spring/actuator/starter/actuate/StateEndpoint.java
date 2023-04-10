package cn.aradin.spring.actuator.starter.actuate;

import java.time.Duration;

import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.health.HealthComponent;
import org.springframework.boot.actuate.health.HealthContributorRegistry;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.boot.actuate.health.HealthEndpointGroups;

import cn.aradin.spring.actuator.starter.context.DeployContext;

@Endpoint(id = "state", enableByDefault = true)
public class StateEndpoint extends HealthEndpoint {

	public StateEndpoint(HealthContributorRegistry registry, HealthEndpointGroups groups,
			Duration slowIndicatorLoggingThreshold) {
		super(registry, groups, slowIndicatorLoggingThreshold);
		// TODO Auto-generated constructor stub
	}
	
	@ReadOperation
	public HealthComponent state() {
		if (DeployContext.isStopped()||DeployContext.isStopping()) {
			throw new RuntimeException("Context is stopping or already stopped");
		}
		HealthComponent health = super.health();
		return health;
	}
}
