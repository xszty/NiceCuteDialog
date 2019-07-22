package com.saxiao.library.dialog;

/**
 * Created by yanghui on 2019/5/27.
 *
 * 封装的dialog每个控件所对应的类型
 *
 */

public @interface DialogItemType {

	/**
	 * 文本
	 */
	int TEXT = 0;
	/**
	 * 编辑
	 */
	int EDIT = 1;
	/**
	 *  下拉
	 */
	int SPINNER = 2;
	/**
	 *  日期
	 */
	int DATE = 3;

}
