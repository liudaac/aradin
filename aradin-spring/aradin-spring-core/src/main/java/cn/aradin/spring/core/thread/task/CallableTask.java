package cn.aradin.spring.core.thread.task;

import java.util.concurrent.Callable;

import org.apache.commons.lang3.ArrayUtils;

public abstract class CallableTask implements Callable<String>{
	
	protected static String SUCCESS = "success";
	protected static String FAILED = "failed";
	
	protected Object[] objects = null;
	public CallableTask(Object...objects)
	{
		this.objects = ArrayUtils.clone(objects);
	}
	
	@Override
	public abstract String call() throws Exception;
}
