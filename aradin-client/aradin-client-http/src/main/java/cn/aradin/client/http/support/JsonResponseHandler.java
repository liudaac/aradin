package cn.aradin.client.http.support;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.TypeReference;

public class JsonResponseHandler{

	private static Map<String, ResponseHandler<?>> map = new HashMap<String, ResponseHandler<?>>();
	
	private static final Logger logger = LoggerFactory.getLogger(JsonResponseHandler.class);

	@SuppressWarnings({ "unchecked" })
	public static ResponseHandler<Object> createResponseHandler(final Type clazz){

		if(map.containsKey(clazz.getTypeName())){
			return (ResponseHandler<Object>)map.get(clazz.getTypeName());
		}else{
			ResponseHandler<Object> responseHandler = new ResponseHandler<Object>() {
				@Override
				public Object handleResponse(HttpResponse response)
						throws ClientProtocolException, IOException {
					int status = response.getStatusLine().getStatusCode();
	                if (status >= 200 && status < 300) {
	                    HttpEntity entity = response.getEntity();
	                    String str = EntityUtils.toString(entity, Charset.forName("utf-8"));   
	                    logger.debug("返回结果》》》》》"+str);
	                    if (String.class.equals(clazz)) {
	                    	return str;
	                    }else if (Integer.class.equals(clazz)) {
							return Integer.parseInt(str);
						}else if (Long.class.equals(clazz)) {
							return Long.parseLong(str);
						}
	                    return JSONObject.parseObject(str, TypeReference.get(clazz));
	                } else {
	                    throw new ClientProtocolException("Unexpected response status: " + status);
	                }
				}
			};
			map.put(clazz.getTypeName(), responseHandler);
			return responseHandler;
		}
	}

	
}
