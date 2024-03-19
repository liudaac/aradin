package cn.aradin.easy.http.buddy;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.config.RequestConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.aradin.easy.http.annotation.Controller;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;

public class BuddyBuilder {

	private static final Logger logger = LoggerFactory.getLogger(BuddyBuilder.class);
	
	public Map<Class<?>, Object> serviceMap = new HashMap<Class<?>, Object>();

	private static class BuddyBuilderHolder {
		private static BuddyBuilder factory = new BuddyBuilder();
	}

	public static BuddyBuilder ins() {
		return BuddyBuilderHolder.factory;
	}
	
	public <T> T service(Class<T> serviceInterface) {
		return service(serviceInterface, null);
	}

	@SuppressWarnings("unchecked")
	public <T> T service(Class<T> serviceInterface, RequestConfig requestConfig) {
		if (serviceMap.containsKey(serviceInterface)) {
			logger.debug("Service存在" + serviceInterface);
			return (T) serviceMap.get(serviceInterface);
		} else {
			logger.debug("Service不存在" + serviceInterface);
			try {
				T t = create(serviceInterface);
				serviceMap.put(serviceInterface, t);
				return t;
			} catch (Exception e) {
				// TODO: handle exception
				throw new RuntimeException(e.getCause());
			}
		}
	}
	
	public <T> T create(Class<T> interfaceClazz) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		Controller s = interfaceClazz.getAnnotation(Controller.class);
		String domain = null;
		if (s != null) {
			if (StringUtils.isNotBlank(s.param())) {
				domain = System.getProperty(s.param());
			}
			if (StringUtils.isBlank(domain)) {
				domain = s.value();
			}
		}
		if (StringUtils.isBlank(domain)) {
			logger.error(interfaceClazz.getName() + ",没有定义 @Controller参数！");
			throw new RuntimeException("@Controller参数不全，value or param");
		}
		ByteBuddy buddy = new ByteBuddy();
		return buddy.subclass(interfaceClazz).name(getNewClassName(interfaceClazz))
				.method(ElementMatchers.any())
				.intercept(MethodDelegation.to(new BuddyInvocation(domain, null)))
				.make()
				.load(BuddyBuilder.class.getClassLoader(), ClassLoadingStrategy.Default.WRAPPER)
				.getLoaded().getDeclaredConstructor().newInstance();
	}
	
	private static String getNewClassName(Class<?> clazz) {
        return clazz.getSimpleName() + "Impl";
    }
}
