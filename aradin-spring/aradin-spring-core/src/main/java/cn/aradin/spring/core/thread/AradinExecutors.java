package cn.aradin.spring.core.thread;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class AradinExecutors {

	public static Executor newFixedThreadPool(String group, int corePoolSize, int maximumPoolSize, int queueSize, long keepAliveTime) {
		Executor executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.MILLISECONDS,
				new LinkedBlockingDeque<Runnable>(queueSize), new AradinThreadFactory(group), null);
		return executor;
	}
}
