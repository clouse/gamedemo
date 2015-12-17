package com.atet.tvmarket.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

public class NoScrollTvRecyclerView extends RecyclerView {

	// 上次点击方向键右键的时间
	private long lastFocusRightTime = 0;

	public NoScrollTvRecyclerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// 允许改变子view的绘制顺序
		setChildrenDrawingOrderEnabled(true);
	}

	/**
	 * 改变子view的绘制顺序，聚焦的View，放大后能够层叠在其他子View的上方。
	 * 
	 * @author zhaominglai
	 * @date 2014-12-30
	 * 
	 * */
	@Override
	protected int getChildDrawingOrder(int childCount, int i) {
		if (hasFocus()) {
			View child = getFocusedChild();

			if (child != null) {
				int index = indexOfChild(child);
				if (index != -1) {

					if (i < index) {
						return i;
					} else {
						return childCount - 1 - i + index;
					}
				}
			} else {
				return i;
			}

		}

		return super.getChildDrawingOrder(childCount, i);

	}
	
	
	/**
	 * 设置手柄按键的速度
	 */
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		// TODO Auto-generated method stub
		/*int action = event.getAction();
		int keyCode = event.getKeyCode();
		View nextFocus = null;

		if (action == KeyEvent.ACTION_DOWN) {

			if (keyCode == KeyEvent.KEYCODE_DPAD_UP
					|| keyCode == KeyEvent.KEYCODE_DPAD_DOWN
					|| keyCode == KeyEvent.KEYCODE_DPAD_LEFT
					|| keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
				
				View child = getFocusedChild();
				if (child != null) {
					//判断是上下移动还是左右移动
					if(keyCode == KeyEvent.KEYCODE_DPAD_UP
							|| keyCode == KeyEvent.KEYCODE_DPAD_DOWN){
						
						nextFocus= child.focusSearch(FOCUS_UP);
					}else{
						nextFocus = child.focusSearch(FOCUS_RIGHT);
					}
					if (nextFocus != null) {
						long curTime = System.currentTimeMillis();
						// 判断这次时间与上次按下的时间之差是不是小于200ms
						if (curTime - lastFocusRightTime < 200) {
							return true;
						}

						lastFocusRightTime = curTime;
					}
				}
			}

		}*/
		return super.dispatchKeyEvent(event);
	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev)
	{
		return false;
	}
	 
	@Override
	public boolean onTouchEvent(MotionEvent ev)
	{
		return false;
	}
}
