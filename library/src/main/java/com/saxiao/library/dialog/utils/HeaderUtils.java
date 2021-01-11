package com.saxiao.library.dialog.utils;

import android.util.Base64;
import java.security.MessageDigest;
import java.util.Iterator;
import java.util.Random;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * 传参加密
 */
public class HeaderUtils {
	/**
	 * 排序并取Sha1值
	 * @param secretKey
	 * @param _f
	 * @param _n
	 * @param _t
	 * @return
	 */
	public static String sortAndSha1(String secretKey,String _f,String _n,String _t){
		SortedMap<String,Object> map = new TreeMap<>();
		map.put(secretKey,"1");
		map.put(_f,"2");
		map.put(_t,"3");
		map.put(_n,"4");
		map.comparator();
		StringBuilder sb = new StringBuilder();
		Iterator i = map.entrySet().iterator();
		while (i.hasNext()) {
			java.util.Map.Entry entry = (java.util.Map.Entry) i.next();
			entry.getKey();
			sb.append(entry.getKey());
		}
		//计算字符串的SHA1值
		return getSha1(sb.toString());
	}


	/**
	 * 拼接的字符串的SHA1值
	 * @param str
	 * @return
	 */
	public static String getSha1(String str) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
			'A', 'B', 'C', 'D', 'E', 'F' };
		try {
			MessageDigest mdTemp = MessageDigest.getInstance("SHA1");
			mdTemp.update(str.getBytes("UTF-8"));
			byte[] md = mdTemp.digest();
			int j = md.length;
			char buf[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				buf[k++] = hexDigits[byte0 >>> 4 & 0xf];
				buf[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(buf);
		} catch (Exception e) {
			return null;
		}

	}


	/**
	 * appId的base64
	 * @return
	 */
	public static String encode(String appId) {
		return Base64.encodeToString(appId.getBytes(), Base64.DEFAULT);
	}

	/**
	 * 生成随机串
	 * @param length
	 * @return
	 */
	public static String getCharAndNumr(int length) {
		Random random = new Random();
		StringBuffer valSb = new StringBuffer();
		String charStr = "0123456789abcdefghijklmnopqrstuvwxyz";
		int charLength = charStr.length();
		for (int i = 0; i < length; i++) {
			int index = random.nextInt(charLength);
			valSb.append(charStr.charAt(index));
		}
		return valSb.toString();
	}

}
