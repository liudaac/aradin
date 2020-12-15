package cn.aradin.spring.core.net.http;

import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;

import javax.net.ssl.SSLContext;

import org.apache.http.client.HttpClient;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;

/**
 * httpclient 4.3.x
 * @author daliu
 *
 */
public class HttpClientFactory {
	
	public static HttpClient createHttpClient() {
		try {
			SSLContext sslContext = SSLContexts.custom().setProtocol(SSLConnectionSocketFactory.SSL).build();
			SSLConnectionSocketFactory sf = new SSLConnectionSocketFactory(sslContext,SSLConnectionSocketFactory.getDefaultHostnameVerifier());
			return HttpClientBuilder.create().setSSLSocketFactory(sf).build();
		} catch (KeyManagementException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static HttpClient createHttpClient(int maxTotal,int maxPerRoute) {
		try {
			SSLContext sslContext = SSLContexts.custom().setProtocol(SSLConnectionSocketFactory.SSL).build();
			SSLConnectionSocketFactory sf = new SSLConnectionSocketFactory(sslContext,SSLConnectionSocketFactory.getDefaultHostnameVerifier());
			PoolingHttpClientConnectionManager poolingHttpClientConnectionManager = new PoolingHttpClientConnectionManager();
			poolingHttpClientConnectionManager.setMaxTotal(maxTotal);
			poolingHttpClientConnectionManager.setDefaultMaxPerRoute(maxPerRoute);
			return HttpClientBuilder.create()
									.setConnectionManager(poolingHttpClientConnectionManager)
									.setSSLSocketFactory(sf)
									.build();
		} catch (KeyManagementException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Key store 类型HttpClient
	 * @param keystore
	 * @param keyPassword
	 * @param supportedProtocols
	 * @param maxTotal
	 * @param maxPerRoute
	 * @return
	 */
	public static HttpClient createKeyMaterialHttpClient(KeyStore keystore,String keyPassword,String[] supportedProtocols,int maxTotal,int maxPerRoute) {
		try {
			RegistryBuilder<ConnectionSocketFactory> registryBuilder = RegistryBuilder.<ConnectionSocketFactory>create();
			registryBuilder.register("http", PlainConnectionSocketFactory.INSTANCE);  
			
			//指定信任密钥存储对象和连接套接字工厂  
			SSLContext sslContext = SSLContexts.custom().loadKeyMaterial(keystore, keyPassword.toCharArray()).build();
			SSLConnectionSocketFactory sf = new SSLConnectionSocketFactory(sslContext,supportedProtocols,
	                null,SSLConnectionSocketFactory.getDefaultHostnameVerifier());
			registryBuilder.register("https", sf);
			Registry<ConnectionSocketFactory> registry = registryBuilder.build();
			PoolingHttpClientConnectionManager poolingHttpClientConnectionManager = new PoolingHttpClientConnectionManager(registry);
			poolingHttpClientConnectionManager.setMaxTotal(maxTotal);
			poolingHttpClientConnectionManager.setDefaultMaxPerRoute(maxPerRoute);
			return HttpClientBuilder.create()
									.setConnectionManager(poolingHttpClientConnectionManager)
									.setSSLSocketFactory(sf)
									.build();
		} catch (KeyManagementException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnrecoverableKeyException e) {
			e.printStackTrace();
		} catch (KeyStoreException e) {
			e.printStackTrace();
		}
		return null;
	}
}
