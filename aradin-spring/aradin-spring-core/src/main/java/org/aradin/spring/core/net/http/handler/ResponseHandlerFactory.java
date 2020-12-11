package org.aradin.spring.core.net.http.handler;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSONObject;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ResponseHandlerFactory {
	
	private static Map<String, ResponseHandler<Object>> map = new HashMap<String, ResponseHandler<Object>>();
	
	public static ResponseHandler<Object> createJsonResponseHandler(final Class<?> clazz){

		if(map.containsKey(clazz.getName())){
			return (ResponseHandler<Object>)map.get(clazz.getName());
		}else{
			ResponseHandler<Object> responseHandler = new ResponseHandler<Object>() {
				@Override
				public Object handleResponse(HttpResponse response)
						throws ClientProtocolException, IOException {
					int status = response.getStatusLine().getStatusCode();
	                if (status >= 200 && status < 300) {
	                    HttpEntity entity = response.getEntity();
	                    String str = EntityUtils.toString(entity, Charset.forName("utf-8"));
	                    if (log.isDebugEnabled()) {
	                    	log.debug("HTTP Result：{}", str);
						}
	                    if (String.class.equals(clazz)) {
	                    	return str;
	                    }else if (Integer.class.equals(clazz)) {
							return Integer.parseInt(str);
						}else if (Long.class.equals(clazz)) {
							return Long.parseLong(str);
						}
	                    return JSONObject.parseObject(str, clazz);
	                } else {
	                    throw new ClientProtocolException("Unexpected response status: " + status);
	                }
				}
			};
			map.put(clazz.getName(), responseHandler);
			return responseHandler;
		}
	}
}
