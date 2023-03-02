package cn.aradin.spring.core.thread.handler;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AradinRejectedExecutionHandler implements RejectedExecutionHandler {

	private final static Logger log = LoggerFactory.getLogger(AradinRejectedExecutionHandler.class);
	
	private final String group;
	
	public AradinRejectedExecutionHandler(String group) {
		// TODO Auto-generated constructor stub
		this.group = group;
	}
	
	@Override
	public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
		// TODO Auto-generated method stub
		log.error("ThreadPool Overflow {}", group);
	}
}
