package com.atet.tvmarket.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

public class TvRecyclerView extends RecyclerView {

	// 上次点击方向键右键的时间
	private long lastFocusRightTime = 0;
	private OnUnHandleMoveLisenter lisenter;
	
	public void setLisenter(OnUnHandleMoveLisenter lisenter) {
		this.lisenter = lisenter;
	}
	
	public TvRecyclerView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		setChildrenDrawingOrderEnabled(true);
	}

	public TvRecyclerView(Context context) {
		this(context,null);
		// TODO Auto-generated constructor stub
	}

	public TvRecyclerView(Context context, AttributeSet attrs) {
		this(context, attrs,0);
		// 允许改变子view的绘制顺序
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
	public boolean dispatchUnhandledMove(View focused, int direction) {
		Log.i("life", "unhandleMove:view->"+focused.toString()+",direction->"+direction);
		if(lisenter!=null){
			lisenter.onUnHandleMove(focused,direction);
		}
		return super.dispatchUnhandledMove(focused, direction);
	}
	
	public interface OnUnHandleMoveLisenter{
		public void onUnHandleMove(View focused, int direction);
	}
}
