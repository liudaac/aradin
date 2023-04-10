package cn.aradin.spring.actuator.starter.context;

import cn.aradin.spring.actuator.starter.enums.DeployState;
import static cn.aradin.spring.actuator.starter.enums.DeployState.*;

public class DeployContext {

	private static volatile DeployState state = PENDING;
	
	public static void setStarting() {
		state = STARTING;
	}
	
	public static void setStarted() {
		state = STARTED;
	}
	
	public static void setStopping() {
		state = STOPPING;
	}
	
	public static void setStopped() {
		state = STOPPED;
	}
	
	public static boolean isPending() {
		return state == PENDING;
	}
	
	public static boolean isStarted() {
		return state == STARTED;
	}
	
	public static boolean isStarting() {
		return state == STARTING;
	}
	
	public static boolean isStopping() {
		return state == STOPPING;
	}
	
	public static boolean isStopped() {
		return state == STOPPED;
	}
}
