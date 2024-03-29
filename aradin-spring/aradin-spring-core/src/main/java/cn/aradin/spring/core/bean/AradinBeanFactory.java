package cn.aradin.spring.core.bean;

import java.beans.Introspector;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

/**
 * BeanFactory
 * @author daliu
 *
 */
@Component
public class AradinBeanFactory implements ApplicationContextAware {
	
	private final static Logger log = LoggerFactory.getLogger(AradinBeanFactory.class);
	
	private static ApplicationContext applicationContext = null;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		// TODO Auto-generated method stub
		if (AradinBeanFactory.applicationContext == null) {
			AradinBeanFactory.applicationContext = applicationContext;
		}
		if (log.isDebugEnabled()) {
			log.debug("Context初始化");
		}
	}

	/**
	 * 获取applicationContext
	 * @return ApplicationContext
	 */
	@SuppressWarnings("static-access")
	public static ApplicationContext getApplicationContext() {
		while (applicationContext == null) {
			if (log.isDebugEnabled()) {
				log.debug("等待Context初始化》》》请增加{}", "@DependsOn(\"beanUtil\")");
			}
			try {
				Thread.currentThread().sleep(50l);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return applicationContext;
	}

	/**
	 * 通过name获取 Bean.
	 * @param name Bean-Name
	 * @return Instance
	 */
	public static Object getBean(String name) {
		return getApplicationContext().getBean(name);
	}

	/**
	 * 通过class获取Bean.
	 * @param <T> 指定类型
	 * @param clazz Class
	 * @return Instance
	 */
	public static <T> T getBean(Class<T> clazz) {
		return getApplicationContext().getBean(clazz);
	}

	/**
	 * 通过name,以及Clazz返回指定的Bean
	 * @param <T> 指定类型
	 * @param name Bean-Name
	 * @param clazz Class
	 * @return Instance
	 */
	public static <T> T getBean(String name, Class<T> clazz) {
		return getApplicationContext().getBean(name, clazz);
	}

	/**
	 * 获取带header prefix的bean，方便同一个类构造多组实例
	 * @param <T> 指定类型
	 * @param prefix Bean-Name前缀
	 * @param clazz Class
	 * @return Instance
	 */
	public static <T> T getPrefixBean(String prefix, Class<T> clazz) {
		return getPrefixBean(prefix, clazz, null);
	}

	/**
	 * 获取带header prefix的bean，方便同一个类构造多组实例
	 * @param <T> 指定类型
	 * @param prefix Bean-Name前缀
	 * @param clazz Class
	 * @param pathPrefix 指定路径前缀
	 * @return Instance
	 */
	public static <T> T getPrefixBean(String prefix, Class<T> clazz, String pathPrefix) {
		if (applicationContext != null) {
			String path = ClassUtils.getPackageName(clazz);
			if (StringUtils.isNotBlank(pathPrefix) && !path.contains(pathPrefix)) {
				return null;
			}
			String shortClassName = ClassUtils.getShortName(clazz.getName());
			String beanName = prefix + Introspector.decapitalize(shortClassName);
			return applicationContext.getBean(beanName, clazz);
		}
		return null;
	}
}
