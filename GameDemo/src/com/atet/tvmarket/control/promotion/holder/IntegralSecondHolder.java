package com.atet.tvmarket.control.promotion.holder;

import java.util.List;

import com.atet.common.logging.ALog;
import com.atet.tvmarket.R;
import com.atet.tvmarket.control.promotion.activity.IntegralDetailsActivity;
import com.atet.tvmarket.control.video.PlayVideoActivity;
import com.atet.tvmarket.entity.dao.GoodsDetailPhoto;
import com.atet.tvmarket.entity.dao.GoodsInfo;
import com.atet.tvmarket.view.BaseImageView;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.widget.RelativeLayout;

/*
 * File：SecondItemHolder.java
 *
 * Copyright (C) 2015 MainActivity Project
 * Date：2015年7月12日 下午5:16:28
 * All Rights SXHL(New Space) Corporation Reserved.
 * http://www.at-et.cn
 *
 */
public class IntegralSecondHolder extends PromotionBaseHolder implements 
				OnFocusChangeListener, OnKeyListener, OnClickListener{
	ALog alog = ALog.getLogger(IntegralSecondHolder.class);
	public BaseImageView top_icon;
	private BaseImageView top_shadow;
	public RelativeLayout rl_top;
	private BaseImageView video_icon;
	
	public BaseImageView bellow_icon;
	private BaseImageView bellow_shadow;
	public RelativeLayout rl_below;
	private RecyclerView item_recyclerView;
	private RecyclerView recyclerView;//最外层的RecyclerView
	private IntegralDetailsActivity context;
	
	private GoodsInfo info;
	private int position;
	
	public IntegralSecondHolder(View itemView, RecyclerView item_recyclerView,
			RecyclerView recyclerView,IntegralDetailsActivity context) {
		super(itemView);
		this.recyclerView = recyclerView;
		this.item_recyclerView = item_recyclerView;
		this.context = context;
		initView();
	}
	
	@Override
	protected void initView() {
		rl_top = (RelativeLayout) itemView.findViewById(R.id.detail_item2_rl_top);
		top_icon = (BaseImageView) itemView.findViewById(R.id.item_recyclerview_top_icon);
		top_shadow = (BaseImageView) itemView.findViewById(R.id.item_recyclerview_top_shadow);
		rl_below = (RelativeLayout) itemView.findViewById(R.id.detail_item2_rl_below);
		bellow_icon = (BaseImageView) itemView.findViewById(R.id.item_recyclerview_bellow_icon);
		bellow_shadow = (BaseImageView) itemView.findViewById(R.id.item_recyclerview_bellow_shadow);
		video_icon = (BaseImageView) itemView.findViewById(R.id.item_recyclerview_video);
		
		rl_top.setOnFocusChangeListener(this);
		rl_top.setOnKeyListener(this);
		rl_top.setOnClickListener(this);
		rl_below.setOnFocusChangeListener(this);
		rl_below.setOnKeyListener(this);
	}
	

	public void setData(GoodsInfo info, int position) {
		this.info = info;
		this.position = position;
		if(info != null){
			List<GoodsDetailPhoto> detailPhotos = info.getDetailPhotos();
			if(info.getVideo() == null || info.getVideo().isEmpty()){
				mImageFetcher.loadImage(detailPhotos.get(position * 2 - 2).getPicture(), top_icon, R.drawable.default_recommand);
				
				if(detailPhotos.size() > position * 2 - 1){
					mImageFetcher.loadImage(detailPhotos.get(position * 2 - 1).getPicture(), bellow_icon, R.drawable.default_recommand);
				}else{
					setBellowClickable();
				}
			}else{
				if (position == 1) {
					mImageFetcher.loadImage(info.getThumbnail(), top_icon,
							R.drawable.default_recommand);
					//NewToast.makeToast(context, "--" + info.getThumbnail(), 0).show();
					if (detailPhotos != null && detailPhotos.size() != 0) {
						mImageFetcher.loadImage(detailPhotos.get(0)
								.getPicture(), bellow_icon,
								R.drawable.default_recommand);
					} else {
						setBellowClickable();
					}
				} else {
					if (detailPhotos != null && detailPhotos.size() > 1) {
						mImageFetcher.loadImage(detailPhotos.get(1)
								.getPicture(), top_icon,
								R.drawable.default_recommand);
						if (detailPhotos.size() > 2) {
							mImageFetcher.loadImage(detailPhotos.get(2)
									.getPicture(), bellow_icon,
									R.drawable.default_recommand);
						} else {
							setBellowClickable();
						}
					}
				}
			}
		}
		
		/*if(info != null){
			List<GoodsDetailPhoto> detailPhotos = info.getDetailPhotos();
			mImageFetcher.loadImage(detailPhotos.get(position * 2 - 2).getPicture(), top_icon, R.drawable.default_recommand);
			
			if(detailPhotos.size() > position * 2 - 1){
				mImageFetcher.loadImage(detailPhotos.get(position * 2 - 1).getPicture(), bellow_icon, R.drawable.default_recommand);
			}else{
				rl_below.setClickable(false);
				rl_below.setFocusable(false);
				rl_below.setFocusableInTouchMode(false);
			}
			
		}*/
		
	}
	private void setBellowClickable(){
		rl_below.setClickable(false);
		rl_below.setFocusable(false);
		rl_below.setFocusableInTouchMode(false);
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if(hasFocus){
			int[] location = new int[2];
			v.getLocationOnScreen(location);
			
			if(IntegralDetailSecondHolder.isLeft){
				if(location[0] - v.getWidth() * 1.1 < 0){
				//	Log.d("lijie","left" + location[0] + "----" + (v.getWidth() *1.2) + "----" + item_recyclerView.getLeft());
					
					item_recyclerView.smoothScrollBy(-(v.getWidth() + 20), 0);
					
				}
			}
			
			if(location[0] + v.getWidth() * 1.1 > item_recyclerView.getRight()){
				//Log.d("lijie","right" + location[0] + "----" + (v.getWidth() *1.2) + "----" + item_recyclerView.getRight());
				item_recyclerView.smoothScrollBy(v.getWidth() + 20, 0);
			}
			
			if(v.getId() == rl_top.getId()){
				
				if(info.getVideo() != null && !info.getVideo().isEmpty()&& position == 1){
					video_icon.setVisibility(View.VISIBLE);
				}
				
				top_shadow.setBackgroundResource(R.drawable.white_focus);
				
			}else if(v.getId() == rl_below.getId()){
				if(rl_below.isShown() && rl_below.isFocusable()){
					bellow_shadow.setBackgroundResource(R.drawable.white_focus);
				}
			}
		}else{
			if(v.getId() == rl_top.getId()){
				video_icon.setVisibility(View.INVISIBLE);
				top_shadow.setBackgroundResource(android.R.color.transparent);
			}else if(v.getId() == rl_below.getId()){
				bellow_shadow.setBackgroundResource(android.R.color.transparent);
			}
		}
		
	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		int action = event.getAction();
		if(action == KeyEvent.ACTION_DOWN){
			if(v.getId() == rl_top.getId() && keyCode == KeyEvent.KEYCODE_DPAD_UP){
				return true;
			}else if(v.getId() == rl_below.getId() && keyCode == KeyEvent.KEYCODE_DPAD_DOWN){
				return true;
			}
			
			/*if(getPosition() == 1 &&  keyCode == KeyEvent.KEYCODE_DPAD_RIGHT){
				alog.info("滑动了150");
				recyclerView.smoothScrollBy(150, 0);
				return true;
			}*/
		}
		return false;
	}

	@Override
	public void onClick(View arg0) {
		if(info.getVideo() != null && !info.getVideo().isEmpty() && position == 1){
			Intent intent = new Intent(context, PlayVideoActivity.class);
			intent.putExtra("videoUrl", info.getVideo());
			intent.putExtra("videoType", info.getGameId());
			intent.putExtra("gameId", info.getGameId());
			context.startActivity(intent);
		}
	}
	
}
