package cn.aradin.spring.salarm.starter.handler;

import cn.aradin.spring.salarm.starter.enums.SalarmLevel;

public interface ISalarmHandler {
	
	/**
	 * The extension method to operate alarm contents
	 * @param level Alarm level
	 * @param type Alarm type
	 * @param content Just the details
	 */
	public void notify(SalarmLevel level, String type, String content);
	
	/**
	 * Decide wether to support the fingered level
	 * @param level Alarm level
	 * @return
	 */
	public boolean isSupported(SalarmLevel level);
}
