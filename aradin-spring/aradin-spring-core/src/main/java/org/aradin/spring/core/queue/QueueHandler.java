package org.aradin.spring.core.queue;

public interface QueueHandler<T> {
	
	public void setName(String queue);
	
	public void pushItem(T item);
	
	public T popItem();
	
	public void handle(T item);
}
