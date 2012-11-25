package com.iedgeco.ryan.softread.http;

import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

public class DefaultClient {
	
	private static HttpClient httpClient;

	/**
	 * private constructor
	 * */
	private DefaultClient() {}

	/**
	 * <pre>
	 * Get the default singleton HttpClient
	 * Follow these steps:
	 * 	1. Set basic http params 
	 * 	2. Timeout settings
	 * 	3. Connection mode: http or https
	 * 	4. Create thread-safe HttpClient
	 * </pre>
	 * */
	public static synchronized HttpClient getDefaultClientInstance() {
		if (httpClient == null){
			//1.
			HttpParams params = new BasicHttpParams();
			HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(params, HttpConfig.CHARSET);
			HttpProtocolParams.setUseExpectContinue(params, true);
			HttpProtocolParams.setUserAgent(params, HttpConfig.USER_AGENT);
			
			//2.
			ConnManagerParams.setTimeout(params, 3000);//retrieve from connection time pool in 2s
			HttpConnectionParams.setConnectionTimeout(params, 5000);//connection time out
			HttpConnectionParams.setSoTimeout(params, 5000);//request time out
			
			//3.
			SchemeRegistry schemeRegistry = new SchemeRegistry();
			schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
			schemeRegistry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
			
			//4.
			ClientConnectionManager connManager = new ThreadSafeClientConnManager(params, schemeRegistry);
			httpClient = new DefaultHttpClient(connManager, params);
		}
		
		return httpClient;
	}
	
}//-end of class
