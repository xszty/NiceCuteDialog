package com.saxiao.library.dialog.myView;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;

public class DragFloatActionButton extends LinearLayout {

    private int parentHeight;
    private int parentWidth;
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            setAlpha(1f);
        }
    };




    private int lastX;
    private int lastY;

    private boolean isDrag;
    private ViewGroup parent;

    public DragFloatActionButton(Context context) {
        super(context);
    }

    public DragFloatActionButton(Context context,  AttributeSet attrs) {
        super(context, attrs);
    }

    public DragFloatActionButton(Context context,  AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

	//@Override public boolean onInterceptTouchEvent(MotionEvent event) {
	//	int rawX = (int) event.getRawX();
	//	int rawY = (int) event.getRawY();
	//	Log.e("xxxx","action:"+event.getAction());
	//	switch (event.getAction() & MotionEvent.ACTION_MASK) {
	//		case MotionEvent.ACTION_DOWN:
	//			Log.e("xxx","actionDown");
	//			this.setAlpha(1f);
	//			setPressed(true);
	//			isDrag = false;
	//			getParent().requestDisallowInterceptTouchEvent(true);
	//			lastX = rawX;
	//			lastY = rawY;
	//			if (getParent() != null) {
	//				parent = (ViewGroup) getParent();
	//				parentHeight = parent.getHeight();
	//				parentWidth = parent.getWidth();
	//			}
	//			break;
	//		case MotionEvent.ACTION_MOVE:
	//			Log.e("xxx","actionMove");
	//			if (parentHeight <= 0.2 || parentWidth <= 0.2) {
	//				isDrag = false;
	//				break;
	//			} else {
	//				isDrag = true;
	//			}
	//			this.setAlpha(1f);
	//			int dx = rawX - lastX;
	//			int dy = rawY - lastY;
	//			//这里修复一些华为手机无法触发点击事件
	//			int distance = (int) Math.sqrt(dx * dx + dy * dy);
	//			if (distance == 0) {
	//				isDrag = false;
	//				break;
	//			}
	//			float x = getX() + dx;
	//			float y = getY() + dy;
	//			//检测是否到达边缘 左上右下
	//			x = x < 0 ? 0 : x > parentWidth - getWidth() ? parentWidth - getWidth() : x;
	//			y = getY() < 0 ? 0 : getY() + getHeight() > parentHeight ? parentHeight - getHeight() : y;
	//			setX(x);
	//			setY(y);
	//			lastX = rawX;
	//			lastY = rawY;
	//			Log.i("aa", "isDrag=" + isDrag + "getX=" + getX() + ";getY=" + getY() + ";parentWidth=" + parentWidth);
	//			break;
	//		case MotionEvent.ACTION_UP:
	//			Log.e("xxx","actionUp");
	//			if (!isNotDrag()) {
	//				//恢复按压效果
	//				setPressed(false);
	//				//Log.i("getX="+getX()+"；screenWidthHalf="+screenWidthHalf);
	//				moveHide(rawX);
	//			} else {
	//				myRunable();
	//			}
	//			break;
	//		default:
	//			break;
	//	}
	//	//如果是拖拽则消s耗事件，否则正常传递即可。
	//	return !isNotDrag() || super.onInterceptTouchEvent(event);
	//}



	@Override
    public boolean onTouchEvent(MotionEvent event) {
        int rawX = (int) event.getRawX();
        int rawY = (int) event.getRawY();
        Log.e("xxxx","action:"+event.getAction());
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
	            Log.e("xxx","actionDown");
                this.setAlpha(1f);
                setPressed(true);
                isDrag = false;
                getParent().requestDisallowInterceptTouchEvent(true);
                lastX = rawX;
                lastY = rawY;
                if (getParent() != null) {
                    parent = (ViewGroup) getParent();
                    parentHeight = parent.getHeight();
                    parentWidth = parent.getWidth();
                }
                break;
            case MotionEvent.ACTION_MOVE:
            	Log.e("xxx","actionMove");
                if (parentHeight <= 0.2 || parentWidth <= 0.2) {
                    isDrag = false;
                    break;
                } else {
                    isDrag = true;
                }
                this.setAlpha(1f);
                int dx = rawX - lastX;
                int dy = rawY - lastY;
                //这里修复一些华为手机无法触发点击事件
                int distance = (int) Math.sqrt(dx * dx + dy * dy);
                if (distance == 0) {
                    isDrag = false;
                    break;
                }
                float x = getX() + dx;
                float y = getY() + dy;
                //检测是否到达边缘 左上右下
                x = x < 0 ? 0 : x > parentWidth - getWidth() ? parentWidth - getWidth() : x;
                y = getY() < 0 ? 0 : getY() + getHeight() > parentHeight ? parentHeight - getHeight() : y;
                setX(x);
                setY(y);
                lastX = rawX;
                lastY = rawY;
                Log.i("aa", "isDrag=" + isDrag + "getX=" + getX() + ";getY=" + getY() + ";parentWidth=" + parentWidth);
                break;
            case MotionEvent.ACTION_UP:
	            Log.e("xxx","actionUp");
                if (!isNotDrag()) {
                    //恢复按压效果
                    setPressed(false);
                    //Log.i("getX="+getX()+"；screenWidthHalf="+screenWidthHalf);
                    moveHide(rawX);
                } else {
                    myRunable();
                }
                break;
	        default:
	        	break;
        }
        //如果是拖拽则消s耗事件，否则正常传递即可。
         return !isNotDrag() || super.onTouchEvent(event);
    }

    private boolean isNotDrag() {
        return !isDrag && (getX() == 0
                || (getX() == parentWidth - getWidth()));
    }

    private void moveHide(int rawX) {
        if (rawX >= parentWidth / 2) {
            //靠右吸附
            animate().setInterpolator(new DecelerateInterpolator())
                    .setDuration(500)
                    .xBy(parentWidth - getWidth() - getX())
                    .start();
            myRunable();
        } else {
            //靠左吸附
            ObjectAnimator oa = ObjectAnimator.ofFloat(this, "x", getX(), 0);
            oa.setInterpolator(new DecelerateInterpolator());
            oa.setDuration(500);
            oa.start();
            myRunable();

        }
    }

    private void myRunable() {
        handler.removeCallbacks(runnable);
        handler.postDelayed(runnable, 2000);
    }




}
