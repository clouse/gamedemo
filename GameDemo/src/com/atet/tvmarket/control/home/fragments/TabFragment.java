package com.atet.tvmarket.control.home.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.atet.tvmarket.control.common.BaseFragment;

/*
 * File：TabFragment.java
 *
 * Copyright (C) 2015 MainActivity Project
 * Date：2015年7月13日 下午3:47:58
 * All Rights SXHL(New Space) Corporation Reserved.
 * http://www.at-et.cn
 *
 */
public abstract class TabFragment extends BaseFragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		return initView();
	}
	
	protected abstract View initView();
	
}
