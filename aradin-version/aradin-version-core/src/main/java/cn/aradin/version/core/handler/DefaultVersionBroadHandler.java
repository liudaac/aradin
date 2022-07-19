package cn.aradin.version.core.handler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DefaultVersionBroadHandler implements IVersionBroadHandler {

	@Override
	public void broadcast(String group, String key) {
		// TODO Auto-generated method stub
		log.warn("Version changed {},{} and you may need a customized IVersionBroadHandler", group, key);
	}
}
