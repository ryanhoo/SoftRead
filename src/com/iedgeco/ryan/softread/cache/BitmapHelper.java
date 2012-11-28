package com.iedgeco.ryan.softread.cache;

import java.io.File;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;

public class BitmapHelper {

	public static Bitmap decodeFile(File file, Options options){
		return BitmapFactory.decodeFile(file.getAbsolutePath(), options);
	}
}
