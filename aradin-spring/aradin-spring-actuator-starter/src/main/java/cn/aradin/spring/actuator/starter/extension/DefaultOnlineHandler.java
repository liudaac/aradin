package cn.aradin.spring.actuator.starter.extension;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DefaultOnlineHandler implements IOnlineHandler {

	@Override
	public void online() {
		// TODO Auto-generated method stub
		if (log.isDebugEnabled()) {
			log.debug("DefaultOnlineHandler is Running");
		}
	}

}
