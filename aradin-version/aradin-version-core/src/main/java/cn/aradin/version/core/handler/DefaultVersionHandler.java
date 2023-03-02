package cn.aradin.version.core.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultVersionHandler implements IVersionHandler {

	private final static Logger log = LoggerFactory.getLogger(DefaultVersionHandler.class);
	
	@Override
	public String get(String group, String key) {
		// TODO Auto-generated method stub
		log.info("Get Version {} , {}", group, key);
		return "0";
	}
	
	@Override
	public boolean support(String group, String key) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void version(String group, String key, String version) {
		// TODO Auto-generated method stub
		log.info("Version Broadcast {} , {}", key, version);
	}

	@Override
	public void changed(String group, String key, String version) {
		// TODO Auto-generated method stub
		log.info("Version Changed {} , {}", key, version);
	}
}
