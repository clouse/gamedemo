package com.atet.tvmarket.control.promotion.holder;

import java.util.List;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.widget.Button;

import com.atet.tvmarket.R;
import com.atet.tvmarket.common.cache.ImageFetcher;
import com.atet.tvmarket.control.home.inf.OnRecyItemClickListener;
import com.atet.tvmarket.control.promotion.activity.GiftDetailActivity;
import com.atet.tvmarket.control.promotion.view.MyButton;
import com.atet.tvmarket.entity.dao.GiftInfo;
import com.atet.tvmarket.entity.dao.UserGameGiftInfo;
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
public class GiftDetailHolder extends ViewHolder implements OnClickListener {
	
	private OnRecyItemClickListener mListener;
	private RecycleOnFocusChangeListener recycleOnFocusChangeListener;
	private RecycleOnTouchListener recycleOnTouchListener;
	private RecyclerView recyclerView;
	public BaseImageView game_icon;
	public CloseAcceTextView gift_name;
	public CloseAcceTextView gift_count;
	public CloseAcceTextView gift_time;
	public CloseAcceTextView gift_reward;
	public Button gift_received_bt;
	private GiftDetailActivity context;
	private MyButton myButton;
	private Integer[] mGrayButton = { R.color.bt_gift_received_color,R.drawable.bt_gray_focus, R.drawable.bt_gray_click};
	private boolean isNull = false;
	
	
	public GiftDetailHolder(View itemView, OnRecyItemClickListener listener,
			RecyclerView recyclerView, GiftDetailActivity context) {
		super(itemView);
		this.context = context;
		this.mListener = listener;
		this.recyclerView = recyclerView;
		initView();
	}

	private void initView() {
		ScaleViewUtils.scaleView(itemView);
		myButton = new MyButton(context);
		recycleOnFocusChangeListener = new RecycleOnFocusChangeListener();
		recycleOnTouchListener = new RecycleOnTouchListener();
		gift_received_bt = (Button) itemView.findViewById(R.id.gift_received_bt);
		game_icon = (BaseImageView) itemView.findViewById(R.id.gift_game_iv);
		gift_name = (CloseAcceTextView) itemView.findViewById(R.id.gift_receiced_name);
		gift_count = (CloseAcceTextView) itemView.findViewById(R.id.gift_receiced_count);
		gift_time = (CloseAcceTextView) itemView.findViewById(R.id.gift_receiced_date);
		gift_reward = (CloseAcceTextView) itemView.findViewById(R.id.gift_receiced_content);
		
		gift_received_bt.setOnFocusChangeListener(recycleOnFocusChangeListener);
		gift_received_bt.setOnClickListener(this);
		gift_received_bt.setOnTouchListener(recycleOnTouchListener);
		
	}

	public void setData(GiftInfo giftinfo, List<UserGameToGift> giftList) {
		
		for (int i = 0; i < giftList.size(); i++) {
			UserGameToGift userGameToGift = giftList.get(i);
			if(giftinfo.getGiftPackageid().equals(userGameToGift.getGiftPackageid())){
				gift_received_bt.setClickable(false);
				gift_received_bt.setText(R.string.gift_item_received);
				//gift_received_bt.setBackgroundColor(UIUtils.getColor(R.color.bt_gift_received_color));
				gift_received_bt.setBackgroundDrawable(myButton.setBg(mGrayButton));
			}else{
				if(isNull){
					gift_received_bt.setClickable(false);
					gift_received_bt.setText(R.string.gift_all_received);
					//gift_received_bt.setBackgroundColor(UIUtils.getColor(R.color.bt_gift_received_color));
					gift_received_bt.setBackgroundDrawable(myButton.setBg(mGrayButton));
				}
			}
		}
		
		gift_name.setText(UIUtils.getString(R.string.gift_name) + giftinfo.getName());
		gift_count.setText(UIUtils.getString(R.string.gift_count) + giftinfo.getNumber());
		gift_time.setText(UIUtils.getString(R.string.gift_time) + UIUtils.changeTimeStyle(giftinfo.getStartTime()) + "-"
				+ UIUtils.changeTimeStyle(giftinfo.getEndTime()));
		gift_reward.setText(UIUtils.getString(R.string.gift_content) + giftinfo.getRemark());
		
	}

	@Override
	public void onClick(View view) {
		if (mListener != null) {
			mListener.onItemClick(view, getPosition());
		}
	}

	class RecycleOnFocusChangeListener implements OnFocusChangeListener {
		private View view;

		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			if (hasFocus) {

				int parentHeight = ((View) v.getParent()).getHeight();
				int[] location = new int[2];
				v.getLocationOnScreen(location);
				// 控制recyclerVIew的滑动
				if (location[1] - v.getHeight() * 2 < recyclerView.getTop()) {
					recyclerView.smoothScrollBy(0, -(parentHeight));
				}

				if (location[1] + parentHeight > recyclerView.getBottom()) {
					recyclerView.smoothScrollBy(0, parentHeight);
				}

				if (v.isInTouchMode() && v == view) {

					v.performClick();
				}

				/*if (v.getId() == gift_received_bt.getId()) {
					gift_received_bt.setBackgroundColor(UIUtils
							.getColor(R.color.game_cneter_game_search_bg));
				}*/
			} else {
				/*if (v.getId() == gift_received_bt.getId()) {
					gift_received_bt.setBackgroundColor(UIUtils
							.getColor(R.color.game_cneter_quick_start_bg));
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
				RecycleOnFocusChangeListener listener = (RecycleOnFocusChangeListener) v
						.getOnFocusChangeListener();
				if (listener != null
						&& listener instanceof RecycleOnFocusChangeListener) {
					((RecycleOnFocusChangeListener) listener).setView(v);
				}
			}
			return false;
		}
	}

}
