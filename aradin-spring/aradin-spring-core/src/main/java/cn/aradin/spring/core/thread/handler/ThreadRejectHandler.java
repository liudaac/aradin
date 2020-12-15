package cn.aradin.spring.core.thread.handler;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ThreadRejectHandler implements RejectedExecutionHandler{
	@Override
	public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
		// TODO Auto-generated method stub
		log.error("Thread Pool Overflow！！！");
	}
}
