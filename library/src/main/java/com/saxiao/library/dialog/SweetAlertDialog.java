package com.saxiao.library.dialog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.saxiao.library.R;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Administrator on 2019/7/9.
 */

public class SweetAlertDialog extends Dialog implements View.OnClickListener{
	private View mDialogView;
	private AnimationSet mModalInAnim;
	private AnimationSet mModalOutAnim;
	private Animation mOverlayOutAnim;
	private Animation mErrorInAnim;
	private AnimationSet mErrorXInAnim;
	private AnimationSet mSuccessLayoutAnimSet;
	private Animation mSuccessBowAnim;
	private Drawable mCustomImgDrawable;
	private boolean mHideConfirmButton = false;
	public static boolean DARK_STYLE = false;

	private ProgressBar progressBar;
	private String mTitleText;
	private String mContentText;
	private boolean mShowCancel;
	private boolean mShowContent;
	private String mCancelText;
	private String mConfirmText;
	private String mNeutralText;
	private SweetAlertType mAlertAlertType;
	private OnSweetClickListener mCancelClickListener;
	private OnSweetClickListener mConfirmClickListener;
	private OnSweetClickListener mNeutralClickListener;
	private boolean mCloseFromCancel;
	private List<InfoBean> mInfoList;

	private TextView mTitleTextView;
	private TextView mContentTextView;
	private LinearLayout mCustomViewContainer;
	private LinearLayout mButtonsContainer;
	private View mCustomView;
	private View mSuccessLeftMask;
	private View mSuccessRightMask;
	private FrameLayout mErrorFrame;
	private FrameLayout mSuccessFrame;
	private FrameLayout mProgressFrame;
	private FrameLayout mWarningFrame;
	private SuccessTickView mSuccessTick;
	private ImageView mErrorX;
	private ImageView mCustomImage;
	private Button mConfirmButton;
	private Button mCancelButton;
	private Button mNeutralButton;
	/**
	 * 查找条件的布局容器
	 */
	private LinearLayout mSearchLinear;
	/**
	 * 查询条件的布局
	 */
	private LinearLayout mllDialog;
	/**
	 * 查询按钮
	 */
	private Button mBtnSearch;
	private String mBtnSearchText;
	private ImageView mBtnClose;
	/**
	 * 查询标题
	 */
	private TextView mSearchTitle;
	private String mSearchTitleText;
	private OnSendSearchDataListener mSearchDataListener;
	/**
	 * 正常布局
	 */
	private LinearLayout mCommonLinear;
	private TextView mBottomTextTv;
	private String mBottomText;
	private int mbottomTextColor;
	private LinearLayout mCbLinear;
	private TextView mCbTextTv;
	private String mCbText;
	private CheckBox mCbox;

	//aliases
	public final static int BUTTON_CONFIRM = DialogInterface.BUTTON_POSITIVE;
	public final static int BUTTON_CANCEL = DialogInterface.BUTTON_NEGATIVE;

	public SweetAlertDialog hideConfirmButton() {
		this.mHideConfirmButton = true;
		return this;
	}

	public interface OnSweetClickListener {
		void onClick(SweetAlertDialog sweetAlertDialog,boolean selected);
	}

	/**
	 * 查询条件返回值
	 */
	public interface OnSendSearchDataListener{
		void onClick(List<InfoBean> list,SweetAlertDialog sweetAlertDialog);
	}

	public SweetAlertDialog(Context context) {
		this(context, SweetAlertType.NORMAL_TYPE);
	}

	public SweetAlertDialog(Context context, SweetAlertType alertAlertType) {
		super(context, DARK_STYLE ? R.style.alert_dialog_dark : R.style.alert_dialog_light);
		//点击 dialog 框体之外与返回键会取消显示 dialog
		// setCancelable(true);
		//返回键会取消显示 dialog，点击空白区域不会
		setCanceledOnTouchOutside(false);
		mAlertAlertType = alertAlertType;
		mErrorInAnim = OptAnimationLoader.loadAnimation(getContext(), R.anim.error_frame_in);
		mErrorXInAnim = (AnimationSet) OptAnimationLoader.loadAnimation(getContext(), R.anim.error_x_in);
		mSuccessBowAnim = OptAnimationLoader.loadAnimation(getContext(), R.anim.success_bow_roate);
		mSuccessLayoutAnimSet = (AnimationSet) OptAnimationLoader.loadAnimation(getContext(), R.anim.success_mask_layout);
		mModalInAnim = (AnimationSet) OptAnimationLoader.loadAnimation(getContext(), R.anim.modal_in);
		mModalOutAnim = (AnimationSet) OptAnimationLoader.loadAnimation(getContext(), R.anim.modal_out);
		mModalOutAnim.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				mDialogView.setVisibility(View.GONE);
				mDialogView.post(() -> {
					if (mCloseFromCancel) {
						SweetAlertDialog.super.cancel();
					} else {
						SweetAlertDialog.super.dismiss();
					}
				});
			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}
		});
		// dialog overlay fade out
		mOverlayOutAnim = new Animation() {
			@Override
			protected void applyTransformation(float interpolatedTime, Transformation t) {
				WindowManager.LayoutParams wlp = getWindow().getAttributes();
				wlp.alpha = 1 - interpolatedTime;
				getWindow().setAttributes(wlp);
			}
		};
		mOverlayOutAnim.setDuration(120);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alert_dialog);
		mDialogView = getWindow().getDecorView().findViewById(android.R.id.content);
		mTitleTextView = findViewById(R.id.title_text);
		mContentTextView = findViewById(R.id.content_text);
		mCustomViewContainer = findViewById(R.id.loading);
		mErrorFrame = findViewById(R.id.error_frame);
		mErrorX = mErrorFrame.findViewById(R.id.error_x);
		mSuccessFrame = findViewById(R.id.success_frame);
		mProgressFrame = findViewById(R.id.progress_dialog);
		mSuccessTick = mSuccessFrame.findViewById(R.id.success_tick);
		mSuccessLeftMask = mSuccessFrame.findViewById(R.id.mask_left);
		mSuccessRightMask = mSuccessFrame.findViewById(R.id.mask_right);
		mCustomImage = findViewById(R.id.custom_image);
		mWarningFrame = findViewById(R.id.warning_frame);
		mButtonsContainer = findViewById(R.id.buttons_container);
		mConfirmButton = findViewById(R.id.confirm_button);
		mConfirmButton.setOnClickListener(this);
		mConfirmButton.setOnTouchListener(Constants.FOCUS_TOUCH_LISTENER);
		mCancelButton = findViewById(R.id.cancel_button);
		mCancelButton.setOnClickListener(this);
		mCancelButton.setOnTouchListener(Constants.FOCUS_TOUCH_LISTENER);
		mNeutralButton = findViewById(R.id.neutral_button);
		mNeutralButton.setOnClickListener(this);
		mNeutralButton.setOnTouchListener(Constants.FOCUS_TOUCH_LISTENER);
		progressBar = findViewById(R.id.progress);
		mSearchLinear = findViewById(R.id.ll_search);
		mBottomTextTv = findViewById(R.id.tv_bottom_text);
		mCbLinear = findViewById(R.id.cb_ll);
		mCbTextTv = findViewById(R.id.cb_text);
		mCbox = findViewById(R.id.checkBox);
		mCbox.setOnCheckedChangeListener((compoundButton, b) -> mCbox.setSelected(b));

		mllDialog = findViewById(R.id.ll_dialog);
		mBtnSearch = findViewById(R.id.btn_dialog);
		mBtnSearch.setOnClickListener(this);
		mBtnClose = findViewById(R.id.close);
		mBtnClose.setOnClickListener(this);
		mSearchTitle = findViewById(R.id.search_title);
		mCommonLinear = findViewById(R.id.ll_common);

		setTitleText(mTitleText);
		setContentText(mContentText);
		setCustomView(mCustomView);
		setCancelText(mCancelText);
		setConfirmText(mConfirmText);
		setNeutralText(mNeutralText);
		changeAlertType(mAlertAlertType, true);
		setBottomTextTv(mBottomText,mbottomTextColor);
		setBottomCbTv(mCbText);
		//动态生成布局
		initLayout(mInfoList);
		setSearchTitleText(mSearchTitleText);
		setSearchBtnText(mBtnSearchText);

	}

	private void restore() {
		mCustomImage.setVisibility(View.GONE);
		mErrorFrame.setVisibility(View.GONE);
		mSuccessFrame.setVisibility(View.GONE);
		mWarningFrame.setVisibility(View.GONE);
		mProgressFrame.setVisibility(View.GONE);

		mConfirmButton.setVisibility(mHideConfirmButton ? View.GONE : View.VISIBLE);

		adjustButtonContainerVisibility();

		mConfirmButton.setBackgroundResource(R.drawable.blue_button_background);
		mErrorFrame.clearAnimation();
		mErrorX.clearAnimation();
		mSuccessTick.clearAnimation();
		mSuccessLeftMask.clearAnimation();
		mSuccessRightMask.clearAnimation();
	}

	/**
	 * Hides buttons container if all buttons are invisible or gone.
	 * This deletes useless margins
	 */
	private void adjustButtonContainerVisibility() {
		boolean showButtonsContainer = false;
		for (int i = 0; i < mButtonsContainer.getChildCount(); i++) {
			View view = mButtonsContainer.getChildAt(i);
			if (view instanceof Button && view.getVisibility() == View.VISIBLE) {
				showButtonsContainer = true;
				break;
			}
		}
		mButtonsContainer.setVisibility(showButtonsContainer ? View.VISIBLE : View.GONE);
	}

	private void playAnimation() {
		if (mAlertAlertType == SweetAlertType.ERROR_TYPE) {
			mErrorFrame.startAnimation(mErrorInAnim);
			mErrorX.startAnimation(mErrorXInAnim);
		} else if (mAlertAlertType == SweetAlertType.SUCCESS_TYPE) {
			mSuccessTick.startTickAnim();
			mSuccessRightMask.startAnimation(mSuccessBowAnim);
		}
	}

	private void changeAlertType(SweetAlertType alertType, boolean fromCreate) {
		Log.e("xxxx","changetype");
		mAlertAlertType = alertType;
		// call after created views
		if (mDialogView != null) {
			if (!fromCreate) {
				// restore all of views state before switching alert type
				restore();
			}
			mConfirmButton.setVisibility(mHideConfirmButton ? View.GONE : View.VISIBLE);
			switch (mAlertAlertType) {
				case ERROR_TYPE:
					mErrorFrame.setVisibility(View.VISIBLE);
					break;
				case SUCCESS_TYPE:
					mSuccessFrame.setVisibility(View.VISIBLE);
					// initial rotate layout of success mask
					mSuccessLeftMask.startAnimation(mSuccessLayoutAnimSet.getAnimations().get(0));
					mSuccessRightMask.startAnimation(mSuccessLayoutAnimSet.getAnimations().get(1));
					break;
				case WARNING_TYPE:
					//                    mConfirmButton.setBackgroundResource(R.drawable.red_button_background);
					mWarningFrame.setVisibility(View.VISIBLE);
					break;
				case CUSTOM_IMAGE_TYPE:
					setCustomImage(mCustomImgDrawable);
					break;
				case PROGRESS_TYPE:
					mProgressFrame.setVisibility(View.VISIBLE);
					mConfirmButton.setVisibility(View.GONE);
					//                    mButtonsContainer.setVisibility(View.GONE);
					break;
				case SEARCH_TYPE:
					mSearchLinear.setVisibility(View.VISIBLE);
					mCommonLinear.setVisibility(View.GONE);
					break;
				default:
					break;
			}
			adjustButtonContainerVisibility();
			if (!fromCreate) {
				playAnimation();
			}
		}
	}

	public SweetAlertType getAlerType() {
		return mAlertAlertType;
	}

	public void changeAlertType(SweetAlertType alertType) {
		changeAlertType(alertType, false);
	}


	public String getTitleText() {
		return mTitleText;
	}

	public SweetAlertDialog setTitleText(String text) {
		//先在这儿给赋值，等show方法之后加载完布局后再set上值
		mTitleText = text;
		if (mTitleTextView != null && mTitleText != null) {
			if (text.isEmpty()) {
				mTitleTextView.setVisibility(View.GONE);
			} else {
				mTitleTextView.setVisibility(View.VISIBLE);
				mTitleTextView.setText(mTitleText);
			}
		}
		return this;
	}

	public SweetAlertDialog setCustomImage(Drawable drawable) {
		mCustomImgDrawable = drawable;
		if (mCustomImage != null && mCustomImgDrawable != null) {
			mCustomImage.setVisibility(View.VISIBLE);
			mCustomImage.setImageDrawable(mCustomImgDrawable);
		}
		return this;
	}

	public SweetAlertDialog setCustomImage(int resourceId) {
		return setCustomImage(getContext().getResources().getDrawable(resourceId));
	}

	public String getContentText() {
		return mContentText;
	}

	public SweetAlertDialog setContentText(String text) {
		mContentText = text;
		if (mContentTextView != null && mContentText != null) {
			showContentText(true);
			mContentTextView.setText(mContentText);
			mContentTextView.setVisibility(View.VISIBLE);
		}
		return this;
	}

	public boolean isShowCancelButton() {
		return mShowCancel;
	}

	public SweetAlertDialog showCancelButton(boolean isShow) {
		mShowCancel = isShow;
		if (mCancelButton != null) {
			mCancelButton.setVisibility(mShowCancel ? View.VISIBLE : View.GONE);
		}
		return this;
	}

	public boolean isShowContentText() {
		return mShowContent;
	}

	public SweetAlertDialog showContentText(boolean isShow) {
		mShowContent = isShow;
		if (mContentTextView != null) {
			mContentTextView.setVisibility(mShowContent ? View.VISIBLE : View.GONE);
		}
		return this;
	}

	public String getCancelText() {
		return mCancelText;
	}

	public SweetAlertDialog setCancelText(String text) {
		mCancelText = text;
		if (mCancelButton != null && mCancelText != null) {
			showCancelButton(true);
			mCancelButton.setText(mCancelText);
		}
		return this;
	}

	public SweetAlertDialog setConfirmText(String text) {
		mConfirmText = text;
		if (mConfirmButton != null && mConfirmText != null) {
			mConfirmButton.setText(mConfirmText);
		}
		return this;
	}

	public SweetAlertDialog setCancelClickListener(OnSweetClickListener listener) {
		mCancelClickListener = listener;
		return this;
	}

	public SweetAlertDialog setConfirmClickListener(OnSweetClickListener listener) {
		mConfirmClickListener = listener;
		return this;
	}

	public SweetAlertDialog setSearchClickListener(OnSendSearchDataListener listener) {
		mSearchDataListener = listener;
		return this;
	}

	public SweetAlertDialog setNeutralText(String text) {
		mNeutralText = text;
		if (mNeutralButton != null && mNeutralText != null && !text.isEmpty()) {
			mNeutralButton.setVisibility(View.VISIBLE);
			mNeutralButton.setText(mNeutralText);
		}
		return this;
	}

	public SweetAlertDialog setNeutralClickListener(OnSweetClickListener listener) {
		mNeutralClickListener = listener;
		return this;
	}

	@Override
	public void setTitle(CharSequence title) {
		this.setTitleText(title.toString());
	}

	@Override
	public void setTitle(int titleId) {
		this.setTitleText(getContext().getResources().getString(titleId));
	}


	public SweetAlertDialog setConfirmButton(String text, OnSweetClickListener listener) {
		this.setConfirmText(text);
		this.setConfirmClickListener(listener);
		return this;
	}

	public SweetAlertDialog setConfirmButton(int resId, OnSweetClickListener listener) {
		String text = getContext().getResources().getString(resId);
		setConfirmButton(text, listener);
		return this;
	}

	public SweetAlertDialog setCancelButton(String text, OnSweetClickListener listener) {
		this.setCancelText(text);
		this.setCancelClickListener(listener);
		return this;
	}

	public SweetAlertDialog setCancelButton(int resId, OnSweetClickListener listener) {
		String text = getContext().getResources().getString(resId);
		setCancelButton(text, listener);
		return this;
	}

	public SweetAlertDialog setNeutralButton(String text, OnSweetClickListener listener) {
		this.setNeutralText(text);
		this.setNeutralClickListener(listener);
		return this;
	}

	public SweetAlertDialog setNeutralButton(int resId, OnSweetClickListener listener) {
		String text = getContext().getResources().getString(resId);
		setNeutralButton(text, listener);
		return this;
	}

	/**
	 * 设置按钮底部checkbox
	 */
	public SweetAlertDialog setBottomCbTv(String text){
		mCbText = text;
		if (mCbTextTv != null && mCbText != null) {
			if (text.isEmpty()) {
				mCbLinear.setVisibility(View.GONE);
			} else {
				mCbLinear.setVisibility(View.VISIBLE);
				mCbTextTv.setText(mCbText);
			}
		}
		return this;
	}

	/**
	 * 设置底部一行字
	 */
	public SweetAlertDialog setBottomTextTv(String text,int color){
		mBottomText = text;
		mbottomTextColor = color;
		if (mBottomTextTv != null && mBottomText != null) {
			if (text.isEmpty()) {
				mBottomTextTv.setVisibility(View.GONE);
			} else {
				mBottomTextTv.setVisibility(View.VISIBLE);
				mBottomTextTv.setText(mBottomText);
				mBottomTextTv.setTextColor(color);
			}
		}
		return this;
	}

	@Override
	protected void onStart() {
		mDialogView.startAnimation(mModalInAnim);
		playAnimation();
	}

	/**
	 * The real Dialog.cancel() will be invoked async-ly after the animation finishes.
	 */
	@Override
	public void cancel() {
		dismissWithAnimation(true);
	}

	/**
	 * The real Dialog.dismiss() will be invoked async-ly after the animation finishes.
	 */
	public void dismissWithAnimation() {
		dismissWithAnimation(false);
	}

	private void dismissWithAnimation(boolean fromCancel) {
		mCloseFromCancel = fromCancel;
		//several view animations can't be launched at one view, that's why apply alpha animation on child
		//alpha animation
		((ViewGroup) mDialogView).getChildAt(0).startAnimation(mOverlayOutAnim);
		//scale animation
		mDialogView.startAnimation(mModalOutAnim);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.cancel_button) {
			if (mCancelClickListener != null) {
				mCancelClickListener.onClick(SweetAlertDialog.this,false);
			} else {
				dismissWithAnimation();
			}
		} else if (v.getId() == R.id.confirm_button) {
			if (mConfirmClickListener != null) {
				boolean selected = false;
				if(mCbox!=null){
					selected = mCbox.isSelected();
				}
				Log.e("xxxx","select--->"+selected);
				mConfirmClickListener.onClick(SweetAlertDialog.this,selected);
			} else {
				dismissWithAnimation();
			}
		} else if (v.getId() == R.id.neutral_button) {
			if (mNeutralClickListener != null) {
				mNeutralClickListener.onClick(SweetAlertDialog.this,false);
			} else {
				dismissWithAnimation();
			}
		} else if(v.getId() == R.id.btn_dialog){
			if(mSearchDataListener != null){
				mSearchDataListener.onClick(mInfoList,SweetAlertDialog.this);
			}else{
				dismissWithAnimation();
			}
		} else if(v.getId() == R.id.close){
			if (mCancelClickListener != null) {
				mCancelClickListener.onClick(SweetAlertDialog.this,false);
			} else {
				dismissWithAnimation();
			}
		}
	}


	/**
	 * 自定义View样式
	 * @param view
	 */
	public SweetAlertDialog setCustomView(View view) {
		mCustomView = view;
		if (mCustomView != null && mCustomViewContainer != null) {
			mCustomViewContainer.removeAllViews();
			mCustomViewContainer.addView(view);
		}
		return this;
	}

	/**
	 * 查询条件布局的标题
	 */
	public SweetAlertDialog setSearchTitleText(String text) {
		//先在这儿给赋值，等show方法之后加载完布局后再set上值
		mSearchTitleText = text;
		if (mSearchTitle != null && mSearchTitleText != null) {
			if (text.isEmpty()) {
				mSearchTitle.setVisibility(View.GONE);
			} else {
				mSearchTitle.setVisibility(View.VISIBLE);
				mSearchTitle.setText(mSearchTitleText);
			}
		}
		return this;
	}

	/**
	 * 查询条件布局的按钮文字
	 */
	public SweetAlertDialog setSearchButton(String text, OnSendSearchDataListener listener) {
		Log.e("xxx","setSearchButton");
		this.setSearchBtnText(text);
		this.setSearchClickListener(listener);
		return this;
	}

	public SweetAlertDialog setCloseButton(OnSweetClickListener listener){
		this.setCancelClickListener(listener);
		return this;
	}

	public SweetAlertDialog setSearchBtnText(String text) {
		mBtnSearchText = text;
		if (mBtnSearch != null && mBtnSearchText != null) {
			mBtnSearch.setText(mBtnSearchText);
		}
		return this;
	}

	/**
	 * 动态生成查询条件的布局
	 * 保证只走一遍（在onCreate之后会绘制布局，之前mSearchLineart为null）
	 */
	public SweetAlertDialog initLayout(List<InfoBean> infoList){
		mInfoList = infoList;
		if(mSearchLinear == null){
			return this;
		}
		if(mInfoList==null){
			return this;
		}
		for(int j=0;j<mInfoList.size();j++) {
			LinearLayout lLayout = new LinearLayout(this.getContext());
			lLayout.setOrientation(LinearLayout.HORIZONTAL);
			lLayout.setPadding(8, 5, 8, 8);
			LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			lLayout.setLayoutParams(lParams);
			//绘制每一行控件
			for (int i = 0; i < 2; i++) {
				View view = null;
				LinearLayout.LayoutParams viewLayoutParams;
				if(i==0){
					viewLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
				}else{
					viewLayoutParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
					viewLayoutParams.weight = 1f;
				}
				if(i == 0){
					view = initTextView2(viewLayoutParams, mInfoList.get(j).getTitle());
				}
				if(i == 1){
					switch (mInfoList.get(j).getItemType()){
						case DialogItemType.TEXT:
							view = initTextView(viewLayoutParams, mInfoList, j);
							break;
						case DialogItemType.EDIT:
							view = initEditText(viewLayoutParams, mInfoList, j);
							break;
						case DialogItemType.SPINNER:
							view = initSpinner(viewLayoutParams, mInfoList, j);
							viewLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
							break;
						case DialogItemType.DATE:
							view = initDateView(viewLayoutParams, mInfoList, j);
						default:
							break;
					}
				}
				assert view != null;
				view.setLayoutParams(viewLayoutParams);
				lLayout.addView(view);
			}
			if(mllDialog != null){
				mllDialog.addView(lLayout);
			}
		}
		return this;
	}

	/**
	 * 初始化文本类型控件:title
	 */
	private View initTextView2(LinearLayout.LayoutParams viewLayoutParams,String title){
		TextView view = new TextView(this.getContext());
		viewLayoutParams.leftMargin = 5;
		viewLayoutParams.gravity = Gravity.CENTER_VERTICAL;
		view.setText(title);
		view.setTextColor(ContextCompat.getColor(this.getContext(),R.color.t_1));
		view.setGravity(Gravity.START);
		view.setTextSize(16);
		return view;
	}

	/**
	 * 初始化文本类型控件
	 */
	private View initTextView(LinearLayout.LayoutParams viewLayoutParams,List<InfoBean> infoBeans,int i){
		TextView view = new TextView(this.getContext());
		viewLayoutParams.leftMargin = 5;
		viewLayoutParams.gravity = Gravity.CENTER_VERTICAL;
		view.setText(infoBeans.get(i).getText());
		view.setTextColor(ContextCompat.getColor(this.getContext(),R.color.t_4));
		view.setTextSize(12);
		return view;
	}

	/**
	 * 初始化编辑框控件
	 */
	private View initEditText(LinearLayout.LayoutParams viewLayoutParams,List<InfoBean> infoBeans,int i){
		EditText view = new EditText(this.getContext());
		viewLayoutParams.leftMargin = 5;
		viewLayoutParams.gravity = Gravity.CENTER_VERTICAL;
		view.setGravity(Gravity.START);
		view.setBackground(null);
		view.setTextSize(12);
		view.setTextColor(ContextCompat.getColor(this.getContext(),R.color.t_4));
		view.setHint(infoBeans.get(i).getText());
		TextWatcher textWatcher = new TextWatcher() {
			@Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override public void afterTextChanged(Editable editable) {
				infoBeans.get(i).setValue(editable.toString());
			}
		};
		view.addTextChangedListener(textWatcher);
		return view;
	}

	/**
	 * 初始化下拉框控件
	 */
	private View initSpinner(LinearLayout.LayoutParams viewLayoutParams,List<InfoBean> infoBeans,int i){
		MaterialSpinner view = new MaterialSpinner(this.getContext());
		viewLayoutParams.gravity = Gravity.CENTER_VERTICAL;
		view.setGravity(Gravity.START|Gravity.CENTER);
		view.setBackgroundColor(ContextCompat.getColor(this.getContext(), R.color.dialog_background));
		List<String> items = Arrays.asList(infoBeans.get(i).getText().split(","));
		view.setItems(items);
		view.setBackgroundColor(ContextCompat.getColor(this.getContext(),R.color.black_8));
		//设置下拉列表颜色
		view.getPopupWindow().getBackground().setColorFilter(ContextCompat.getColor(this.getContext(),R.color.dialog_background), PorterDuff.Mode.SRC_IN);
		view.setTextSize(12);
		view.setTextColor(ContextCompat.getColor(this.getContext(),R.color.t_4));
		view.setOnItemSelectedListener((view1, position, id, item) -> infoBeans.get(i).setValue(items.get(position)));
		viewLayoutParams.height = 120;
		return view;
	}

	/**
	 * 初始化日期控件
	 */
	private View initDateView(LinearLayout.LayoutParams viewLayoutParams,List<InfoBean> infoBeans,int i) {
		TextView view = new TextView(this.getContext());
		viewLayoutParams.weight = 2f;
		viewLayoutParams.gravity = Gravity.CENTER_VERTICAL;
		viewLayoutParams.topMargin = 5;
		viewLayoutParams.bottomMargin = 5;
		view.setGravity(Gravity.CENTER);
		view.setTextSize(12);
		view.setText("请选择日期");
		view.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.time, 0, 0, 0);

		view.setOnClickListener(view1 -> {
			Calendar c = Calendar.getInstance();
			DatePickerDialog dialog = new DatePickerDialog(this.getContext(), (datePicker, year, month, dayOfMonth) -> {
				String date = com.saxiao.library.dialog.utils.DateUtils.dateAddZero(year,month,String.valueOf(dayOfMonth));
				((TextView) view1).setText(date);
				infoBeans.get(i).setValue(date);
			},c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH));
			dialog.show();
		});
		return view;
	}
}
