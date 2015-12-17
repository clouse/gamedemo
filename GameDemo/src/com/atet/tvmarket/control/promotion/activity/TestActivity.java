package com.atet.tvmarket.control.promotion.activity;

import com.atet.tvmarket.R;
import com.atet.tvmarket.utils.ScaleViewUtils;

import android.app.Activity;
import android.os.Bundle;
/*
 * File：TestActivity.java
 *
 * Copyright (C) 2015 DeviceIdActivity Project
 * Date：2015年7月18日 下午1:46:58
 * All Rights SXHL(New Space) Corporation Reserved.
 * http://www.at-et.cn
 *
 */
public class TestActivity extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.integral_detail_item3);
		ScaleViewUtils.init(this);
		ScaleViewUtils.scaleView(getWindow().getDecorView());
	}
}
