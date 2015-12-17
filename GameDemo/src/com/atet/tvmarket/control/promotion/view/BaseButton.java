package com.atet.tvmarket.control.promotion.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Button;

/*
 * File：BaseButton.java
 *
 * Copyright (C) 2015 MainActivity Project
 * Date：2015年6月17日 下午4:06:20
 * All Rights SXHL(New Space) Corporation Reserved.
 * http://www.at-et.cn
 *
 */
public class BaseButton extends Button {

	public BaseButton(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public BaseButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public BaseButton(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
	}
	

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		return super.onTouchEvent(event);
	}
}
