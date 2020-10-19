package com.saxiao.library.dialog.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;


@SuppressWarnings("unused")
public class NetworkUtil {

	private static final int NETWORN_NONE = 0;
	private static final int NETWORN_WIFI = 1;
	private static final int NETWORN_2G = 2;
	private static final int NETWORN_3G = 3;
	private static final int NETWORN_4G = 4;
	private static final int NETWORN_MOBILE = 5;

	private NetworkUtil() {
		throw new UnsupportedOperationException("u can't instantiate me...");
	}

	private static int getNetworkState(Context context) {
		ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (null == connManager) {
			return NETWORN_NONE;
		}
		NetworkInfo activeNetInfo = connManager.getActiveNetworkInfo();
		if (activeNetInfo == null || !activeNetInfo.isAvailable()) {
			return NETWORN_NONE;
		}

		NetworkInfo wifiInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (null != wifiInfo) {
			NetworkInfo.State state = wifiInfo.getState();
			if (null != state) {
				if (state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING) {
					return NETWORN_WIFI;
				}
			}
		}

		NetworkInfo networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		if (null != networkInfo) {
			NetworkInfo.State state = networkInfo.getState();
			String strSubTypeName = networkInfo.getSubtypeName();
			if (null != state) {
				if (state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING) {
					switch (activeNetInfo.getSubtype()) {
						case TelephonyManager.NETWORK_TYPE_GPRS:
						case TelephonyManager.NETWORK_TYPE_CDMA:
						case TelephonyManager.NETWORK_TYPE_EDGE:
						case TelephonyManager.NETWORK_TYPE_1xRTT:
						case TelephonyManager.NETWORK_TYPE_IDEN:
							return NETWORN_2G;
						case TelephonyManager.NETWORK_TYPE_EVDO_A:
						case TelephonyManager.NETWORK_TYPE_UMTS:
						case TelephonyManager.NETWORK_TYPE_EVDO_0:
						case TelephonyManager.NETWORK_TYPE_HSDPA:
						case TelephonyManager.NETWORK_TYPE_HSUPA:
						case TelephonyManager.NETWORK_TYPE_HSPA:
						case TelephonyManager.NETWORK_TYPE_EVDO_B:
						case TelephonyManager.NETWORK_TYPE_EHRPD:
						case TelephonyManager.NETWORK_TYPE_HSPAP:
							return NETWORN_3G;
						case TelephonyManager.NETWORK_TYPE_LTE:
							return NETWORN_4G;
						default:
							if ("TD-SCDMA".equalsIgnoreCase(strSubTypeName) || "WCDMA".equalsIgnoreCase(strSubTypeName) || "CDMA2000".equalsIgnoreCase(strSubTypeName)) {
								return NETWORN_3G;
							} else {
								return NETWORN_MOBILE;
							}
					}
				}
			}
		}
		return NETWORN_NONE;
	}

	public static boolean isNetworkStatus(Context context){
		boolean result = false;
		switch (getNetworkState(context)) {
			case 0:
			case 2:
				result = false;
				break;
			case 1:
			case 3:
			case 4:
			case 5:
				result = true;
				break;
			default:
				break;
		}
		return result;
	}

}
