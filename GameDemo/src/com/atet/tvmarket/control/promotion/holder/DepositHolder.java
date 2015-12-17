package com.atet.tvmarket.control.promotion.holder;

import java.io.Serializable;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.widget.RelativeLayout;

import com.atet.common.logging.ALog;
import com.atet.tvmarket.R;
import com.atet.tvmarket.app.Constant;
import com.atet.tvmarket.common.cache.ImageFetcher;
import com.atet.tvmarket.control.promotion.activity.DepositActivity;
import com.atet.tvmarket.control.promotion.activity.DepositBoxActivity;
import com.atet.tvmarket.control.promotion.adapter.GiftAdapter;
import com.atet.tvmarket.entity.dao.UserGameGiftInfo;
import com.atet.tvmarket.entity.dao.UserGameToGift;
import com.atet.tvmarket.utils.ScaleViewUtils;
import com.atet.tvmarket.view.BaseImageView;
import com.atet.tvmarket.view.CloseAcceTextView;

/*
 * File：DepositHolder.java
 *
 * Copyright (C) 2015 MainActivity Project
 * Date：2015年7月9日 下午3:20:16
 * All Rights SXHL(New Space) Corporation Reserved.
 * http://www.at-et.cn
 *
 */
public class DepositHolder extends ViewHolder implements
		OnClickListener, OnKeyListener {
	ALog alog = ALog.getLogger(DepositHolder.class);
	private ImageFetcher mImageFetcher;
	public RelativeLayout deposit_item;
	public BaseImageView deposit_item_iv;
	public BaseImageView deposit_item_shadow;
	public CloseAcceTextView item_name;
	private RecyclerView recyclerView;
	private DepositActivity context;
	private UserGameGiftInfo giftInfo;

	public DepositHolder(View itemView ,RecyclerView recyclerView,
			DepositActivity context) {
		super(itemView);
		this.recyclerView = recyclerView;
		this.context = context;
		initView();
	}

	private void initView() {
		ScaleViewUtils.scaleView(itemView);
		mImageFetcher = new ImageFetcher();
		deposit_item = (RelativeLayout) itemView.findViewById(R.id.deposit_item);
		deposit_item_iv = (BaseImageView) itemView.findViewById(R.id.deposit_item_iv);
		item_name = (CloseAcceTextView) itemView.findViewById(R.id.deposit_item_name);
		deposit_item_shadow = (BaseImageView) itemView.findViewById(R.id.deposit_item_shadow);
		
		
		deposit_item.setOnFocusChangeListener(new FocusChangeListener());
		deposit_item.setOnTouchListener(new TouchListener());
		deposit_item.setOnClickListener(this);
		deposit_item.setOnKeyListener(this);;
	}
	
	public void setData(UserGameGiftInfo giftInfo) {
		this.giftInfo = giftInfo;
		mImageFetcher.loadImage(giftInfo.getMinPhoto(), deposit_item_iv, R.drawable.gift_default_bg);
		alog.info("======================="+giftInfo.getGameName());
		item_name.setText(giftInfo.getGameName());
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
				if (v.getId() == deposit_item.getId()) {
					deposit_item_shadow.setBackgroundResource(R.drawable.game_detail_focus);
					deposit_item_iv.setScaleX(1.125f);
					deposit_item_iv.setScaleY(1.125f);
				}
				
			}else{
				if (v.getId() == deposit_item.getId()) {
					deposit_item_shadow.setBackgroundResource(android.R.color.transparent);
					deposit_item_iv.setScaleX(1.0f);
					deposit_item_iv.setScaleY(1.0f);
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
	
	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		int itemCount = recyclerView.getAdapter().getItemCount();
		
		if(event.getAction()==KeyEvent.ACTION_DOWN){
			int pos = (Integer) v.getTag();
			if(keyCode==KeyEvent.KEYCODE_DPAD_RIGHT){
				if(itemCount == 12 && pos >= 9){
					context.nextPage();
					GiftAdapter.lastFocusView=null;
					return true;
				}
				else{
					
					int col = 0;
					if(itemCount%3==0){
						col=itemCount/3;
					}
					else{
						col = itemCount/3+1;
					}
					if((col==1 && pos>=0)||(col==2 && pos>=3)||(col==3 && pos>=6)||(col==4 && pos>=9)){
						context.nextPage();
						GiftAdapter.lastFocusView=null;
						return true;
					}
				}
			}else if(pos<=2 && keyCode==KeyEvent.KEYCODE_DPAD_LEFT){
				context.previousPage();
				GiftAdapter.lastFocusView=null;
				return true;
			}
		}
		return false;
	} 

	@Override
	public void onClick(View v) {
		v.requestFocus();
		Intent intent = new Intent(context,DepositBoxActivity.class);
		List<UserGameToGift> userGameToGiftList = giftInfo.getUserGameToGiftList();
		alog.info("userGameToGiftList ===" + userGameToGiftList);
		Bundle bundle = new Bundle();
		bundle.putSerializable(Constant.USERGIFTINFO,(Serializable) userGameToGiftList);
		intent.putExtras(bundle);
		context.startActivity(intent);
	}
	
}
