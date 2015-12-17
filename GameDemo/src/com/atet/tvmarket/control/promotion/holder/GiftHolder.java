package com.atet.tvmarket.control.promotion.holder;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;

import com.atet.common.logging.ALog;
import com.atet.tvmarket.R;
import com.atet.tvmarket.common.cache.ImageFetcher;
import com.atet.tvmarket.control.home.inf.OnRecyItemClickListener;
import com.atet.tvmarket.control.home.view.GiftPanel;
import com.atet.tvmarket.control.mygame.utils.QRUtil;
import com.atet.tvmarket.control.promotion.activity.GiftActivity;
import com.atet.tvmarket.control.promotion.adapter.GiftAdapter;
import com.atet.tvmarket.control.promotion.view.MyButton;
import com.atet.tvmarket.entity.dao.GameGiftInfo;
import com.atet.tvmarket.entity.dao.UserGameGiftInfo;
import com.atet.tvmarket.utils.UIUtils;

/*
 * File：GiftHolder.java
 *
 * Copyright (C) 2015 MainActivity Project
 * Date：2015年6月10日 下午8:10:30
 * All Rights SXHL(New Space) Corporation Reserved.
 * http://www.at-et.cn
 *
 */
public class GiftHolder extends ViewHolder implements OnClickListener,
		OnKeyListener {
	ALog alog = ALog.getLogger(GiftActivity.class);
	public GiftPanel panel;
	private RecyclerView recyclerView;
	private OnRecyItemClickListener mListener;
	private ImageFetcher imageFetcher;
	private RecycleOnFocusChangeListener recycleOnFocusChangeListener;
	private RecycleOnTouchListener recycleOnTouchListener;
	private GiftActivity context;
	private GameGiftInfo gameGiftInfo;
	private List<UserGameGiftInfo> gifts = new ArrayList<UserGameGiftInfo>();
	private Integer[] mGreenButton = { R.color.green_bg,R.drawable.green_focus, R.drawable.green_click};
	private Integer[] mGrayButton = { R.color.bt_gift_received_color,R.drawable.bt_gray_focus, R.drawable.bt_gray_click};
	private MyButton myButton;
	private boolean isReceived;
	
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			
			String gameid = gameGiftInfo.getGameid();
			if(gifts.size() != 0){
				for (int i = 0; i < gifts.size(); i++) {
					UserGameGiftInfo userGameGiftInfo = gifts.get(i);
					String giftGameId = gifts.get(i).getGameid();
					if(gameid.equals(giftGameId)){
						if(userGameGiftInfo.getUserGameToGiftList().size() >=
								gameGiftInfo.getGiftData().size()){
							if(!isReceived){
								isReceivedGift();
							}
							isReceived = true;
						}else{
							panel.gift_top_bt.setBackgroundDrawable(myButton.setBg(mGreenButton));
						}
					}
				}
			}
		};
	};
	
	public void setData(GameGiftInfo gameGiftInfo2,UserGameGiftInfo userGameGiftInfo) {
		
		if(userGameGiftInfo.getUserGameToGiftList().size() >= gameGiftInfo2.getGiftData().size()){
			if(!isReceived){
				isReceivedGift();
			}
			isReceived = true;
		}
	}
	
	private void isReceivedGift(){
		panel.gift_top_bt.setText(R.string.gift_item_received);
		panel.gift_top_bt.setBackgroundDrawable(myButton.setBg(mGrayButton));
		
		panel.getGift_top_iv().setDrawingCacheEnabled(true);
		//panel.getGift_top_iv().buildDrawingCache();
		
		Bitmap grayscale = QRUtil.toGrayscale(panel.getGift_top_iv().getDrawingCache());
		panel.getGift_top_iv().setImageBitmap(grayscale);
		panel.getGift_top_iv().setDrawingCacheEnabled(false);
	}

	public GiftHolder(View itemView, RecyclerView recyclerView,
			OnRecyItemClickListener listener, GiftActivity context) {
		super(itemView);
		panel = (GiftPanel) itemView;
		this.mListener = listener;
		this.recyclerView = recyclerView;
		this.context = context;
		initView();
	}

	private void initView() {
		myButton = new MyButton(context);
		imageFetcher = new ImageFetcher();
		recycleOnFocusChangeListener = new RecycleOnFocusChangeListener();
		recycleOnTouchListener = new RecycleOnTouchListener();

		panel.getGift_top_bt().setOnFocusChangeListener(recycleOnFocusChangeListener);
		panel.getGift_top_bt().setOnTouchListener(recycleOnTouchListener);
		panel.getGift_top_bt().setOnKeyListener(this);
		panel.getGift_top_bt().setOnClickListener(this);
	}

	public void setData(GameGiftInfo gameGiftInfo, List<UserGameGiftInfo> userGifts) {
		this.gameGiftInfo = gameGiftInfo;
		gifts.clear();
		gifts.addAll(userGifts);
		
		imageFetcher.loadGiftImage(gameGiftInfo.getMinPhoto(), panel.getGift_top_iv(), 
				R.drawable.gift_default_bg,mHandler);
		panel.getGift_top_name().setText(gameGiftInfo.getGameName());
		//panel.gift_top_bt.setBackgroundDrawable(myButton.setbg(mBlueButton));
		alog.info("giftholder" + gameGiftInfo.getGiftData());
	}	
	
	class RecycleOnFocusChangeListener implements OnFocusChangeListener {
		private View view;

		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			if (hasFocus) {
				/*
				 * int parentWidth = ((View)v.getParent()).getWidth(); int[]
				 * location = new int[2]; v.getLocationOnScreen(location);
				 * 
				 * if(location[0] - v.getWidth() < recyclerView.getLeft()){
				 * recyclerView.smoothScrollBy(-parentWidth * 2 , 0); }
				 * 
				 * if(location[0] + parentWidth > recyclerView.getRight()){
				 * recyclerView.smoothScrollBy(parentWidth * 2, 0); }
				 */

				if (v.isInTouchMode() && v == view) {
					v.performClick();
				}

				/*
				 * if(v.getId() == panel.getGift_top_bt().getId()){
				 * panel.getGift_top_bt
				 * ().setBackgroundResource(R.drawable.common_btn_focus);
				 * 
				 * }
				 */
			} else {
				/*
				 * if(v.getId() == panel.getGift_top_bt().getId()){
				 * panel.getGift_top_bt
				 * ().setBackgroundColor(UIUtils.getColor(R.color.blue_bg));
				 * 
				 * }
				 */
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

	@Override
	public void onClick(View v) {
		if (mListener != null) {
			mListener.onItemClick(v, getPosition());
		}
	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		int itemCount = recyclerView.getAdapter().getItemCount();

		if (event.getAction() == KeyEvent.ACTION_DOWN) {
			int pos = (Integer) v.getTag();
			if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
				if (itemCount == 8 && pos >= 6) {
					context.nextPage();
					GiftAdapter.lastFocusView = null;
					return true;
				} else {
					int col = 0;
					if (itemCount % 2 == 0) {
						col = itemCount / 2;
					} else {
						col = itemCount / 2 + 1;
					}
					if ((col == 1 && pos >= 0) || (col == 2 && pos >= 2)
							|| (col == 3 && pos >= 4) || (col == 4 && pos >= 6)) {
						context.nextPage();
						GiftAdapter.lastFocusView = null;
						return true;
					}
				}
			} else if (pos <= 1 && keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
				context.previousPage();
				GiftAdapter.lastFocusView = null;
				return true;
			} else if (pos == recyclerView.getChildCount() - 1
					&& keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
				context.box_bt.requestFocus();
				return true;
			}

		}
		return false;
	}

}
