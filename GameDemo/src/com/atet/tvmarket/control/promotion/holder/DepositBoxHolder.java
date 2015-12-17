package com.atet.tvmarket.control.promotion.holder;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.text.ClipboardManager;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.TextView;

import com.atet.tvmarket.R;
import com.atet.tvmarket.common.cache.ImageFetcher;
import com.atet.tvmarket.control.home.inf.OnRecyItemClickListener;
import com.atet.tvmarket.entity.dao.UserGameToGift;
import com.atet.tvmarket.utils.ScaleViewUtils;
import com.atet.tvmarket.utils.UIUtils;
import com.atet.tvmarket.view.BaseImageView;
import com.atet.tvmarket.view.CloseAcceTextView;

/*
 * File：GiftReceivedHolder.java
 *
 * Copyright (C) 2015 MainActivity Project
 * Date：2015年6月11日 下午5:45:03
 * All Rights SXHL(New Space) Corporation Reserved.
 * http://www.at-et.cn
 *
 */
public class DepositBoxHolder extends ViewHolder implements 
			OnClickListener, OnKeyListener {
	public BaseImageView gift_icon;
	public CloseAcceTextView gift_name;
	public CloseAcceTextView gift_count;
	public TextView gift_reward;
	public Button deposit_box_copy;
	private OnRecyItemClickListener mListener;
	private RecycleOnFocusChangeListener recycleOnFocusChangeListener;
	private RecycleOnTouchListener recycleOnTouchListener;
	private RecyclerView recyclerView;
	private ImageFetcher mImageFetcher;
	private UserGameToGift userGameToGift;
	
	public DepositBoxHolder(View itemView, OnRecyItemClickListener listener,RecyclerView recyclerView) {
		super(itemView);
		this.recyclerView = recyclerView;
		this.mListener = listener;
		initView();
	}

	private void initView() {
		ScaleViewUtils.scaleView(itemView);
		recycleOnFocusChangeListener = new RecycleOnFocusChangeListener();
		recycleOnTouchListener = new RecycleOnTouchListener();
		mImageFetcher = new ImageFetcher();
		
		gift_icon = (BaseImageView) itemView.findViewById(R.id.deposit_box_iv);
		gift_name = (CloseAcceTextView) itemView.findViewById(R.id.deposit_box_name);
		gift_count = (CloseAcceTextView) itemView.findViewById(R.id.deposit_box_count);
		gift_reward = (TextView) itemView.findViewById(R.id.deposit_box_content);
		deposit_box_copy = (Button) itemView.findViewById(R.id.deposit_box_copy);
		deposit_box_copy.setOnFocusChangeListener(recycleOnFocusChangeListener);
		deposit_box_copy.setOnClickListener(this);
		deposit_box_copy.setOnTouchListener(recycleOnTouchListener);
		deposit_box_copy.setOnKeyListener(this);
		
	}
	
	public void setData(UserGameToGift userGameToGift) {
		this.userGameToGift = userGameToGift;
		mImageFetcher.loadImage(userGameToGift.getUserGiftInfo().getIcon(), gift_icon, 
				R.drawable.gift_default_bg);
		gift_name.setText(UIUtils.getString(R.string.gift_name) + userGameToGift.getUserGiftInfo().getName());
		gift_count.setText(UIUtils.getString(R.string.gift_code)+ userGameToGift.getUserGiftInfo().getGiftCode());
		gift_reward.setText(UIUtils.getString(R.string.gift_content) + userGameToGift.getUserGiftInfo().getContent());
		
	}
	
	@Override
	public void onClick(View view) {
		if(mListener != null){
			mListener.onItemClick(view, getPosition());
		}
	}


	class RecycleOnFocusChangeListener implements OnFocusChangeListener {
		private View view;

		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			if (hasFocus) {
				
				int parentHeight = ((View)v.getParent()).getHeight();
				int[] location = new int[2];
				v.getLocationOnScreen(location);
				//控制recyclerVIew的滑动
				if(location[1] - v.getHeight() * 2< recyclerView.getTop()){
					recyclerView.smoothScrollBy(0 , - (parentHeight));
				}
				
				if(location[1] + parentHeight > recyclerView.getBottom()){
					recyclerView.smoothScrollBy( 0, parentHeight);
				}
				
				if (v.isInTouchMode() && v == view) {
					v.performClick();
				}
				
				/*if(v.getId() == R.id.deposit_box_copy){
					deposit_box_copy.setBackgroundResource(R.drawable.bt_blue_focus);
				}*/
			}else{
				/*if(v.getId() == R.id.deposit_box_copy){
					deposit_box_copy.setBackgroundColor(UIUtils.getColor(R.color.game_cneter_quick_start_bg));
				}*/
			}
		}

		public View getView() {
			return view;
		}

		public void setView(View view) {
			this.view = view;
		}
	}
	
	class RecycleOnTouchListener implements OnTouchListener {
		   @Override
		    public boolean onTouch(View v, MotionEvent event) {
		     if (event.getAction() == MotionEvent.ACTION_UP) {
		             RecycleOnFocusChangeListener listener = (RecycleOnFocusChangeListener) v.getOnFocusChangeListener();
		             if (listener != null && listener instanceof RecycleOnFocusChangeListener) {
		                    ((RecycleOnFocusChangeListener) listener).setView(v);
		              }
		      }
		      return false;
		    }
		 }

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		int action = event.getAction();
		if(action == KeyEvent.ACTION_DOWN && 
				keyCode == KeyEvent.KEYCODE_DPAD_LEFT && 
				keyCode == KeyEvent.KEYCODE_DPAD_RIGHT){
			return true;
		}
		return false;
	}
}
