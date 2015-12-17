package com.atet.tvmarket.control.mygame.view;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.RecycledViewPool;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;

/**
 * 我的游戏RecyclerView
 */
public class MyGameRecyclerView extends RecyclerView {
private MoveFrame mFrame;
	
	//上次点击方向键右键的时间
	private long lastFocusRightTime = 0;
	
	private Rect prevFocusedRect;
	
	private FocusEscapeListener focusEscaplistener;
	
	private View preFocusView;
	
	public MyGameRecyclerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		prevFocusedRect = new Rect();

		//允许改变子view的绘制顺序
		setChildrenDrawingOrderEnabled(true);
	}
	
	

	public void setMoveFrame(MoveFrame m){
		this.mFrame = m;
	}
	
	


	/**
	 * 改变子view的绘制顺序，聚焦的View，放大后能够层叠在其他子View的上方。
	 * @author zhaominglai
	 * @date 2014-12-30
	 * 
	 * */
	@Override
	protected int getChildDrawingOrder(int childCount, int i) {
		
		if(hasFocus()){
			View child = getFocusedChild();
			
			if (child != null){
				int index = indexOfChild(child);
				
				if (index != -1){
					
					if (i < index){
						return i;
					}else {
						return childCount - 1 - i + index;
					}
				}
			}else{
				return i;
			}
			
		}
			
		return super.getChildDrawingOrder(childCount, i);
		
	}
	
	
	
	/**
	 * 为了解决焦点丢失的情况，屏蔽用户在200ms内连续按向右的方向键
	 * @author zhaominglai
	 * @date 2014-12-30
	 * 
	 * */
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		// TODO Auto-generated method stub
		int action = event.getAction();	
		
		if (action == KeyEvent.ACTION_DOWN){
			
			if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT){
				
				View child = getFocusedChild();{
					
					if (child != null){
					
						View nextFocus = child.focusSearch(FOCUS_RIGHT);
						
						if (nextFocus != null){
							
							long curTime = System.currentTimeMillis();
							//判断这次时间与上次按下的时间之差是不是小于200ms
							if (curTime-lastFocusRightTime < 200){
								return true;
							}
							
							lastFocusRightTime = curTime;
						}
					}
				}
				
			}
			
		}
		return super.dispatchKeyEvent(event);
	}
	
	

	@Override
	public boolean dispatchUnhandledMove(View focused, int direction) {
		
		int[] location = new int[2];
		focused.getLocationOnScreen(location);
		prevFocusedRect.set(location[0],location[1], location[0]+focused.getWidth(),location[1]+getHeight());
		
		preFocusView = (View) focused.getParent().getParent();
		if (focusEscaplistener != null && getTag()!=null){
			focusEscaplistener.escape((Integer)getTag(), direction);
		}
		
//		Log.e("tvrecyclerview dispatchunhandlemove ",focused.toString()+"  direction "+direction);
		return super.dispatchUnhandledMove(focused, direction);
	}
	
	public FocusEscapeListener getFocusEscaplistener() {
		return focusEscaplistener;
	}


	public void setFocusEscaplistener(FocusEscapeListener focusEscaplistener) {
		this.focusEscaplistener = focusEscaplistener;
	}

	/**
	 * 移动聚焦白板接口，暂时保留不实现
	 * 
	 * @author zhaominglai
	 * @date 2014-12-30
	 * 
	 * */
	public interface MoveFrame{
		void move(int l,int t,int w,int h);
	}
	
	/**
	 * 焦点逸出监听器，当焦点从TvRecyclerView四个方向逸出时会触发
	 * 
	 * 外层的Fragament或者是Actvity一定需要实现这个接口
	 * 
	 * @author zhaominglai
	 * @date 2015-1-12
	 * 
	 * */
	public interface FocusEscapeListener{
		
		/**
		 * 当有焦点从TvRecyclerView中逃逸时触发。
		 * @param id 自定义id,用来标志某个TvRecyclerView
		 * @param direct 焦点逃逸的方向  FOCUS_UP,FOCUS_DOWN,FOCUS_LEFT,FOCUS_RIGHT　
		 * 
		 * */
		void escape(int id,int direction);
	}
}
