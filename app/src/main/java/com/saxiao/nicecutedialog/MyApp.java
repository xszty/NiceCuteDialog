package com.saxiao.nicecutedialog;

import android.app.Application;
import android.content.Intent;
import android.util.Log;
import com.saxiao.library.dialog.model.CrashLog;
import com.saxiao.library.dialog.utils.CrashHandler;

public class MyApp extends Application implements CrashHandler.OnSendCrashInfoListener {
	@Override public void onCreate() {
		super.onCreate();
		//CrashHandler.getInstance().initCrashHandler(this);
	}

	@Override public void onAcceptCrash(CrashLog bean) {
		//
		//Toast.makeText(this,"程序正在优化",Toast.LENGTH_SHORT).show();
		//接收到异常信息类
		Log.e("xxxx","接收到了异常信息类："+bean.getClassName()+"\n"+bean.getCrashLine()+"\n"+bean.getCrashCause());
		//TODO 上传异常信息类
		Intent intent = new Intent(this, MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
		android.os.Process.killProcess(android.os.Process.myPid());
		System.exit(0);
	}
}
