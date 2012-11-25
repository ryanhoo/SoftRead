package com.iedgeco.ryan.softread.utils;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import android.content.res.Resources;

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
}
