package com.saxiao.library.dialog.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

/**
 * 获取设备唯一Id
 */
public class DeviceAuthIdUtil {

	private static String sID = null;
	private static final String INSTALLATION = "INSTALLATION";
	private static String mMac,mImei,mUUId,mIMSI;

	/**
	 * 蓝牙标识
	 * @return
	 */
	public synchronized static String getBlueId() {
		BluetoothAdapter mBlueth = BluetoothAdapter.getDefaultAdapter();
		String mBluethId = mBlueth.getAddress();
		return mBluethId;
	}

	/**
	 * MAC地址
	 * 在平板设备上，无法通过imei标示设备，我们会将mac地址作为用户的唯一标识
	 * @param context
	 * @return
	 */
	public static String getMacid(Context context) {
		WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		String WLANMAC = wm.getConnectionInfo().getMacAddress();
		return WLANMAC;
	}

	/**
	 * IMEI
	 * @param context
	 * @return
	 */
	@SuppressLint("MissingPermission")
	public static String getIMEI(Context context) {
		TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		try {
			Method method = manager.getClass().getMethod("getImei", int.class);
			String imei1 = (String) method.invoke(manager, 0);
			String imei2 = (String) method.invoke(manager, 1);
			if(TextUtils.isEmpty(imei2)){
				return imei1;
			}
			if(!TextUtils.isEmpty(imei1)){
				//因为手机卡插在不同位置，获取到的imei1和imei2值会交换，所以取它们的最小值,保证拿到的imei都是同一个
				String imei = "";
				if(imei1.compareTo(imei2) <= 0){
					imei = imei1;
				}else{
					imei = imei2;
				}
				return imei;
			}
		} catch (Exception e) {
			e.printStackTrace();
			//return "";
			return manager.getDeviceId();
		}
		return "";
	}

	/**
	 * 获取手机IMSI
	 */
	public static String getIMSI(Context context){
		try {
			TelephonyManager telephonyManager=(TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
			//获取IMSI号
			String imsi=telephonyManager.getSubscriberId();
			if(null==imsi){
				imsi="";
			}
			return imsi;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}


	/**
	 * InstalltionId
	 * 考虑到Android设备的多样性，比如一些平板没有通话功能，或者部分低价设备没有WLAN或者蓝牙，甚至用户不愿意赋予APP这些需要的权限，我们就使用无需权限的方法;这种方式的原理是在程序安装后第一次运行时生成一个ID，该方式和设备唯一标识不一样，不同的应用程序会产生不同的ID，同一个程序重新安装也会不同。所以这不是设备的唯一ID，但是可以保证每个用户的ID是不同的。可以说是用来标识每一份应用程序的唯一ID（即Installtion ID），可以用来跟踪应用的安装数量等
	 * @param context
	 * @return
	 */
	public synchronized static String getInstalltionId(Context context) {
		if (sID == null) {
			File installation = new File(context.getFilesDir(), INSTALLATION);
			try {
				if (!installation.exists()) {
					writeInstallationFile(installation);
				}
				sID = readInstallationFile(installation);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return sID;
	}

	private static String readInstallationFile(File installation) throws IOException {
		RandomAccessFile f = new RandomAccessFile(installation, "r");
		byte[] bytes = new byte[(int) f.length()];
		f.readFully(bytes);
		f.close();
		return new String(bytes);
	}



	private static void writeInstallationFile(File installation) throws IOException {
		FileOutputStream out = new FileOutputStream(installation);
		String id = UUID.randomUUID().toString();
		out.write(id.getBytes());
		out.close();
	}



	/**
	 * 得到全局唯一UUID,有权限时,不可变的
	 * @param context NameActivity.this
	 * @return 返回UUID字符串
	 */
	public static String getUniqueID(Context context) {
		if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

		}
		final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		final String tmDevice, tmSerial, androidId;
		tmDevice = "" + tm.getDeviceId();
		tmSerial = "" + tm.getSimSerialNumber();
		androidId = "" + android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

		UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());
		android.util.Log.e("xxxx","uuid:"+deviceUuid.toString());
		return deviceUuid.toString();
	}
	/**
	 * 得到全局唯一UUID,无权限时通过UUID.randomUUID().toString()随机产生一个UUID
	 * 可变的
	 */
	public static String getUUID(Context context) {
		return UUID.randomUUID().toString();
	}

	/**
	 * 获取设备的id
	 * @param mContext
	 * @return
	 */
	public static String getDeviceId(Context mContext){
		mMac = DeviceAuthIdUtil.getMacid(mContext);
		mImei = DeviceAuthIdUtil.getIMEI(mContext);
		mIMSI = DeviceAuthIdUtil.getIMSI(mContext);
		mUUId = DeviceAuthIdUtil.getUniqueID(mContext);
		String mBeforeLongID =  mImei + mMac + mIMSI+mUUId;
		MessageDigest m = null;
		try {
			m = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		m.update(mBeforeLongID.getBytes(),0,mBeforeLongID.length());
		// get md5 bytes
		byte p_md5Data[] = m.digest();
		// create a hex string
		String mAfterLongID = new String();
		for (int i=0;i<p_md5Data.length;i++) {
			int b = (0xFF & p_md5Data[i]);
			if (b <= 0xF) {
				mAfterLongID+="0";
			}
			// add number to string
			mAfterLongID+=Integer.toHexString(b);
		}
		// hex string to uppercase
		mAfterLongID = mAfterLongID.toUpperCase();
		android.util.Log.e("xxxx","设备Id加密值："+mAfterLongID);
		return mAfterLongID;
	}

	/**
	 * 获取手机厂商
	 */
	public static String getDeviceBrand(){
		return Build.BRAND;
	}
}
