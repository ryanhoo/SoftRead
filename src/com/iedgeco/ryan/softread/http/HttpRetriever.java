package com.iedgeco.ryan.softread.http;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentProducer;
import org.apache.http.entity.EntityTemplate;

import android.util.Log;

public class HttpRetriever {

	private static final String TAG = "HttpRetriever";
	
	private HttpClient defaultClient;
	
	public HttpRetriever(){
		this.defaultClient = DefaultClient.getDefaultClientInstance();
	}
	
	/**
	 * 1. HttpGet<br/>
	 * 2. Set http headers<br/>
	 * 3. Execute HttpGet request
	 * */
	public HttpResponse requestGet(String url, Map<String, String> headers){
		//1.
		HttpGet httpGet = new HttpGet(url);
		
		//2.
		if(headers != null){
			Iterator<Entry<String, String>> iterator = headers.entrySet().iterator();
			while(iterator.hasNext()){
				Entry<String, String> entry = iterator.next();
				String headeName = entry.getKey();
				String headerValue = entry.getValue();
				Log.d(TAG, "name: " + headeName + " value: " + headerValue);
				httpGet.addHeader(headeName, headerValue);
			}
		}
		
		//3.
		HttpResponse httpResponse = null;
		try {
			httpResponse = defaultClient.execute(httpGet);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return httpResponse;
	}
	
	/**
	 * 1. HttpPost
	 * 2. Set http headers
	 * 3. ContentProducer
	 * 4. HttpEntity
	 * 5. Execute HttpPost request
	 * */
	public HttpResponse requestPost(String url, Map<String, String> headers){
		//1.
		HttpPost httpPost = new HttpPost(url);
		
		//2.
		if(headers != null){
			Iterator<Entry<String, String>> iterator = headers.entrySet().iterator();
			while(iterator.hasNext()){
				Entry<String, String> entry = iterator.next();
				String headeName = entry.getKey();
				String headerValue = entry.getValue();
				Log.d(TAG, "name: " + headeName + " value: " + headerValue);
				httpPost.addHeader(headeName, headerValue);
			}
		}
		
		//3.
		ContentProducer contentProducer = new ContentProducer() {
			@Override
			public void writeTo(OutputStream outstream) throws IOException {
				Writer writer = new OutputStreamWriter(outstream, HttpConfig.CHARSET);
				//TODO writer.write(requestBody);
				writer.flush();
				writer.close();
			}
		};
		
		//4.
		HttpEntity entity = new EntityTemplate(contentProducer);
		httpPost.setEntity(entity);
		
		//5.
		HttpResponse httpResponse = null;
		try {
			httpResponse = defaultClient.execute(httpPost);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return httpResponse;
	}
}
