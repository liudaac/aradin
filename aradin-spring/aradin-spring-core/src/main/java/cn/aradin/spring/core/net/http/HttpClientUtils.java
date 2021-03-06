package cn.aradin.spring.core.net.http;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.util.EntityUtils;

import cn.aradin.spring.core.net.http.handler.ResponseHandlerFactory;

/**
 * The Interface For Using Http Clients
 * @author liudaac
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
	 * @param mch_id mch_id
	 * @param maxTotal maxTotal
	 * @param maxPerRoute maxPerRoute
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
	 * @param <T> The class name of clazz
	 * @param request The URI request
	 * @param clazz The result class
	 * @return Instance of clazz
	 * @throws Exception Exception
	 */
	@SuppressWarnings("unchecked")
	public static <T> T executeJsonResult(HttpUriRequest request, Class<T> clazz) throws Exception {
		return (T) execute(request, ResponseHandlerFactory.createJsonResponseHandler(clazz));
	}
	
	/**
	 * Download Data
	 * @param url Target
	 * @return Datas
	 */
	public static byte[] downloadData(String url) {
		 HttpGet httpGet = new HttpGet(url);
		 try {
			HttpResponse response = httpClient.execute(httpGet);
			HttpEntity entity = response.getEntity();
	        if (entity == null) {
	            return null;
	        }
	        return EntityUtils.toByteArray(entity);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
