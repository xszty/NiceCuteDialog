package com.saxiao.nicecutedialog;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import com.saxiao.library.dialog.DialogItemType;
import com.saxiao.library.dialog.InfoBean;
import com.saxiao.library.dialog.SweetAlertDialog;
import com.saxiao.library.dialog.SweetAlertType;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

	private String sn,stockstate,relsn,planstockdtStart,planstockdtEnd;
	private String stockStateText;
	private List<InfoBean> curList = new ArrayList<>();
	@Override protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		TextView tvhw = findViewById(R.id.tv_hw);
		tvhw.setOnClickListener(new View.OnClickListener() {
			@Override public void onClick(View view) {
				showSearchDialog();
			}
		});

	}

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
