package com.atet.tvmarket.control.home.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;

/*
 * File：NoFocusViewPager.java
 *
 * Copyright (C) 2015 ATET_MARKET_3.6 Project
 * Date：2015年6月1日 下午8:13:22
 * All Rights SXHL(New Space) Corporation Reserved.
 * http://www.at-et.cn
 *
 */

public class NoFocusViewPager extends ViewPager {

	public NoFocusViewPager(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public NoFocusViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		// TODO Auto-generated method stub
		return false;
	}
	
	/*@Override
	public boolean onInterceptTouchEvent(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return false;
	}*/
	
}
