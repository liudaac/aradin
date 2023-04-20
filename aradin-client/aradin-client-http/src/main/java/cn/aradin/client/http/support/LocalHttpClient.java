package cn.aradin.client.http.support;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LocalHttpClient {

	private static final Logger logger = LoggerFactory.getLogger(LocalHttpClient.class);
	private static final String caller = System.getProperty("aradin.http.caller");
	protected static HttpClient httpClient = HttpClientFactory.createHttpClient(100,20);
	
	private static Map<String,HttpClient> httpClient_mchKeyStore = new HashMap<String, HttpClient>();
	public static void init(int maxTotal,int maxPerRoute){
		httpClient = HttpClientFactory.createHttpClient(maxTotal,maxPerRoute);
	}

	/**
	 * 初始化   MCH HttpClient KeyStore
	 * @param keyStoreName  keyStore 名称
	 * @param keyStoreFilePath 私钥文件路径
	 * @param mch_id
	 * @param maxTotal
	 * @param maxPerRoute
	 */
	public static void initMchKeyStore(String keyStoreName,String keyStoreFilePath,String mch_id,int maxTotal,int maxPerRoute){
		try {
			KeyStore keyStore = KeyStore.getInstance(keyStoreName);
			InputStream instream = LocalHttpClient.class.getClassLoader().getResourceAsStream(keyStoreFilePath);
//			 FileInputStream instream = new FileInputStream(new File(keyStoreFilePath));
			 keyStore.load(instream,mch_id.toCharArray());
			 instream.close();
			 HttpClient httpClient = HttpClientFactory.createKeyMaterialHttpClient(keyStore, mch_id, new String[]{"TLSv1"}, maxTotal, maxPerRoute);
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


	public static HttpResponse execute(HttpUriRequest request){
		try {
			return httpClient.execute(request);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static <T> T execute(HttpUriRequest request,ResponseHandler<T> responseHandler) throws Exception{
		try {
			if (logger.isDebugEnabled()) {
				logger.debug(request.getMethod()+" "+request.getURI());
			}
			if (StringUtils.isNotBlank(caller)) {
				request.addHeader("EASY-CALLER", caller);
			}
			return httpClient.execute(request, responseHandler);
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 数据返回自动JSON对象解析
	 * @param request
	 * @param clazz
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public static <T> T executeJsonResult(HttpUriRequest request,Type clazz) throws Exception{
		if (logger.isDebugEnabled()) {
			logger.debug(request.getMethod()+" "+request.getURI());
		}
		return (T)execute(request,JsonResponseHandler.createResponseHandler(clazz));
	}
}
