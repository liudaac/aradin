package cn.aradin.spring.core.queue;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class AradinQueue<T> extends Thread{
	
	private ReentrantLock newItemLock = new ReentrantLock();
	private Condition newItemCondition = newItemLock.newCondition();
	private long waittime = 10000L;
	private QueueHandler<T> queueHandler = null;
	private String queuename;
	
	public AradinQueue(QueueHandler<T> queueHandler, 
			long waittime,
			String queuename) {
		// TODO Auto-generated constructor stub
		this.queueHandler = queueHandler;
		this.waittime = waittime;
		this.queuename = queuename;
		queueHandler.setName(queuename);
	}
	
	public void pushItem(T t) {
		queueHandler.pushItem(t);
		signalNewItem();
	}
	
	public void waitNewItem() {
		newItemLock.lock();
		try {
			newItemCondition.await(waittime, TimeUnit.MILLISECONDS);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			newItemLock.unlock();
		}
	}
	
	public void signalNewItem() {
		try {
			newItemLock.lock();
			newItemCondition.signalAll();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			newItemLock.unlock();
		}
	}
	
	@SuppressWarnings("static-access")
	@Override
	public void run() {
		try {
			while (!Thread.currentThread().interrupted()) {
				try {
					T t = queueHandler.popItem();
					if (t == null) {
						waitNewItem();
					}else{
						queueHandler.handle(t);
					}
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
		}catch (Exception e) {
			// TODO: handle exception
		}
		System.out.println(queuename+"_AradinQueue thread exit！！！");
	}
	
	public String queuename() {
		return this.queuename;
	}
}
