package com.saxiao.library.dialog.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import java.io.UnsupportedEncodingException;

public class Base64Utils {
	/**
	 * 加密
	 */
	public static String encodeStr(String str){
		try {
			return Base64.encodeToString(str.getBytes("UTF-8"), Base64.NO_WRAP);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 解密
	 */
	public static String decodeToString(String str){
		try {
			return new String(Base64.decode(str.getBytes("UTF-8"), Base64.NO_WRAP));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 转图片
	 */
	public static Bitmap baseToBitmap(String base64){
		try {
			byte[] decode = Base64.decode(base64, Base64.NO_WRAP);
			return BitmapFactory.decodeByteArray(decode, 0, decode.length);
		}catch (RuntimeException ex){
			return null;
		}

	}

}
