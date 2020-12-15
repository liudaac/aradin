package cn.aradin.spring.core.thread.task;

import org.apache.commons.lang3.ArrayUtils;

public abstract class RunnableTask implements Runnable{
	
	protected Object[] objects = null;
	public RunnableTask(Object...objects)
	{
		this.objects = ArrayUtils.clone(objects);
	}
	
	public abstract void run();
}
