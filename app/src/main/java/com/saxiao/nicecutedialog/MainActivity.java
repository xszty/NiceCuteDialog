package com.saxiao.nicecutedialog;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import com.saxiao.library.dialog.DialogItemType;
import com.saxiao.library.dialog.InfoBean;
import com.saxiao.library.dialog.SweetAlertDialog;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

	@Override protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		//new SweetAlertDialog(MainActivity.this, SweetAlertType.SEARCH_TYPE)
		//	.setSearchTitleText("查询测试")
		//	.initLayout(setDialogData())
		//	.setSearchButton("确定", (list, dialog) -> {
		//		dialog.dismissWithAnimation();
		//		for(int i=0;i<list.size();i++){
		//			Log.e("xxxxx","value:"+list.get(i).getValue());
		//		}
		//	})
		//	.setCloseButton(sweetAlertDialog -> sweetAlertDialog.dismissWithAnimation())
		//	.show();

		//new SweetAlertDialog(MainActivity.this,SweetAlertType.WARNING_TYPE)
		//	.setTitleText("dsdsaf")
		//	.setConfirmButton("是", sweetAlertDialog -> {
		//		Log.e("xxxx","zzszzzzzz");
		//		sweetAlertDialog.dismissWithAnimation();
		//	})
		//	.setCancelButton("否", sweetAlertDialog -> sweetAlertDialog.dismissWithAnimation())
		//	.show();
		new SweetAlertDialog(MainActivity.this)
			.setTitleText("是否检修完成？")
			.setBottomCbTv("是否入库")
			.setConfirmButton("是", new SweetAlertDialog.OnSweetClickListener() {
				@Override public void onClick(SweetAlertDialog sweetAlertDialog, boolean selected) {
					if (selected) {
						Log.e("xxx", "选择了入库");
					} else {
						Log.e("xxx", "不入库");
					}
				}
			})
			.setCancelButton("否", (sweetAlertDialog, selected) -> {

			})
			.show();
	}

	/**
	 * 设置dialog的数据
	 * @return
	 */
	private List<InfoBean> setDialogData(){
		List<InfoBean> info = new ArrayList<>();
		InfoBean i1 = new InfoBean(DialogItemType.EDIT,"缺料单单号：","请输入缺料单号","searchParam.sn","");
		InfoBean i2 = new InfoBean(DialogItemType.DATE,"检修开始日期：","请输入检修开始时间","searchParam.startTime","");
		InfoBean i3 = new InfoBean(DialogItemType.DATE,"检修结束日期：","请输入检修结束时间","searchParam.endTime","");
		InfoBean i4 = new InfoBean(DialogItemType.SPINNER,"是否已生成采购计划：","请选择,是,否","searchParam.state","");
		info.add(i1);
		info.add(i2);
		info.add(i3);
		info.add(i4);
		return info;
	}
}
