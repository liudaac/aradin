package cn.aradin.spring.core.thread;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class AradinThreadFactory implements ThreadFactory {

	private final String group;
	
	private final AtomicInteger nextId = new AtomicInteger(1);
	
	public AradinThreadFactory(String group) {
		// TODO Auto-generated constructor stub
		this.group = group;
	}
	
	@Override
	public Thread newThread(Runnable r) {
		// TODO Auto-generated method stub
		String name = group + "-" + nextId.getAndIncrement();
		Thread thread = new Thread(null, r, name, 0);
		return thread;
	}

}
