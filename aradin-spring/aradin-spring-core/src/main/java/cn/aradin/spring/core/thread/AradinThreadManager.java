package cn.aradin.spring.core.thread;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

public class AradinThreadManager {
	
	public static final String DEFAULT_SINGLE_POOL_NAME = "DEFAULT_SINGLE_POOL_NAME";

	private static Executor mLongPool = null;
	private static Object mLongLock = new Object();

	private static Executor mShortPool = null;
	private static Object mShortLock = new Object();

	private static Executor mDownloadPool = null;
	private static Object mDownloadLock = new Object();

	private static Map<String, Executor> mMap = new HashMap<String, Executor>();
	private static Object mSingleLock = new Object();

	/**
	 * Get the thread pool for downloading
	 * @return thread pool
	 */
	public static Executor getDownloadPool() {
		if (mDownloadPool == null) {
			synchronized (mDownloadLock) {
				if (mDownloadPool == null) {
					mDownloadPool = AradinExecutors.newFixedThreadPool("download", 4, 10, 10000, 5000l);
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
	public static Executor getLongPool() {
		if (mLongPool == null) {
			synchronized (mLongLock) {
				if (mLongPool == null) {
					mLongPool = AradinExecutors.newFixedThreadPool("longtask", 4, 16, 10000, 5000l);
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
	public static Executor getShortPool() {
		if (mShortPool == null) {
			synchronized (mShortLock) {
				if (mShortPool == null) {
					mShortPool = AradinExecutors.newFixedThreadPool("shorttask", 4, 16, 100000, 5000l);
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
	public static Executor getSinglePool() {
		return getSinglePool(DEFAULT_SINGLE_POOL_NAME);
	}

	public static Executor getSinglePool(String name) {
		synchronized (mSingleLock) {
			Executor singlePool = mMap.get(name);
			if (singlePool == null) {
				singlePool = AradinExecutors.newFixedThreadPool("singletask", 1, 1, 20000, 5000l);
				mMap.put(name, singlePool);
			}
			return singlePool;
		}
	}
}
