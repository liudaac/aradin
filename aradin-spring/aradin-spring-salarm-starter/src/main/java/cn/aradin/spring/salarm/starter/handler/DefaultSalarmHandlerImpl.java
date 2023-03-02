package cn.aradin.spring.salarm.starter.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.aradin.spring.salarm.starter.enums.SalarmLevel;

public class DefaultSalarmHandlerImpl implements ISalarmHandler {

	private final static Logger log = LoggerFactory.getLogger(DefaultSalarmHandlerImpl.class);
	
	@Override
	public void notify(SalarmLevel level, String type, String content) {
		// TODO Auto-generated method stub
		log.warn("Alarm Occured {}------{}-------{}", level, type, content);
	}

	@Override
	public boolean isSupported(SalarmLevel level) {
		// TODO Auto-generated method stub
		return true;
	}
}
