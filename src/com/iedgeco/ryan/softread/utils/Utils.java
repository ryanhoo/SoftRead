package com.iedgeco.ryan.softread.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.iedgeco.ryan.softread.config.Config;
import com.iedgeco.ryan.softread.models.SoftNews;

public class Utils {
	public static String dateToString(Date date, String pattern)
			throws Exception {
		return new SimpleDateFormat(pattern).format(date);
	}

	public static Date stringToDate(String dateStr, String pattern)
			throws Exception {
		return new SimpleDateFormat(pattern).parse(dateStr);
	}

	public static ArrayList<SoftNews> readNewsFromJsonStream(InputStream in){
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.readValue(in, new TypeReference<ArrayList<SoftNews>>() {});
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static InputStream readSampleJson(Resources resources){
		try {
			return resources.getAssets().open("sample.json");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String getRealUrlOfPicture(String picFile){
		return Config.SERVER_URL + Config.REQUEST_PICTURE + picFile;
	}
	
	public static String generateMD5Key(String fileName){
		try {
			MessageDigest digest = MessageDigest.getInstance("md5");
			byte[] digestedBytes = digest.digest(fileName.getBytes());
			return new String(digestedBytes);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	/** Check if the network is working.  */
	public static boolean isNetworkAvailable(Context context){
	    ConnectivityManager connMgr = (ConnectivityManager) 
	    		context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
	    return (networkInfo != null && networkInfo.isConnected());
	}
	
	public static boolean saveBitmap(File file, Bitmap bitmap){
		if(file == null || bitmap == null)
			return false;
		try {
			BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file));
			return bitmap.compress(CompressFormat.JPEG, 100, out);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/*
	//check if network is working
		if (Utils.isNetworkAvailable(this)) {
			Log.i(getClass().getSimpleName(), "Network is available! ");
		} else {
			AlertDialog.Builder networkBuilder = new AlertDialog.Builder(this);
			AlertDialog networkDialog = networkBuilder.setTitle("network is not working...")
					.setPositiveButton("settings", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							Intent intent = null;
							// if the Android SDK version is greater than 10
							if (android.os.Build.VERSION.SDK_INT > 10) {
								intent = new Intent( android.provider.Settings.ACTION_WIRELESS_SETTINGS);
							} else {
								intent = new Intent();
								ComponentName component = new ComponentName("com.android.settings","com.android.settings.WirelessSettings");
								intent.setComponent(component);
								intent.setAction("android.intent.action.VIEW");
							}
							SoftReadApplication.this.startActivity(intent);
						}
					})
					.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					})
					.create();
			networkDialog.show();
		}
	 * */
}
