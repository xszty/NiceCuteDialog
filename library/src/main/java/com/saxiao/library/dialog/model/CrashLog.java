package com.saxiao.library.dialog.model;

/**
 * 异常日志
 */
public class CrashLog {
	/**
	 * 异常发生的类
	 */
	private String className;
	/**
	 * 异常发生的方法
	 */
	private String methodName;
	/**
	 * 异常发生在第几行
	 */
	private String crashLine;
	/**
	 * 异常原因
	 */
	private String crashCause;

	/**
	 * 异常发生的时间
	 */
	private String crashTime;

	/**
	 * 设备型号
	 */
	private String deviceModel;

	/**
	 * 设备版本号
	 */
	private String deviceVersionCode;

	/**
	 * 设备版本名称
	 */
	private String deviveVersionName;

	/**
	 * 账号信息
	 */
	private String userName;
	/**
	 * 所在工区
	 */
	private String deptName;

	/**
	 * 设备imei值
	 */
	private String imei;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String getCrashLine() {
		return crashLine;
	}

	public void setCrashLine(String crashLine) {
		this.crashLine = crashLine;
	}

	public String getCrashCause() {
		return crashCause;
	}

	public void setCrashCause(String crashCause) {
		this.crashCause = crashCause;
	}

	public String getCrashTime() {
		return crashTime;
	}

	public void setCrashTime(String crashTime) {
		this.crashTime = crashTime;
	}

	public String getDeviceModel() {
		return deviceModel;
	}

	public void setDeviceModel(String deviceModel) {
		this.deviceModel = deviceModel;
	}

	public String getDeviceVersionCode() {
		return deviceVersionCode;
	}

	public void setDeviceVersionCode(String deviceVersionCode) {
		this.deviceVersionCode = deviceVersionCode;
	}

	public String getDeviveVersionName() {
		return deviveVersionName;
	}

	public void setDeviveVersionName(String deviveVersionName) {
		this.deviveVersionName = deviveVersionName;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}
}
