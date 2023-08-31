package cn.aradin.spring.core.thread;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import cn.aradin.spring.core.thread.handler.AradinRejectedExecutionHandler;

/**
 * Constructor for AradinExecutors
 * @author daliu
 *
 */
public class AradinExecutors {

	/**
	 * FixedThreadPool contructor
	 * @param group group
	 * @param corePoolSize corePoolSize
	 * @param maximumPoolSize maximumPoolSize
	 * @param queueSize queueSize
	 * @param keepAliveTime keepAliveTime
	 * @return Executor
	 */
	public static Executor newFixedThreadPool(String group, int corePoolSize, int maximumPoolSize, int queueSize, long keepAliveTime) {
		Executor executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.MILLISECONDS,
				new LinkedBlockingDeque<Runnable>(queueSize), new AradinThreadFactory(group), new AradinRejectedExecutionHandler(group));
		return executor;
	}
}
