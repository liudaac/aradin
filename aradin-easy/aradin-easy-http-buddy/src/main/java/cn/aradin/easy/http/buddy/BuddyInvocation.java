package cn.aradin.easy.http.buddy;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.config.RequestConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson2.JSONObject;

import cn.aradin.easy.http.EasyRequest;
import cn.aradin.easy.http.annotation.RequestBody;
import cn.aradin.easy.http.annotation.RequestHeader;
import cn.aradin.easy.http.annotation.RequestMapping;
import cn.aradin.easy.http.annotation.RequestParam;
import cn.aradin.easy.http.annotation.support.EncryptHolder;
import cn.aradin.easy.http.support.RequestMethod;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;

public class BuddyInvocation {
	
	private static final Logger logger = LoggerFactory.getLogger(BuddyInvocation.class);
	
	private String domain;
	
	private EasyRequest clent = new EasyRequest();

	private RequestConfig requestConfig;

	public BuddyInvocation(String domain, RequestConfig requestConfig) {
		// TODO Auto-generated constructor stub
		this.domain = domain;
		this.requestConfig = requestConfig;
	}
	
    @RuntimeType
    public Object intercept(@Origin Method method, @AllArguments Object[] args) throws IOException {
    	RequestMapping m = method.getAnnotation(RequestMapping.class);
		Type returnClaz = method.getGenericReturnType();
		if (m == null) {
			logger.error(returnClaz.getTypeName() + "." + method.getName() + ",没有定义@MyMethod!");
			return null;
		} else {
			int retries = m.retries();
			int i = 0;
			while (true) {
				try {
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
					logger.error(returnClaz.getTypeName() + "." + method.getName() + ",调用异常,重试!");
					if (i >= retries) {
						logger.error(returnClaz.getTypeName() + "." + method.getName() + ",超出重试次数!");
						return null;
					}
				} finally {
					i++;
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
						values.put(requestHeader.value(), EncryptHolder.function(requestHeader.encrypt()).apply(String.valueOf(args[i])));
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
						String result = null;
						if (parameter.getType().isAssignableFrom(String.class)
								|| parameter.getType().isAssignableFrom(Integer.class)
								|| parameter.getType().isAssignableFrom(Long.class)
								|| parameter.getType().isAssignableFrom(Float.class)
								|| parameter.getType().isAssignableFrom(Double.class)
								|| parameter.getType().isAssignableFrom(Boolean.class)) {
							result = String.valueOf(args[i]);
						} else {
							result = JSONObject.toJSONString(args[i]);
						}
						return EncryptHolder.function(requestBody.encrypt()).apply(result);
					}
				}
			}
		}
		return null;
	}
	
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
}
