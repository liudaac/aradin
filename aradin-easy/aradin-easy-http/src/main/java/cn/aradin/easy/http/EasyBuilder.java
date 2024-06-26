package cn.aradin.easy.http;

import java.util.HashMap;
import java.util.Map;
import org.apache.http.client.config.RequestConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EasyBuilder {
	private static final Logger logger = LoggerFactory.getLogger(EasyBuilder.class);
	/**
	 * 当前应用对应的服务Map
	 */
	public Map<Class<?>, Object> serviceMap = new HashMap<Class<?>, Object>();

	private EasyRequest client;

	private EasyBuilder() {
		client = new EasyRequest();
	}
	
	private static class EasyBuilderHolder {
		private static EasyBuilder factory = new EasyBuilder();
	}

	public static EasyBuilder ins() {
		return EasyBuilderHolder.factory;
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
			return add(serviceInterface, requestConfig);
		}
	}

	/**
	 * 此处未做同步，因为创建多个实例会被覆盖，不会出现问题！ 只会影响首次的创建效率
	 * 
	 * @param serviceInterface
	 * @return
	 */
	private <T> T add(Class<T> serviceInterface, RequestConfig requestConfig) {
		EasyInvocation handler = new EasyInvocation(client, requestConfig);
		T t = handler.create(serviceInterface);
		serviceMap.put(serviceInterface, t);
		return t;
	}
}
