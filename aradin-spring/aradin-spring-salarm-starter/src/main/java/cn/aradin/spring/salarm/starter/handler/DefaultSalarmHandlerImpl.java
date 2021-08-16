package cn.aradin.spring.salarm.starter.handler;

import cn.aradin.spring.salarm.starter.enums.SalarmLevel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DefaultSalarmHandlerImpl implements ISalarmHandler {

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
