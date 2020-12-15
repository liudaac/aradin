package cn.aradin.spring.actuator.starter.extension;

import org.springframework.context.ApplicationContext;

import lombok.extern.slf4j.Slf4j;

/**
 * The Default Implement Class For Offline Handler
 * @author daliu
 *
 */
@Slf4j
public class DefaultOfflineHandler implements IOfflineHandler{

	@Override
	public void offline(ApplicationContext context) {
		// TODO Auto-generated method stub
		if (log.isDebugEnabled()) {
			log.debug("DefaultOfflineHandler is Running");
		}
	}
}
