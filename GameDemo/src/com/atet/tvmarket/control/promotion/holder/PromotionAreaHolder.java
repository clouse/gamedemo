package com.atet.tvmarket.control.promotion.holder;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.widget.RelativeLayout;

import com.atet.tvmarket.R;
import com.atet.tvmarket.app.BaseApplication;
import com.atet.tvmarket.common.cache.ImageFetcher;
import com.atet.tvmarket.control.home.inf.OnRecyItemClickListener;
import com.atet.tvmarket.entity.dao.ActInfo;
import com.atet.tvmarket.utils.ScaleViewUtils;
import com.atet.tvmarket.view.BaseImageView;
import com.atet.tvmarket.view.CloseAcceTextView;

/*
 * File：PromotionAreaHolder.java
 *
 * Copyright (C) 2015 MainActivity Project
 * Date：2015年6月10日 下午2:51:50
 * All Rights SXHL(New Space) Corporation Reserved.
 * http://www.at-et.cn
 *
 */
public class PromotionAreaHolder extends ViewHolder implements
		OnClickListener {
	private ImageFetcher mImageFetcher;
	public BaseImageView item_shadow;
	public RelativeLayout item_content;
	public BaseImageView item_reflect;
	public CloseAcceTextView item_name;
	public BaseImageView item_iv;
	private OnRecyItemClickListener mListener;
	private RecyclerView mRecyclerView;

	public PromotionAreaHolder(View itemView, RecyclerView recyclerView,
			OnRecyItemClickListener listener) {
		super(itemView);
		initView();
		this.mListener = listener;
		this.mRecyclerView = recyclerView;
	}

	private void initView() {
		ScaleViewUtils.scaleView(itemView);
		item_shadow = (BaseImageView) itemView
				.findViewById(R.id.promotion_area_item_shadow);
		item_content = (RelativeLayout) itemView
				.findViewById(R.id.promotion_area_item_content);
		item_name = (CloseAcceTextView) itemView
				.findViewById(R.id.promotion_area_item_name);
		item_iv = (BaseImageView) itemView
				.findViewById(R.id.promotion_area_item_iv);

		mImageFetcher = new ImageFetcher();

		item_content.setOnFocusChangeListener(new FocusChangeListener());
		item_content.setOnTouchListener(new TouchListener());
		item_content.setOnClickListener(this);
	}

	public void setData(ActInfo actInfo) {
		mImageFetcher.loadImage(actInfo.getErectPhoto(), item_iv,
				R.drawable.default_vertical);
		item_name.setText(actInfo.getTitle());
	}

	public class FocusChangeListener implements OnFocusChangeListener {

		private View view;

		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			if (hasFocus) {
				
				if (v.isInTouchMode() && v == view) {
					v.performClick();
				} else {
					view = null;
				}
				
				int[] location = new int[2];
				v.getLocationOnScreen(location);

				if (location[0] + v.getWidth() > BaseApplication.mScreenWidth) {
					mRecyclerView.smoothScrollBy(v.getWidth(), 0);
				}

				if (location[0] - v.getWidth() < mRecyclerView.getLeft()) {
					mRecyclerView.smoothScrollBy(-v.getWidth(), 0);
				}

				if (v.getId() == item_content.getId()) {
					item_content.setScaleX(1.1f);
					item_content.setScaleY(1.1f);
					item_shadow.setBackgroundResource(R.drawable.white_focus);;
					
				}
				
			}else{
				if (v.getId() == item_content.getId()) {
					item_shadow.setBackgroundResource(android.R.color.transparent);
					item_content.setScaleX(1.0f);
					item_content.setScaleY(1.0f);
				}
			}
		}
		
		public View getView() {
			return view;
		}
		
		public void setView(View view) {
			this.view = view;
		}
	}
	
	public class TouchListener implements OnTouchListener {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (event.getAction() == MotionEvent.ACTION_UP) {
				OnFocusChangeListener listener = v.getOnFocusChangeListener();
				if (listener != null && listener instanceof FocusChangeListener) {
					((FocusChangeListener) listener).setView(v);
				}
			}
			return false;
		}
	}

	@Override
	public void onClick(View v) {
		if (mListener != null) {
			mListener.onItemClick(v, getPosition());
		}
	}
}
