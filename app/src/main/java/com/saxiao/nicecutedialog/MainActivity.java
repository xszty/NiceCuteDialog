package com.saxiao.nicecutedialog;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.saxiao.library.dialog.DialogItemType;
import com.saxiao.library.dialog.InfoBean;
import com.saxiao.library.dialog.SweetAlertDialog;
import com.saxiao.library.dialog.SweetAlertType;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

	private String sn, stockstate, relsn, planstockdtStart, planstockdtEnd;
	private String stockStateText;
	private List<InfoBean> curList = new ArrayList<>();
	private String a;
	private String TAG = "xxxx";
	private LocationManager locationManager;
	private StringBuilder oInfo = new StringBuilder();
	private StringBuilder bInfo = new StringBuilder();

	@Override protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Button btnO = findViewById(R.id.btn_ori);
		TextView tvOinfo = findViewById(R.id.tv_ori_info);
		Button btnBai = findViewById(R.id.btn_bai);
		TextView tvBInfo = findViewById(R.id.tv_bai_info);

		tvOinfo.setText(oInfo.toString());
		tvBInfo.setText(bInfo.toString());

		btnO.setOnClickListener(new View.OnClickListener() {
			@Override public void onClick(View view) {
				locationManager = (LocationManager) MainActivity.this.getSystemService(Context.LOCATION_SERVICE);
				//绑定监听状态
				if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
					// TODO: Consider calling
					//    ActivityCompat#requestPermissions
					// here to request the missing permissions, and then overriding
					//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
					//                                          int[] grantResults)
					// to handle the case where the user grants the permission. See the documentation
					// for ActivityCompat#requestPermissions for more details.
					return;
				}
				locationManager.addGpsStatusListener(listener);
				// 1秒更新一次，或最小位移变化超过1米更新一次；
				//注意：此处更新准确度非常低，推荐在service里面启动一个Thread，在run中sleep(10000);然后执行handler.sendMessage(),更新位置
				locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, locationListener);


			}
		});

		//SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(this,SweetAlertType.WARNING_TYPE);
		//sweetAlertDialog.setTitleText("确定退出？");
		//sweetAlertDialog.show();

		//SweetAlertDialog s = new SweetAlertDialog(this,SweetAlertType.PROGRESS_TYPE);
		//s.show();

		//Log.e("xxxxxLogin","LoginActivity："+ DeviceAuthIdUtil.getDeviceId(MainActivity.this));


	}



	private LocationListener locationListener = new LocationListener() {

		/**
		 * 位置信息变化时触发
		 */
		@Override public void onLocationChanged(Location location) {
			//updateView(location);
			oInfo = new StringBuilder();
			oInfo.append("经度：" + location.getLongitude()+"\n"+"纬度：" + location.getLatitude());
			Log.i(TAG, "时间：" + location.getTime());
			Log.i(TAG, "经度：" + location.getLongitude());
			Log.i(TAG, "纬度：" + location.getLatitude());
			Log.i(TAG, "海拔：" + location.getAltitude());
		}

		/**
		 * GPS状态变化时触发
		 */
		@Override public void onStatusChanged(String provider, int status, Bundle extras) {
			switch (status) {
				//GPS状态为可见时
				case LocationProvider.AVAILABLE:
					Log.i(TAG, "当前GPS状态为可见状态");
					break;
				//GPS状态为服务区外时
				case LocationProvider.OUT_OF_SERVICE:
					Log.i(TAG, "当前GPS状态为服务区外状态");
					break;
				//GPS状态为暂停服务时
				case LocationProvider.TEMPORARILY_UNAVAILABLE:
					Log.i(TAG, "当前GPS状态为暂停服务状态");
					break;
			}
		}

		/**
		 * GPS开启时触发
		 */
		@Override public void onProviderEnabled(String provider) {
			if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
				&& ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
				// TODO: Consider calling
				//    ActivityCompat#requestPermissions
				// here to request the missing permissions, and then overriding
				//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
				//                                          int[] grantResults)
				// to handle the case where the user grants the permission. See the documentation
				// for ActivityCompat#requestPermissions for more details.
				return;
			}
			Location location = locationManager.getLastKnownLocation(provider);
			Log.e("xxxxx","location:"+location.getLatitude()+"-->"+location.getLongitude());
			//updateView(location);
		}

		/**
		 * GPS禁用时触发
		 */
		@Override public void onProviderDisabled(String provider) {
			//updateView(null);
		}


	};

	private void setProgressDialog(){
		SweetAlertDialog proDialog = new SweetAlertDialog(MainActivity.this,SweetAlertType.PROGRESS_TYPE);

	}

	//状态监听
	GpsStatus.Listener listener = new GpsStatus.Listener() {
		public void onGpsStatusChanged(int event) {
			switch (event) {
				//第一次定位
				case GpsStatus.GPS_EVENT_FIRST_FIX:
					Log.i(TAG, "第一次定位");
					oInfo.append("第一次定位");
					break;
				//卫星状态改变
				case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
					oInfo.append("");
					Log.i(TAG, "卫星状态改变");
					//获取当前状态
					GpsStatus gpsStatus=locationManager.getGpsStatus(null);
					//获取卫星颗数的默认最大值
					int maxSatellites = gpsStatus.getMaxSatellites();
					//创建一个迭代器保存所有卫星
					Iterator<GpsSatellite> iters = gpsStatus.getSatellites().iterator();
					int count = 0;
					while (iters.hasNext() && count <= maxSatellites) {
						GpsSatellite s = iters.next();
						count++;
					}
					System.out.println("搜索到："+count+"颗卫星");
					break;
				//定位启动
				case GpsStatus.GPS_EVENT_STARTED:
					Log.i(TAG, "定位启动");
					break;
				//定位结束
				case GpsStatus.GPS_EVENT_STOPPED:
					Log.i(TAG, "定位结束");
					break;
			}
		};
	};

	//private void oriGps(){
	//	if (!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
	//		Toast.makeText(context, "请开启GPS导航...", Toast.LENGTH_SHORT).show();
	//		// 返回开启GPS导航设置界面
	//		Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
	//		context.startActivityForResult(intent, 0);
	//		return;
	//	}
	//
	//}


	/**
	 * 显示查询条件的对话框
	 */
	private void showSearchDialog(){
		new SweetAlertDialog(MainActivity.this, SweetAlertType.SEARCH_TYPE)
			.setSearchTitleText("查询条件")
			.initLayout(setDialogData())
			.setSearchButton("确定", (list, dialog) -> {
				dialog.dismissWithAnimation();
				curList.addAll(list);
				for(int i=0;i<list.size();i++) {
					switch (list.get(i).getKey()) {
						case "searchParam.sn":
							sn = list.get(i).getValue();
							break;
						case "searchParam.stockstate":
							stockStateText = list.get(i).getValue();
							switch (list.get(i).getValue()){
								case "请选择":
									stockstate = "";
									break;
								case "待配料":
									stockstate = "0";
									break;
								case "配料中":
									stockstate = "1";
									break;
								case "配料完成":
									stockstate = "2";
									break;
								default:
									stockstate = "";
									break;
							}
							break;
						case "searchParam.relsn":
							relsn = list.get(i).getValue();
							break;
						case "searchParam.planstockdtStart":
							planstockdtStart = list.get(i).getValue();
							break;
						case "searchParam.planstockdtEnd":
							planstockdtEnd = list.get(i).getValue();
							break;
						default:
							break;
					}
				}
				Log.e("xxxxx",""+sn+"//"+stockstate+"//"+relsn+"//"+planstockdtStart+"//"+planstockdtEnd);
			})
			.setResetButton("重置", new SweetAlertDialog.OnSendResetDataListener() {
				@Override public void onClick(List<InfoBean> list, SweetAlertDialog sweetAlertDialog) {
					//重置
					sn = "";
					stockstate = "";
					stockStateText = "";
					relsn = "";
					planstockdtStart = "";
					planstockdtEnd = "";
					sweetAlertDialog.dismissWithAnimation();
					showSearchDialog();
				}
			})
			.setCloseButton((sweetAlertDialog, selected) -> sweetAlertDialog.dismissWithAnimation())
			.show();
	}

	/*


	 */

	/**
	 * 设置dialog的数据
	 */
	private List<InfoBean> setDialogData(){
		List<InfoBean> info = new ArrayList<>();
		InfoBean i1 = new InfoBean(DialogItemType.EDIT,"配料单号：","请输入配料单号","searchParam.sn",sn);
		InfoBean i2 = new InfoBean(DialogItemType.SPINNER,"配料状态：","请选择,待配料,配料中,配料完成","searchParam.stockstate",stockStateText);
		InfoBean i3 = new InfoBean(DialogItemType.EDIT,"相关计划单号：","请输入相关计划单号","searchParam.relsn",relsn);
		InfoBean i4 = new InfoBean(DialogItemType.DATE,"计划开始配料时间：","请选择开始时间","searchParam.planstockdtStart",planstockdtStart);
		InfoBean i5 = new InfoBean(DialogItemType.DATE,"计划结束配料时间：","请选择结束时间","searchParam.planstockdtEnd",planstockdtEnd);
		info.add(i1);
		info.add(i2);
		info.add(i3);
		info.add(i4);
		info.add(i5);
		return info;
	}



	///**
	// * 设置dialog的数据
	// * @return
	// */
	//private List<InfoBean> setDialogData(){
	//	List<InfoBean> info = new ArrayList<>();
	//	InfoBean i1 = new InfoBean(DialogItemType.EDIT,"缺料单单号：","请输入缺料单号","searchParam.sn","");
	//	InfoBean i2 = new InfoBean(DialogItemType.DATE,"检修开始日期：","请输入检修开始时间","searchParam.startTime","");
	//	InfoBean i3 = new InfoBean(DialogItemType.DATE,"检修结束日期：","请输入检修结束时间","searchParam.endTime","");
	//	InfoBean i4 = new InfoBean(DialogItemType.SPINNER,"是否已生成采购计划：","请选择,是,否","searchParam.state","");
	//	info.add(i1);
	//	info.add(i2);
	//	info.add(i3);
	//	info.add(i4);
	//	return info;
	//}
}
