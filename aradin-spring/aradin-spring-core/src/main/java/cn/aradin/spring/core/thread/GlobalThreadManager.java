package cn.aradin.spring.core.thread;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import cn.aradin.spring.core.thread.handler.ThreadRejectHandler;

public class GlobalThreadManager {
	
	public static final String DEFAULT_SINGLE_POOL_NAME = "DEFAULT_SINGLE_POOL_NAME";

	private static ThreadPoolProxy mLongPool = null;
	private static Object mLongLock = new Object();

	private static ThreadPoolProxy mShortPool = null;
	private static Object mShortLock = new Object();

	private static ThreadPoolProxy mDownloadPool = null;
	private static Object mDownloadLock = new Object();

	private static Map<String, ThreadPoolProxy> mMap = new HashMap<String, ThreadPoolProxy>();
	private static Object mSingleLock = new Object();

	/**
	 * Get the thread pool for downloading
	 * @return thread pool
	 */
	public static ThreadPoolProxy getDownloadPool() {
		if (mDownloadPool == null) {
			synchronized (mDownloadLock) {
				if (mDownloadPool == null) {
					mDownloadPool = new ThreadPoolProxy(4, 10, 10000, 5L);
				}
				return mDownloadPool;
			}
		}
		return mDownloadPool;
	}

	/**
	 * 	获取一个用于执行长耗时任务的线程池，避免和短耗时任务处在同一个队列 而阻塞了重要的短耗时的任务，通常用联网操作s
	 * Get a thread pool for long-duration tasks
	 * @return thread pool
	 */
	public static ThreadPoolProxy getLongPool() {
		if (mLongPool == null) {
			synchronized (mLongLock) {
				if (mLongPool == null) {
					mLongPool = new ThreadPoolProxy(10, 30, 10000, 30L);
				}
				return mLongPool;
			}
		}
		return mLongPool;
	}

	/**
	 *   获取一个用于执行短耗时任务的线程池，避免因为和耗时长的任务处在同一个队列而长时间得不到执行，通常用来执行本地的IO/SQL
	 * Get a thread pool for short-duration tasks  
	 * @return thread pool
	 */
	public static ThreadPoolProxy getShortPool() {
		if (mShortPool == null) {
			synchronized (mShortLock) {
				if (mShortPool == null) {
					mShortPool = new ThreadPoolProxy(8, 20, 100000, 15L);
				}
				return mShortPool;
			}
		}
		return mShortPool;
	}

	/**
	 * Construct a one-thread pool for sync tasks
	 * @return thread pool
	 */
	public static ThreadPoolProxy getSinglePool() {
		return getSinglePool(DEFAULT_SINGLE_POOL_NAME);
	}

	public static ThreadPoolProxy getSinglePool(String name) {
		synchronized (mSingleLock) {
			ThreadPoolProxy singlePool = mMap.get(name);
			if (singlePool == null) {
				singlePool = new ThreadPoolProxy(1, 1, 20000, 5L);
				mMap.put(name, singlePool);
			}
			return singlePool;
		}
	}

	public static class ThreadPoolProxy {
		private ThreadPoolExecutor mPool;
		private int mCorePoolSize; // 线程池维护线程的最少数量
		private int mMaximumPoolSize; // 线程池维护线程的最大数量
		private int queueSize;// 线程池等待队列的长度
		private long mKeepAliveTime; // 线程池维护线程所允许的空闲时间

		private ThreadPoolProxy(int corePoolSize, int maximumPoolSize, int queueSize, long keepAliveTime) {
			this.mCorePoolSize = corePoolSize;
			this.mMaximumPoolSize = maximumPoolSize;
			this.queueSize = queueSize;
			this.mKeepAliveTime = keepAliveTime;
		}

		// 执行任务，当线程池处于关闭，将会重新创建的线程池
		public synchronized void execute(Runnable runn) {
			if (runn == null) {
				return;
			}
			if (mPool == null || mPool.isShutdown()) {
				// 参数说明
				// 当线程池中的线程小于mCorePoolSize，直接创建新的线程加入线程池执行任务
				// 当线程池中的线程数目等于mCorePoolSize，将会把任务放入任务队列BlockingQueue中
				// 当BlockingQueue中的任务放满了，将会创建新的线程去执行，
				// 但是当总线程数大于mMaximumPoolSize时，将会抛出异常，交给RejectedExecutionHandler处理
				// mKeepAliveTime是线程执行完任务后，且队列中没有可以执行的任务，存活的时间，后面的参数是时间单位
				// ThreadFactory是每次创建新的线程工厂
				mPool = new ThreadPoolExecutor(mCorePoolSize,
						mMaximumPoolSize, mKeepAliveTime,
						TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(queueSize), Executors.defaultThreadFactory(),
						new ThreadRejectHandler());
			}
			mPool.submit(runn);
		}

		// 执行任务，当线程池处于关闭，将会重新创建的线程池
		public synchronized <T> Future<T> execute(Callable<T> callable) {
			if (callable == null) {
				return null;
			}
			if (mPool == null || mPool.isShutdown()) {
				mPool = new ThreadPoolExecutor(mCorePoolSize,
						mMaximumPoolSize, mKeepAliveTime,
						TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(queueSize), Executors.defaultThreadFactory(),
						new ThreadRejectHandler());
			}
			return mPool.submit(callable);
		}

		public synchronized void cancel(Runnable runn) {
			if (mPool != null && (!mPool.isShutdown()) || mPool.isTerminating()) {
				mPool.getQueue().remove(runn);
			}
		}

		public synchronized boolean contains(Runnable runn) {
			if (mPool != null && (!mPool.isShutdown() || mPool.isTerminating())) {
				return mPool.getQueue().contains(runn);
			} else {
				return false;
			}
		}

		public void stop() {
			if (mPool != null && (!mPool.isShutdown() || mPool.isTerminating())) {
				mPool.shutdown();
			}
		}

		public synchronized void shutdown() {
			if (mPool != null && (!mPool.isShutdown() || mPool.isTerminating())) {
				mPool.shutdownNow();
			}
		}
	}
}
