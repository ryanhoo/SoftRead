package com.iedgeco.ryan.softread.cache;

import java.io.IOException;
import java.io.InputStream;

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
	
	private Context context;
	
	class AsyncImageDownloader extends AsyncTask<Void, Void, Bitmap>{
		
		private ImageView imageView;
		private String url;
		
		public AsyncImageDownloader(ImageView imageView, String url){
			this.imageView = imageView;
			this.url = url;
		}
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			imageView.setImageResource(R.drawable.placeholder);
		}

		@Override
		protected Bitmap doInBackground(Void... arg0) {
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
			
		}
		
		
	}
	
	private AsyncImageLoader(Context context){
		this.context = context;
	}
	
	public static AsyncImageLoader getInstance(Context context){
		if(imageLoader == null)
			imageLoader = new AsyncImageLoader(context);
		
		return imageLoader;
	}

	public void displayImageBitmap(ImageView imageView, String picFile){
		new AsyncImageDownloader(imageView, Utils.getRealUrlOfPicture(picFile)).execute();
	}
}
