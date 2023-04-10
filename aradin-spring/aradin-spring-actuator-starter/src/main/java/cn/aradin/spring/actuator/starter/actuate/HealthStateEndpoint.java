package cn.aradin.spring.actuator.starter.actuate;

import java.time.Duration;

import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.health.HealthComponent;
import org.springframework.boot.actuate.health.HealthContributorRegistry;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.boot.actuate.health.HealthEndpointGroups;

import cn.aradin.spring.actuator.starter.context.DeployContext;

@Endpoint(id = "health", enableByDefault = true)
public class HealthStateEndpoint extends HealthEndpoint {

	public HealthStateEndpoint(HealthContributorRegistry registry, HealthEndpointGroups groups,
			Duration slowIndicatorLoggingThreshold) {
		super(registry, groups, slowIndicatorLoggingThreshold);
		// TODO Auto-generated constructor stub
	}
	
	@ReadOperation
	public HealthComponent health() {
		if (DeployContext.isStopped()||DeployContext.isStopping()) {
			throw new RuntimeException("Context is stopping or already stopped");
		}
		HealthComponent health = super.health();
		return health;
	}
}
