package com.iedgeco.ryan.softread.cache;

import java.io.File;
import java.io.IOException;

import com.iedgeco.ryan.softread.config.Config;
import com.iedgeco.ryan.softread.utils.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class FileCache {
	
	private static final String TAG = "MemoryCache";
	
	private File cacheDir;

	public FileCache(Context context) {
		// Find the dir to save cached images
		if (android.os.Environment.getExternalStorageState()
				.equals(android.os.Environment.MEDIA_MOUNTED))
			cacheDir = new File(
					android.os.Environment.getExternalStorageDirectory(),
					Config.CACHE_DIR);
		else
			cacheDir = context.getCacheDir();
		if (!cacheDir.exists())
			cacheDir.mkdirs();
		
		Log.d(TAG, "cache dir: " + cacheDir.getAbsolutePath());
	}

	public File getFile(String key) {
		File f = new File(cacheDir, key);
		if (f.exists()){
			Log.i(TAG, "the file you wanted exists " + f.getAbsolutePath());
			return f;
		}else{
			Log.w(TAG, "the file you wanted does not exists: " + f.getAbsolutePath());
		}

		return null;
	}

	public void put(String key, Bitmap value){
		File f = new File(cacheDir, key);
		if(!f.exists())
			try {
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		if(Utils.saveBitmap(f, value))
			Log.d(TAG, "Save file to sdcard successfully!");
		else
			Log.w(TAG, "Save file to sdcard failed!");
		
	}
	
	public File createFile(String key) {
		Log.d(TAG, "cache a file: " + key);
		File f = new File(cacheDir, key);
		if(!f.exists())
			try {
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		return f;
	}

	public void clear() {
		File[] files = cacheDir.listFiles();
		for (File f : files)
			f.delete();
	}
}