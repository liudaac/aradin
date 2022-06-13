package cn.aradin.version.core.dispatcher;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import org.apache.commons.collections.CollectionUtils;

import cn.aradin.spring.core.thread.AradinExecutors;
import cn.aradin.version.core.handler.IVersionHandler;

public class VersionDispatcher {
	
	private List<IVersionHandler> versionHandlers = new ArrayList<IVersionHandler>();
	
	private Executor executor = AradinExecutors.newFixedThreadPool("versionevent", 4, 8, 10000, 2000l);
	
	public VersionDispatcher(List<IVersionHandler> versionHandlers) {
		this.versionHandlers = versionHandlers;
	}
	
	public void dispatchVersion(String group, String key, String value) {
		if (CollectionUtils.isNotEmpty(versionHandlers)) {
			versionHandlers.forEach(versionHandler -> {
				if (versionHandler.support(group, key) && !value.equals(versionHandler.get(group, key))) {
					CompletableFuture.runAsync(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							versionHandler.changed(group, key, value);
						}
					}, executor);
				}
			});
		}
	}
	
	
}
