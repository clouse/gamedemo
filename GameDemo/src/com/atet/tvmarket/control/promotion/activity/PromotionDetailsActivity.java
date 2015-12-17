package com.atet.tvmarket.control.promotion.activity;

import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.Window;

import com.atet.tvmarket.R;
import com.atet.tvmarket.app.Constant;
import com.atet.tvmarket.control.common.BaseActivity;
import com.atet.tvmarket.control.mygame.activity.MyGameActivity;
import com.atet.tvmarket.control.promotion.holder.DetailItemfirstHolder;
import com.atet.tvmarket.control.promotion.holder.PromotionDetailSecondHolder;
import com.atet.tvmarket.control.promotion.holder.PromotionDetailfirstHolder;
import com.atet.tvmarket.entity.dao.ActInfo;
import com.atet.tvmarket.entity.dao.GameInfo;
import com.atet.tvmarket.model.net.http.download.BtnDownCommonListener;
import com.atet.tvmarket.utils.ScaleViewUtils;

/*
 * File：PromotionDetails.java
 *
 * Copyright (C) 2015 MainActivity Project
 * Date：2015年6月10日 下午5:17:54
 * All Rights SXHL(New Space) Corporation Reserved.
 * http://www.at-et.cn
 *
 */

/**
 * @description: 活动详情
 *
 * @author: LiJie
 * @date: 2015年6月10日 下午5:18:00 
 */
public class PromotionDetailsActivity extends BaseActivity implements OnFocusChangeListener{
	private RecyclerView recyclerView;
	private LinearLayoutManager manager;
	private PromotionDetailAdapter adapter;
	private ActInfo info;
	//private int page_tag = 0;//0 有两个页面 
	private PromotionDetailSecondHolder secondHolder;

	// 被迫这样写的
	public static GameInfo gameInfo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getWindow().setFormat(PixelFormat.TRANSLUCENT);
		initView();
		
	}
	
	private void initView() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_promotion_detail);
		ScaleViewUtils.init(this);
		ScaleViewUtils.scaleView(getWindow().getDecorView());
		recyclerView = (RecyclerView) findViewById(R.id.promotion_detail_recyclerview);
		
		adapter = new PromotionDetailAdapter();
		manager = new LinearLayoutManager(getApplicationContext());
		getData();
		
		manager.setOrientation(LinearLayoutManager.HORIZONTAL);
		recyclerView.setLayoutManager(manager);
		recyclerView.setAdapter(adapter);
		recyclerView.setOnFocusChangeListener(this);
	}
	
	private void getData(){
		info = (ActInfo) getIntent().getSerializableExtra(Constant.ACTINFO);
		gameInfo = new GameInfo();
		gameInfo.setGameId(info.getGameId());
	}
	
	class PromotionDetailAdapter extends Adapter{
		private int viewType;
		
		@Override
		public int getItemCount() {
			if(info != null ){
				if(info.getUrl() == null || info.getUrl().isEmpty()){
					return 1;
				}
				return 2;
			}
			return 0;
		}
		
		@Override
		public int getItemViewType(int position) {
			
			if(getItemCount() == 1){
				viewType = Constant.VIEWHOLDER_TYPE_1;
			}else if(getItemCount() == 2){
				if(position == 0){
					viewType = Constant.VIEWHOLDER_TYPE_0;
				}else{
					viewType = Constant.VIEWHOLDER_TYPE_1;
				}
			}
			
			return viewType;
		}

		@Override
		public void onBindViewHolder(ViewHolder viewHolder, int position) {
			if(info.getUrl() != null && !info.getUrl().isEmpty()){
				
				if(position == 1){
					secondHolder = (PromotionDetailSecondHolder) viewHolder;
					if(info != null){
						secondHolder.setData(info);
					}
				}else{
					PromotionDetailfirstHolder holder = (PromotionDetailfirstHolder) viewHolder;
					if(info != null){
						holder.setData(info);
					}
				}
			}else{
				secondHolder = (PromotionDetailSecondHolder) viewHolder;
				if(info != null){
					secondHolder.setData(info);
				}
			}
		}

		@Override
		public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
			LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
			
			View root = null;
			ViewHolder holder = null;
			switch (viewType) {

			case Constant.VIEWHOLDER_TYPE_0:
				root = inflater.inflate(R.layout.promotion_detail_item1, viewGroup,false);
				holder = new PromotionDetailfirstHolder(root,recyclerView);
				break;
			case Constant.VIEWHOLDER_TYPE_1:
				root = inflater.inflate(R.layout.promotion_detail_item2,viewGroup, false);
				holder = new PromotionDetailSecondHolder(root ,PromotionDetailsActivity.this);
				break;
			
			default:
				break;
			}
			return holder;
			
		}
		
	}
	
	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if(v.hasFocus()){
			if(info.getUrl() != null && !info.getUrl().isEmpty()){
				PromotionDetailfirstHolder holder = (PromotionDetailfirstHolder) recyclerView.getChildViewHolder(recyclerView.getChildAt(0));
				holder.webView.requestFocus();
			}else{
				PromotionDetailSecondHolder holder = (PromotionDetailSecondHolder) recyclerView.getChildViewHolder(recyclerView.getChildAt(0));
				DetailItemfirstHolder childHolder = (DetailItemfirstHolder) holder.item_recyclerView.
						getChildViewHolder(holder.item_recyclerView.getChildAt(0));
				childHolder.bt_download.requestFocus();
			}
		}
	}
	
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
		if (arg1 == RESULT_OK){
			if(gameInfo!=null){
				//数据统计来源
				gameInfo.setTypeName("GameActivity");
				MyGameActivity.addToMyGameList(this,gameInfo);
			}
			
		}
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		BtnDownCommonListener listener = null;
		
		if(secondHolder.getItemAdapter().getFirstHolder() != null){
			listener = secondHolder.getItemAdapter().getFirstHolder().getListener();
		}
		
		if(listener != null){
			listener.recycle();
		}
	}
}
