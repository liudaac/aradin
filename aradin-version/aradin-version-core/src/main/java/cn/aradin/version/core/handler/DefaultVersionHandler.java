package cn.aradin.version.core.handler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DefaultVersionHandler implements IVersionHandler {

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
