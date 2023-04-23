package cn.aradin.easy.http.annotation.support;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EncryptHolder {

	private static final Logger logger = LoggerFactory.getLogger(EncryptHolder.class);
	
	public static Map<Class<?>, Function<String, String>> serviceMap = new HashMap<Class<?>, Function<String, String>>();
	
	public static Function<String, String> function(Class<? extends Function<String, String>> funClass) {
		if (!serviceMap.containsKey(funClass)) {
			if (logger.isDebugEnabled()) {
				logger.debug("Function存在" + funClass);
			}
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("Function不存在" + funClass);
			}
			try {
				serviceMap.put(funClass, funClass.getDeclaredConstructor().newInstance());
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException | SecurityException e) {
				// TODO Auto-generated catch block
				throw new RuntimeException(e.getCause());
			}
		}
		return serviceMap.get(funClass);
	}
}
