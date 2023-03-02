package cn.aradin.spring.actuator.starter.extension;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultOnlineHandler implements IOnlineHandler {

	private final static Logger log = LoggerFactory.getLogger(DefaultOnlineHandler.class);
	
	@Override
	public void online() {
		// TODO Auto-generated method stub
		if (log.isDebugEnabled()) {
			log.debug("DefaultOnlineHandler is Running");
		}
	}

}
