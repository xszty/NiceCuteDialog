package com.saxiao.library.dialog.utils;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;
import com.google.gson.Gson;
import com.saxiao.library.dialog.model.CrashLog;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 异常捕获
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler{

	public static CrashHandler mAppCrashHandler;

	private Thread.UncaughtExceptionHandler mDefaultHandler;

	private OnSendCrashInfoListener listener;

	public interface OnSendCrashInfoListener{
		void onAcceptCrash(CrashLog bean);
	}

	public void initCrashHandler(Application app) {
		// 获取系统默认的UncaughtException处理器
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(this);
		this.listener = (OnSendCrashInfoListener) app;
	}

	public static CrashHandler getInstance() {
		if (mAppCrashHandler == null) {
			mAppCrashHandler = new CrashHandler();
		}
		return mAppCrashHandler;
	}

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		//用户是否处理了异常信息
		boolean hasHandle = handleException(ex);
		if(!hasHandle && mDefaultHandler != null){
			//没有处理系统默认的处理器处理
			mDefaultHandler.uncaughtException(thread, ex);
		}else{
			//处理了，
		}

	}

	/**
	 * 错误处理,收集错误信息 发送错误报告等操作均在此完成.
	 * @param e
	 * @return true:如果处理了该异常信息;否则返回false.
	 */
	private boolean handleException(Throwable e) {
		if (e == null) {
			return false;
		}
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		pw.flush();

		Log.e("xxxx","e:"+new Gson().toJson(e));
		String stackTrace = sw.toString();
		String cause = e.getMessage();
		Throwable rootTr = e;
		while (e.getCause() != null) {
			e = e.getCause();
			if (e.getStackTrace() != null && e.getStackTrace().length > 0) {
				rootTr = e;
			}
			String msg = e.getMessage();
			if (!TextUtils.isEmpty(msg)) {
				cause = msg;
			}
		}
		String exceptionType = rootTr.getClass().getName();

		String throwClassName;
		String throwMethodName;
		int throwLineNumber;

		if (rootTr.getStackTrace().length > 0) {
			StackTraceElement trace = rootTr.getStackTrace()[0];
			throwClassName = trace.getClassName();
			throwMethodName = trace.getMethodName();
			throwLineNumber = trace.getLineNumber();
		} else {
			throwClassName = "unknown";
			throwMethodName = "unknown";
			throwLineNumber = 0;
		}


		// 自定义处理错误信息
		Log.e("xxxxx","ex-classname:"+throwClassName);
		Log.e("xxxxx","ex-method:"+throwMethodName);
		Log.e("xxxxx","ex-line:"+throwLineNumber);
		Log.e("xxxxx","ex-cause:"+cause);


		CrashLog crashLog = new CrashLog();
		crashLog.setClassName(throwClassName);
		crashLog.setMethodName(throwMethodName);
		crashLog.setCrashLine(String.valueOf(throwLineNumber));
		crashLog.setCrashCause(cause);
		crashLog.setDeviceModel(DeviceAuthIdUtil.getDeviceBrand());
		listener.onAcceptCrash(crashLog);

		return true;
	}



}

