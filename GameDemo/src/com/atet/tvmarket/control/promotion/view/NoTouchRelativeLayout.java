package com.atet.tvmarket.control.promotion.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

/*
 * File：NoTouchRelativeLayout.java
 *
 * Copyright (C) 2015 MainActivity Project
 * Date：2015年6月17日 下午3:46:58
 * All Rights SXHL(New Space) Corporation Reserved.
 * http://www.at-et.cn
 *
 */
public class NoTouchRelativeLayout extends RelativeLayout{

	public NoTouchRelativeLayout(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public NoTouchRelativeLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public NoTouchRelativeLayout(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
		// TODO Auto-generated method stub
		getParent().requestDisallowInterceptTouchEvent(false);
	}
}
