package com.atet.tvmarket.control.promotion.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.webkit.WebView;

/*
 * File：NoFocusWebView.java
 *
 * Copyright (C) 2015 MainActivity Project
 * Date：2015年7月8日 下午7:16:54
 * All Rights SXHL(New Space) Corporation Reserved.
 * http://www.at-et.cn
 *
 */
public class NoFocusWebView extends WebView {

	public NoFocusWebView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public NoFocusWebView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public NoFocusWebView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		// TODO Auto-generated method stub
		return false;
	}
	
}
