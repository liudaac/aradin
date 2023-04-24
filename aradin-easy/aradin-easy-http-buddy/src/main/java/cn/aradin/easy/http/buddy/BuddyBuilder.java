package cn.aradin.easy.http.buddy;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.aradin.easy.http.annotation.Controller;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;

public class BuddyBuilder {

	private static final Logger logger = LoggerFactory.getLogger(BuddyBuilder.class);
	
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
