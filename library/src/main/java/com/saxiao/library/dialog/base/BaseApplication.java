package com.saxiao.library.dialog.base;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import com.saxiao.library.dialog.utils.NetworkUtil;

/**
 * Created by ruinszero on 2017/8/31.
 */

public class BaseApplication extends Application{
	public boolean isConnected;
	private NetWorkReceiver receiver;

	@Override public void onCreate() {
		super.onCreate();
		if(receiver == null){
			receiver = new NetWorkReceiver();
			IntentFilter filter = new IntentFilter();
			filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
			registerReceiver(receiver,filter);
		}
	}

	public class NetWorkReceiver extends BroadcastReceiver{

		@Override public void onReceive(Context context, Intent intent) {
			isConnected = NetworkUtil.isNetworkStatus(context);
		}
	}

	public boolean isConnected(){
		return isConnected;
	}
}
