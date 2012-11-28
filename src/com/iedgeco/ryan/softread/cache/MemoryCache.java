package com.iedgeco.ryan.softread.cache;

import java.util.WeakHashMap;

import android.graphics.Bitmap;
import android.util.Log;

public class MemoryCache {
	private static final String TAG = "MemoryCache";
    private WeakHashMap<String, Bitmap> cache = new WeakHashMap<String, Bitmap>();
    
    public Bitmap get(String key){
        if(key != null)
        	return cache.get(key);
        return null;
    }
    
    public void put(String key, Bitmap bitmap){
    	Log.d(TAG, "size of memory cache: " + cache.size());
    	if(key != null && !"".equals(key) && bitmap != null){
    		cache.put(key, bitmap);
    		Log.i(TAG, "cache bitmap: " + key);
    	}
    }

    public void clear() {
        cache.clear();
    }
}