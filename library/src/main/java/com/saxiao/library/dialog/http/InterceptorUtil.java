package com.saxiao.library.dialog.http;

import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Retrofit2+Rxjava2+OKHttp3+RxAndroid
 * 日志拦截器
 */

public class InterceptorUtil {
	private static String jse;

	public static String getJse() {
		return jse;
	}


	//日志拦截器
    public static HttpLoggingInterceptor LogInterceptor() {
        return new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                if(message.indexOf("JSESSIONID")>-1&message.indexOf(";")>-1){//截取JSESSIONID
                    //Set-Cookie: JSESSIONID=37A0CE2FD1EDF7B428CF5C953F07CF1E; Path=/psms; HttpOnly
                    String[] splitCookie = message.split(";");
                    String[] splitSessionId = splitCookie[0].split("=");
                    String JSESSIONID = splitSessionId[1];
                    //SPUtils.put("JSESSIONID",JSESSIONID);
	                jse = JSESSIONID;
                }
                System.out.println(message);
            }
        }).setLevel(HttpLoggingInterceptor.Level.BODY);//设置打印数据的级别
    }
}
