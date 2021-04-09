package cn.aradin.spring.core.thread.handler;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AradinRejectedExecutionHandler implements RejectedExecutionHandler {

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
