package cn.aradin.spring.actuator.starter.extension;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

/**
 * The Default Implement Class For Offline Handler
 * @author liudaac
 *
 */
public class DefaultOfflineHandler implements IOfflineHandler{

	private final static Logger log = LoggerFactory.getLogger(DefaultOfflineHandler.class);
	
	@Override
	public void offline(ApplicationContext context) {
		// TODO Auto-generated method stub
		if (log.isDebugEnabled()) {
			log.debug("DefaultOfflineHandler is Running");
		}
	}
}
