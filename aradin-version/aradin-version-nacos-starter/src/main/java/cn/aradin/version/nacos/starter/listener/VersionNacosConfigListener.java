package cn.aradin.version.nacos.starter.listener;

import java.util.concurrent.Executor;

import com.alibaba.nacos.api.config.listener.Listener;

import cn.aradin.spring.core.thread.AradinThreadManager;
import cn.aradin.version.core.dispatcher.VersionDispatcher;

public class VersionNacosConfigListener implements Listener {

	private String group;
	
	private String dataId;
	
	private VersionDispatcher versionDispatcher;
	
	public VersionNacosConfigListener(VersionDispatcher versionDispatcher, String group, String dataId) {
		this.group = group;
		this.dataId = dataId;
		this.versionDispatcher = versionDispatcher;
	}
	
	@Override
	public Executor getExecutor() {
		// TODO Auto-generated method stub
		return AradinThreadManager.getShortPool();
	}

	@Override
	public void receiveConfigInfo(String configInfo) {
		// TODO Auto-generated method stub
		versionDispatcher.dispatchVersion(group, dataId, configInfo);
	}
}
