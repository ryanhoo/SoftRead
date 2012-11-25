package com.iedgeco.ryan.softread.config;

public interface Config {
	public static final String LOCALE_DATE_FORMAT = "yyyy年M月d日 HH:mm:ss";
	public static final String DB_DATA_FORMAT = "yyyy-MM-DD HH:mm:ss";
	public static final String NEWS_ITEM_DATE_FORMAT = "hh:mm M月d日 yyyy";
	
	public static final String DB_SOFT_READ = "soft_read";
	public static final String TABLE_SOFT_NEWS = "soft_news";
	public static final String TABLE_ADMIN = "admin";

	public static final String SERVER_URL = "http://169.254.242.251:8080/SoftRead-Server/";
	
	public static final String REQUEST_PICTURE = "upload/";
	public static final String REQUEST_UPDATE = "update/";
	
}
