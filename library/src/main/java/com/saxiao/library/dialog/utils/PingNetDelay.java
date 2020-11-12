package com.saxiao.library.dialog.utils;

import android.util.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * pingIp工具类
 */
public class PingNetDelay {


	public static String getNetDelayMs(String ip){
		String delay =new String();
		Process p = null;
		try{
			p = Runtime.getRuntime().exec("/system/bin/ping -c 4 "+ip);
			BufferedReader buf =new BufferedReader(new InputStreamReader(p.getInputStream()));
			String str = new String();
			while((str=buf.readLine())!=null){
				Log.e("xxxx","str:"+str);
				if(str.contains("avg")){
					int i=str.indexOf("/",20);
					int j=str.indexOf(".", i);
					System.out.println("延迟:"+str.substring(i+1, j));
					delay =str.substring(i+1, j);
				}
			}
			return delay;

		}catch(IOException e) {
			e.printStackTrace();
			return "";
		}

	}

}
