package cn.aradin.spring.core.context.holder;

import java.lang.reflect.Constructor;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ReflectionUtils;

import cn.aradin.spring.core.context.AradinContext;
import cn.aradin.spring.core.context.strategy.AradinContextHolderStrategy;
import cn.aradin.spring.core.context.strategy.GlobalAradinContextHolderStrategy;
import cn.aradin.spring.core.context.strategy.InheritableThreadLocalAradinContextHolderStrategy;
import cn.aradin.spring.core.context.strategy.ThreadLocalAradinContextHolderStrategy;

/**
 * Context holder
 * @author daliu
 *
 */
public class AradinContextHolder {
	/**
	 * MODE_THREADLOCAL
	 */
	public static final String MODE_THREADLOCAL = "MODE_THREADLOCAL";
	/**
	 * MODE_INHERITABLETHREADLOCAL
	 */
	public static final String MODE_INHERITABLETHREADLOCAL = "MODE_INHERITABLETHREADLOCAL";
	/**
	 * MODE_GLOBAL
	 */
	public static final String MODE_GLOBAL = "MODE_GLOBAL";
	/**
	 * SYSTEM_PROPERTY for context strategy
	 */
	public static final String SYSTEM_PROPERTY = "aradin.context.strategy";
	/**
	 * Current strategyName
	 */
	private static String strategyName = System.getProperty(SYSTEM_PROPERTY);
	/**
	 * Current strategy
	 */
	private static AradinContextHolderStrategy strategy;
	/**
	 * Initial size
	 */
	private static int initializeCount = 0;

	static {
		initialize();
	}
	
	/**
	 * Explicitly clears the context value from the current thread.
	 */
	public static void clearContext() {
		strategy.clearContext();
	}

	/**
	 * Obtain the current <code>AradinContext</code>.
	 *
	 * @return the Aradin context (never <code>null</code>)
	 */
	public static AradinContext getContext() {
		return strategy.getContext();
	}

	/**
	 * Primarily for troubleshooting purposes, this method shows how many times the class
	 * has re-initialized its <code>AradinContextHolderStrategy</code>.
	 *
	 * @return the count (should be one unless you've called
	 * {@link #setStrategyName(String)} to switch to an alternate strategy.
	 */
	public static int getInitializeCount() {
		return initializeCount;
	}
	
	private static void initialize() {
		if (StringUtils.isBlank(strategyName)) {
			// Set default
			strategyName = MODE_THREADLOCAL;
		}

		if (strategyName.equals(MODE_THREADLOCAL)) {
			strategy = new ThreadLocalAradinContextHolderStrategy();
		}
		else if (strategyName.equals(MODE_INHERITABLETHREADLOCAL)) {
			strategy = new InheritableThreadLocalAradinContextHolderStrategy();
		}
		else if (strategyName.equals(MODE_GLOBAL)) {
			strategy = new GlobalAradinContextHolderStrategy();
		}
		else {
			// Try to load a custom strategy
			try {
				Class<?> clazz = Class.forName(strategyName);
				Constructor<?> customStrategy = clazz.getConstructor();
				strategy = (AradinContextHolderStrategy) customStrategy.newInstance();
			}
			catch (Exception ex) {
				ReflectionUtils.handleReflectionException(ex);
			}
		}

		initializeCount++;
	}
	
	/**
	 * Associates a new <code>AradinContext</code> with the current thread of execution.
	 *
	 * @param context the new <code>AradinContext</code> (may not be <code>null</code>)
	 */
	public static void setContext(AradinContext context) {
		strategy.setContext(context);
	}

	/**
	 * Changes the preferred strategy. Do <em>NOT</em> call this method more than once for
	 * a given JVM, as it will re-initialize the strategy and adversely affect any
	 * existing threads using the old strategy.
	 *
	 * @param strategyName the fully qualified class name of the strategy that should be
	 * used.
	 */
	public static void setStrategyName(String strategyName) {
		AradinContextHolder.strategyName = strategyName;
		initialize();
	}

	/**
	 * Allows retrieval of the context strategy. See SEC-1188.
	 *
	 * @return the configured strategy for storing the Aradin context.
	 */
	public static AradinContextHolderStrategy getContextHolderStrategy() {
		return strategy;
	}

	/**
	 * Delegates the creation of a new, empty context to the configured strategy.
	 * 
	 * @return context
	 */
	public static AradinContext createEmptyContext() {
		return strategy.createEmptyContext();
	}

	@Override
	public String toString() {
		return "AradinContextHolder[strategy='" + strategyName + "'; initializeCount="
				+ initializeCount + "]";
	}
}
