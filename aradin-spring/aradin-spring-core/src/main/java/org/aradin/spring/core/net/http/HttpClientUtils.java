package org.aradin.spring.core.net.http;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.aradin.spring.core.net.http.handler.ResponseHandlerFactory;

/**
 * The Interface For Using Http Clients
 * @author daliu
 *
 */
public class HttpClientUtils {

	protected static HttpClient httpClient = HttpClientFactory.createHttpClient(100, 20);

	private static Map<String, HttpClient> httpClient_mchKeyStore = new HashMap<String, HttpClient>();

	public static void init(int maxTotal, int maxPerRoute) {
		httpClient = HttpClientFactory.createHttpClient(maxTotal, maxPerRoute);
	}

	/**
	 * 初始化 MCH HttpClient KeyStore
	 * 
	 * @param keyStoreName     keyStore 名称
	 * @param keyStoreFilePath 私钥文件路径
	 * @param mch_id
	 * @param maxTotal
	 * @param maxPerRoute
	 */
	public static void initMchKeyStore(String keyStoreName, String keyStoreFilePath, String mch_id, int maxTotal,
			int maxPerRoute) {
		try {
			KeyStore keyStore = KeyStore.getInstance(keyStoreName);
			InputStream instream = HttpClientUtils.class.getClassLoader().getResourceAsStream(keyStoreFilePath);
			keyStore.load(instream, mch_id.toCharArray());
			instream.close();
			HttpClient httpClient = HttpClientFactory.createKeyMaterialHttpClient(keyStore, mch_id,
					new String[] { "TLSv1" }, maxTotal, maxPerRoute);
			httpClient_mchKeyStore.put(mch_id, httpClient);
		} catch (KeyStoreException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (CertificateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static HttpResponse execute(HttpUriRequest request) {
		try {
			return httpClient.execute(request);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static <T> T execute(HttpUriRequest request, ResponseHandler<T> responseHandler) throws Exception {
		try {
			return httpClient.execute(request, responseHandler);
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 数据返回自动JSON对象解析
	 * 
	 * @param request
	 * @param clazz
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static <T> T executeJsonResult(HttpUriRequest request, Class<T> clazz) throws Exception {
		return (T) execute(request, ResponseHandlerFactory.createJsonResponseHandler(clazz));
	}
}
