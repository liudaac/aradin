package cn.aradin.spring.salarm.starter.notifier;

import cn.aradin.spring.salarm.starter.enums.SalarmDuty;
import cn.aradin.spring.salarm.starter.enums.SalarmLevel;

public interface ISalarm {
	
	public void salarm(SalarmLevel level, SalarmDuty duty, String typeOrFormat, String contentOrFormat, Object... params);
	
	public void trace(SalarmDuty duty, String typeOrFormat, String contentOrFormat, Object... params);
	
	public void info(SalarmDuty duty, String typeOrFormat, String contentOrFormat, Object... params);
	
	public void warn(SalarmDuty duty, String typeOrFormat, String contentOrFormat, Object... params);
	
	public void error(SalarmDuty duty, String typeOrFormat, String contentOrFormat, Object... params);
	
	public void trace(String typeOrFormat, String contentOrFormat, Object... params);
	
	public void info(String typeOrFormat, String contentOrFormat, Object... params);
	
	public void warn(String typeOrFormat, String contentOrFormat, Object... params);
	
	public void error(String typeOrFormat, String contentOrFormat, Object... params);
}
