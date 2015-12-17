package com.atet.tvmarket.control.promotion.holder;

import java.util.List;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.RelativeLayout;

import com.atet.common.logging.ALog;
import com.atet.tvmarket.R;
import com.atet.tvmarket.control.promotion.activity.PromotionDetailsActivity;
import com.atet.tvmarket.control.video.PlayVideoActivity;
import com.atet.tvmarket.entity.dao.ActDetailPhoto;
import com.atet.tvmarket.entity.dao.ActInfo;
import com.atet.tvmarket.view.BaseImageView;

/*
 * File：SecondItemHolder.java
 *
 * Copyright (C) 2015 MainActivity Project
 * Date：2015年7月12日 下午5:16:28
 * All Rights SXHL(New Space) Corporation Reserved.
 * http://www.at-et.cn
 *
 */
public class DetailItemSecondHolder extends PromotionBaseHolder implements 
			OnFocusChangeListener, OnClickListener{
	ALog alog = ALog.getLogger(DetailItemSecondHolder.class);
	public BaseImageView top_icon;
	private BaseImageView top_shadow;
	public RelativeLayout rl_top;
	public BaseImageView bellow_icon;
	private BaseImageView bellow_shadow;
	public RelativeLayout rl_below;
	private RecyclerView mRecyclerView;
	private PromotionDetailsActivity context;
	private BaseImageView video_icon;
	private ActInfo info;
	private int position;
	
	
	public DetailItemSecondHolder(View itemView, RecyclerView recyclerView,
			PromotionDetailsActivity context) {
		super(itemView);
		this.mRecyclerView = recyclerView;
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
		rl_top.setOnClickListener(this);
		rl_below.setOnFocusChangeListener(this);
	}
	

	public void setData(ActInfo info, int position) {
		this.info = info;
		this.position = position;
		if(info != null){
			List<ActDetailPhoto> detailPhotos = info.getDetailPhotos();
			if(info.getVideo() == null || info.getVideo().isEmpty()){
				mImageFetcher.loadImage(detailPhotos.get(position * 2 - 2).getPicture(), top_icon, R.drawable.default_recommand);
				if(detailPhotos.size() > position * 2 - 1){
					mImageFetcher.loadImage(detailPhotos.get(position * 2 - 1).getPicture(), bellow_icon, R.drawable.default_recommand);
				}else{
					setBellowClickable();
				}
			}else{
				if(position == 1){
					mImageFetcher.loadImage(info.getThumbnail(),top_icon , R.drawable.default_recommand);
					if(detailPhotos != null && detailPhotos.size() !=0){
						mImageFetcher.loadImage(detailPhotos.get(0).getPicture() ,bellow_icon , R.drawable.default_recommand);
					}else{
						setBellowClickable();
					}
				}else{
					if(detailPhotos != null && detailPhotos.size() > 1){
						mImageFetcher.loadImage(detailPhotos.get(1).getPicture(),top_icon , R.drawable.default_recommand);
						if(detailPhotos.size() > 2){
							mImageFetcher.loadImage(detailPhotos.get(2).getPicture() ,bellow_icon , R.drawable.default_recommand);
						}else{
							setBellowClickable();
						}
					}
				}
			}
			/*mImageFetcher.loadImage(detailPhotos.get(position * 2 - 2).getPicture(), top_icon, R.drawable.default_recommand);
			
			if(detailPhotos.size() > position * 2 - 1){
				mImageFetcher.loadImage(detailPhotos.get(position * 2 - 1).getPicture(), bellow_icon, R.drawable.default_recommand);
			}else{
				rl_below.setClickable(false);
				rl_below.setFocusable(false);
				rl_below.setFocusableInTouchMode(false);
			}*/
		}
		
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
			
			/*if(location[0] - v.getWidth() * 1.5< mRecyclerView.getLeft()){
				
				mRecyclerView.smoothScrollBy(-(v.getWidth()), 0);
			}
			
			if(location[0] + v.getWidth() * 1.2 > mRecyclerView.getRight()){
				mRecyclerView.smoothScrollBy(v.getWidth(), 0);
			}*/
			
			if(location[0] - v.getWidth() * 1.1 < 0){
				
				mRecyclerView.smoothScrollBy(-(v.getWidth() + 20), 0);
			}
			
			if(location[0] + v.getWidth() * 1.1 > mRecyclerView.getRight()){
				
				mRecyclerView.smoothScrollBy(v.getWidth() + 20, 0);
			}
			
			if(v.getId() == rl_top.getId()){
				if(info.getVideo() != null && !info.getVideo().isEmpty() && position == 1){
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
	public void onClick(View v) {
		if(info.getVideo() != null && !info.getVideo().isEmpty() && position == 1){
			Intent intent = new Intent(context, PlayVideoActivity.class);
			intent.putExtra("videoUrl", info.getVideo());
			intent.putExtra("isGameVideo", true);
			intent.putExtra("gameId", info.getGameId());
			context.startActivity(intent);
		}
		
	}
	
}
