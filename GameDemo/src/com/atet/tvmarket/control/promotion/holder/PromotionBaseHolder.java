package com.atet.tvmarket.control.promotion.holder;

import com.atet.tvmarket.common.cache.ImageFetcher;
import com.atet.tvmarket.utils.ScaleViewUtils;

import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;

/*
 * File：PromotionBaseHolder.java
 *
 * Copyright (C) 2015 MainActivity Project
 * Date：2015年7月14日 上午11:54:24
 * All Rights SXHL(New Space) Corporation Reserved.
 * http://www.at-et.cn
 *
 */
public abstract class PromotionBaseHolder extends ViewHolder{
	protected ImageFetcher mImageFetcher;
	public PromotionBaseHolder(View itemView) {
		super(itemView);
		ScaleViewUtils.scaleView(itemView);
		mImageFetcher = new ImageFetcher();
		initView();
	}

	protected abstract void initView();
	
	protected void initData() {
		
	}
}
