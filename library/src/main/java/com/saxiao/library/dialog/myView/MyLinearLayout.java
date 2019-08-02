package com.saxiao.library.dialog.myView;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by yanghui on 2019/8/2.
 * 自定义布局作为根布局
 * 解决键盘弹出把EditText遮挡问题
 *
 * @author yanghui
 */
public class MyLinearLayout extends LinearLayout {

	public MyLinearLayout(Context context) {
		super(context);
	}

	public MyLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MyLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@Override
	protected boolean fitSystemWindows(Rect insets) {
		insets.top = 0;
		return super.fitSystemWindows(insets);
	}
}

