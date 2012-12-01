package com.iedgeco.ryan.softread.cache;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.HttpResponse;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.iedgeco.ryan.softread.R;
import com.iedgeco.ryan.softread.http.HttpRetriever;
import com.iedgeco.ryan.softread.utils.Utils;

public class AsyncImageLoader {

	private static final String TAG = "AsyncImageLoader";
	private static AsyncImageLoader imageLoader;
	
	//caches
	private MemoryCache memoryCache;
	private FileCache fileCache;
	
	class AsyncImageDownloader extends AsyncTask<Void, Void, Bitmap>{
		private ImageView imageView;
		private String fileName;
		
		public AsyncImageDownloader(ImageView imageView, String fileName){
			this.imageView = imageView;
			this.fileName = fileName;
		}
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			imageView.setImageResource(R.drawable.placeholder);
		}

		@Override
		protected Bitmap doInBackground(Void... arg0) {
			String url = Utils.getRealUrlOfPicture(fileName);
			HttpResponse response = new HttpRetriever().requestGet(url, null);
			Log.i(TAG, "url: " + url);
			Log.i(TAG, "respone: " + response);
			InputStream in = null;
			try {
				if(response != null && response.getEntity() != null)
					in = response.getEntity().getContent();
			} catch (IllegalStateException e) {
				e.printStackTrace();
				return null;
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
			
			//TODO to be optimized: adjust the size of bitmap
			return BitmapFactory.decodeStream(in);
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			super.onPostExecute(result);
			if(result != null && imageView != null)
				imageView.setImageBitmap(result);
			
			//TODO cache the bitmap both in sdcard & memory
			memoryCache.put(fileName, result);// key is a unique token, value is the bitmap
			
			fileCache.put(fileName, result);
		}
	}
	
	private AsyncImageLoader(Context context){
		this.memoryCache 		= 	new MemoryCache();
		this.fileCache			= 	new FileCache(context);
	}
	
	public static AsyncImageLoader getInstance(Context context){
		if(imageLoader == null)
			imageLoader = new AsyncImageLoader(context);
		
		return imageLoader;
	}

	public void displayImageBitmap(ImageView imageView, String fileName){
		//TODO to be optimized
		new AsyncImageDownloader(imageView, fileName).execute();
	}
	
	public void displayBitmap(ImageView imageView, String fileName){
		//no pic for this item
		if(fileName == null || "".equals(fileName))
			return;
		
		Bitmap bitmap = getBitmap(fileName);
		//search in cache, if there is no such bitmap, launch downloads
		if(bitmap != null){
			imageView.setImageBitmap(bitmap);
		}
		else{
			Log.w(TAG, "Can't find the file required.");
			new AsyncImageDownloader(imageView, fileName).execute();
		}
	}
	
	public Bitmap getBitmap(String key){
		Bitmap bitmap = null;
		//1. search memory
		bitmap = memoryCache.get(key);
		
		//2. search sdcard
		if(bitmap == null){
			File file = fileCache.getFile(key);
			if(file != null)
				bitmap = BitmapHelper.decodeFile(file, null);
		}
		
		return bitmap;
	}
	
	public void clearCache(){
		if(memoryCache != null)
			memoryCache.clear();
		if(fileCache != null)
			fileCache.clear();
	}
}
