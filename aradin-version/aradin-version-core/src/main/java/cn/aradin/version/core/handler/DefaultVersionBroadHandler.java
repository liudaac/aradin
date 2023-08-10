package cn.aradin.version.core.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultVersionBroadHandler implements IVersionBroadHandler {

	private final static Logger log = LoggerFactory.getLogger(DefaultVersionBroadHandler.class);
	
	@Override
	public void broadcast(String key) {
		// TODO Auto-generated method stub
		log.warn("Version changed {},{} and you may need a customized IVersionBroadHandler", key);
	}

	@Override
	public void broadcast(String key, String version) {
		// TODO Auto-generated method stub
		log.warn("Version changed {},{},{} and you may need a customized IVersionBroadHandler", key, version);
	}
}
