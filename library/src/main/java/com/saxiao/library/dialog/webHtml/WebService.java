package com.saxiao.library.dialog.webHtml;

import android.app.Service;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import com.saxiao.library.dialog.base.Constant;
import com.saxiao.library.dialog.utils.NetUtils;
import com.yanzhenjie.andserver.AndServer;
import com.yanzhenjie.andserver.Server;
import com.yanzhenjie.andserver.filter.HttpCacheFilter;
import com.yanzhenjie.andserver.website.AssetsWebsite;
import java.util.concurrent.TimeUnit;

public class WebService extends Service {


	protected Server.Builder builder;
	private AssetManager mAssetManager ;
	@Override public void onCreate() {
		super.onCreate();
		mAssetManager = getAssets();
		builder = AndServer.serverBuilder()
			.website(new AssetsWebsite(mAssetManager,"web"))
			.inetAddress(NetUtils.getLocalIPAddress()) //服务器要监听的网络地址
			.port(Constant.PORT_SERVER)
			.timeout(10, TimeUnit.SECONDS)
			.filter(new HttpCacheFilter()); //开启缓存支持

		initBuilder();

	}

	public void initBuilder(){

	}



	@Nullable
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}


}
