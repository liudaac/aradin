package cn.aradin.client.http;

import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.aradin.client.http.support.LocalHttpClient;
import cn.aradin.client.http.support.RequestMethod;

public class EasyRequest {

	private static final Logger logger = LoggerFactory.getLogger(EasyRequest.class);

	protected EasyRequest() {
	}

	public <T> T request(String url, Type returnClaz, RequestMethod method, Map<String, String> headers,
			Map<String, String> postObj, String postBody, RequestConfig requestConfig, String contentType)
			throws Exception {

		if (requestConfig == null) {
			requestConfig = RequestConfig.custom().build();
		}
		try {
			if (RequestMethod.GET.equals(method) || method == null) {
				HttpUriRequest request = RequestBuilder.get().setUri(url).setConfig(requestConfig).build();
				fixHeaders(request, headers);
				return LocalHttpClient.executeJsonResult(request, returnClaz);
			} else if (RequestMethod.POST.equals(method)) {
				RequestBuilder builder = RequestBuilder.post().setUri(url);
				if (postObj != null && postObj.size() > 0) {
					List<BasicNameValuePair> array = new ArrayList<BasicNameValuePair>();
					for (String key : postObj.keySet()) {
						if (StringUtils.isNotBlank(key)) {
							if (postObj.get(key) != null) {
								BasicNameValuePair valuePair = new BasicNameValuePair(key, postObj.get(key));
								array.add(valuePair);
							}
						}
					}
					builder.setEntity(new UrlEncodedFormEntity(array, "UTF-8"));
				} else if (StringUtils.isNotBlank(postBody)) {
					builder.setEntity(new StringEntity(URLEncoder.encode(postBody, "UTF-8"),
							StringUtils.isNotBlank(contentType) ? ContentType.parse(contentType)
									: ContentType.APPLICATION_JSON));
				}
				HttpUriRequest request = builder.build();
				fixHeaders(request, headers);
				return LocalHttpClient.executeJsonResult(request, returnClaz);
			}
			return null;
		} catch (Exception e) {
			logger.error("request is error。[" + url + "]", e);
			throw e;
		}
	}

	private void fixHeaders(HttpUriRequest request, Map<String, String> headers) {
		if (headers != null && headers.size() > 0) {
			for (String key : headers.keySet()) {
				request.addHeader(key, headers.get(key));
			}
		}
	}
}
