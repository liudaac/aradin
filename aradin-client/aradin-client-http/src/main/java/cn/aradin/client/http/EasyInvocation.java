package cn.aradin.client.http;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.config.RequestConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson2.JSONObject;

import cn.aradin.client.http.annotation.Controller;
import cn.aradin.client.http.annotation.RequestBody;
import cn.aradin.client.http.annotation.RequestHeader;
import cn.aradin.client.http.annotation.RequestMapping;
import cn.aradin.client.http.annotation.RequestParam;
import cn.aradin.client.http.support.RequestMethod;

public class EasyInvocation implements InvocationHandler {

	private static final Logger logger = LoggerFactory.getLogger(EasyInvocation.class);

	/**
	 * 代理的目标接口类
	 */
	private Class<?> target;

	/**
	 * url
	 */
	private String domain;

	private EasyRequest clent;

	private RequestConfig requestConfig;

	private Integer maxAutoRetries;

	public EasyInvocation(EasyRequest client, RequestConfig requestConfig) {
		// TODO Auto-generated constructor stub
		String retries = System.getProperty("aradin.http.retries");
		if (StringUtils.isNumeric(retries)) {
			maxAutoRetries = Integer.parseInt(retries);
		}
		if (maxAutoRetries == null || maxAutoRetries <= 0) {
			maxAutoRetries = 1;
		}
		logger.info("retries>>>>>>>>>>>>>>>>>" + retries);
		this.clent = client;
		this.requestConfig = requestConfig;
	}

	@SuppressWarnings("unchecked")
	public <T> T create(Class<T> target) {
		this.target = target;
		Controller s = target.getAnnotation(Controller.class);
		if (s != null) {
			if (StringUtils.isNotBlank(s.param())) {
				domain = System.getProperty(s.param());
			}
			if (StringUtils.isBlank(domain)) {
				domain = s.value();
			}
			if (StringUtils.isBlank(domain)) {
				logger.error(target.getName() + ",没有定义 @Controller参数！");
				throw new RuntimeException("@Controller参数不全，value or param");
			}
			return (T) Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class<?>[] { target }, this);
		} else {
			logger.error(target.getName() + ",没有定义 @Controller！");
			return null;
		}
	}

	public String getDomain() {
		return this.domain;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		// TODO Auto-generated method stub
		RequestMapping m = method.getAnnotation(RequestMapping.class);
		Type returnClaz = method.getGenericReturnType();
		if (m == null) {
			logger.error(target.getName() + "." + method.getName() + ",没有定义@MyMethod!");
			return null;
		} else {
			// 拼接URL
			int i = 0;
			while (true) {
				try {
					i++;
					String contentType = m.contentType();
					StringBuilder builder = new StringBuilder(domain);
					builder.append(m.value());
					String body = parseBody(method, args);
					Map<String, String> headers = parseHeaders(method, args);
					if (RequestMethod.POST.equals(m.method())) {
						Map<String, String> values = null;
						if (StringUtils.isNotBlank(body)) {
							String params = parseToString(method, args);
							if (params != null) {
								builder.append("?");
								builder.append(params);
							}
						} else {
							values = parseParameters(method, args);
						}
						return clent.request(builder.toString(), returnClaz, RequestMethod.POST, headers, values, body,
								requestConfig, contentType);
					} else {
						String params = parseToString(method, args);
						if (params != null) {
							builder.append("?");
							builder.append(params);
						}
						return clent.request(builder.toString(), returnClaz, RequestMethod.GET, headers, null, body,
								requestConfig, contentType);
					}
				} catch (Exception e) {
					// TODO: handle exception
					logger.error(target.getName() + "." + method.getName() + ",调用异常,重试!");
					if (i >= maxAutoRetries) {
						logger.error(target.getName() + "." + method.getName() + ",超出重试次数!");
						return null;
					}
				}
			}
		}
	}

	protected Map<String, String> parseHeaders(Method method, Object[] args) {
		Map<String, String> values = new LinkedHashMap<String, String>();
		Parameter[] parameters = method.getParameters();
		if (parameters != null && parameters.length > 0) {
			for (int i = 0; i < parameters.length; i++) {
				if (args[i] != null) {
					Parameter parameter = parameters[i];
					RequestHeader requestHeader = parameter.getAnnotation(RequestHeader.class);
					if (requestHeader != null) {
						values.put(requestHeader.value(), String.valueOf(args[i]));
					}
				}
			}
		}
		return values;
	}

	@SuppressWarnings("rawtypes")
	protected Map<String, String> parseParameters(Method method, Object[] args) {
		Map<String, String> values = new LinkedHashMap<String, String>();
		Parameter[] parameters = method.getParameters();
		if (parameters != null && parameters.length > 0) {
			for (int i = 0; i < parameters.length; i++) {
				if (args[i] != null) {
					Parameter parameter = parameters[i];
					Annotation[] annotations = parameter.getAnnotations();
					if (annotations != null && annotations.length > 0) {
						RequestParam requestParam = parameter.getAnnotation(RequestParam.class);
						if (requestParam != null) {
							values.put(requestParam.value(), String.valueOf(args[i]));
						}
					} else if (parameter.getType().isAssignableFrom(Map.class)) {
						Map paramData = (Map) args[i];
						for (Object key : paramData.keySet()) {
							if (paramData.get(key) != null) {
								values.put(String.valueOf(key), String.valueOf(paramData.get(key)));
							}
						}
					} else {
						Class paramClass = parameter.getType();
						Field[] fields = paramClass.getDeclaredFields();
						if (fields != null && fields.length > 0) {
							for (Field field : fields) {
								try {
									if ("serialVersionUID".equals(field.getName())) {
										continue;
									}
									field.setAccessible(true);
									Object fieldValue = field.get(args[i]);
									if (fieldValue != null) {
										values.put(field.getName(), String.valueOf(fieldValue));
									}
								} catch (IllegalArgumentException | IllegalAccessException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}
					}
				}
			}
		}
		return values;
	}

	protected String parseBody(Method method, Object[] args) {
		Parameter[] parameters = method.getParameters();
		if (parameters != null && parameters.length > 0) {
			for (int i = 0; i < parameters.length; i++) {
				if (args[i] != null) {
					Parameter parameter = parameters[i];
					RequestBody requestBody = parameter.getAnnotation(RequestBody.class);
					if (requestBody != null) {
						if (parameter.getType().isAssignableFrom(String.class)
								|| parameter.getType().isAssignableFrom(Integer.class)
								|| parameter.getType().isAssignableFrom(Long.class)
								|| parameter.getType().isAssignableFrom(Float.class)
								|| parameter.getType().isAssignableFrom(Double.class)
								|| parameter.getType().isAssignableFrom(Boolean.class)) {
							return String.valueOf(args[i]);
						} else {
							return JSONObject.toJSONString(args[i]);
						}
					}
				}
			}
		}
		return null;
	}

	/**
	 * 
	 * @param method
	 * @param args
	 * @return
	 */
	protected String parseToString(Method method, Object[] args) {
		StringBuilder builder = new StringBuilder();
		try {
			Map<String, String> parameters = parseParameters(method, args);
			int valid = 0;
			for (String name : parameters.keySet()) {
				if (valid > 0) {
					builder.append("&");
				}
				builder.append(name);
				builder.append("=");
				builder.append(URLEncoder.encode(parameters.get(name), "utf-8"));
				valid++;
			}
			return builder.toString();

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获取指定方法的参数名
	 * 
	 * @param method 要获取参数名的方法
	 * @return 按参数顺序排列的参数名列表
	 */
	protected String[] getMethodParameterNamesByAnnotation(Method method) {
		Annotation[][] parameterAnnotations = method.getParameterAnnotations();
		if (parameterAnnotations == null || parameterAnnotations.length == 0) {
			return null;
		}
		String[] parameterNames = new String[parameterAnnotations.length];
		int i = 0;
		for (Annotation[] parameterAnnotation : parameterAnnotations) {
			for (Annotation annotation : parameterAnnotation) {
				if (annotation instanceof RequestParam) {
					RequestParam param = (RequestParam) annotation;
					parameterNames[i++] = param.value();
				}
			}
		}
		return parameterNames;
	}
}
