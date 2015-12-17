package com.atet.tvmarket.control.promotion.holder;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.atet.common.logging.ALog;
import com.atet.tvmarket.R;
import com.atet.tvmarket.control.promotion.activity.IntegralDetailsActivity;
import com.atet.tvmarket.control.promotion.adapter.IntegralItemAdapter;
import com.atet.tvmarket.control.promotion.decoration.SecondItemInsetDecoration;
import com.atet.tvmarket.entity.dao.GoodsInfo;
import com.atet.tvmarket.utils.ScaleViewUtils;

/*
 * File：PromotionDetailSecondHolder.java
 *
 * Copyright (C) 2015 MainActivity Project
 * Date：2015年7月8日 下午2:54:24
 * All Rights SXHL(New Space) Corporation Reserved.
 * http://www.at-et.cn
 *
 */
public class IntegralDetailSecondHolder extends ViewHolder implements OnFocusChangeListener {
	ALog alog = ALog.getLogger(IntegralDetailSecondHolder.class);
	public static boolean isLeft = true;//判断焦点是从左边的item过来还是右边的item过来
	private IntegralDetailsActivity context;
	public RecyclerView item_recyclerView;
	private RecyclerView mRecyclerView;
	private LinearLayoutManager manager;
	private IntegralItemAdapter itemAdapter;
	private SecondItemInsetDecoration insetDecoration;
	private TextView tv_title;
	private GoodsInfo info;
	
	public IntegralDetailSecondHolder(View itemView,IntegralDetailsActivity context,
			RecyclerView recyclerView) {
		super(itemView);
		this.mRecyclerView = recyclerView;
		this.context = context;
		initView();
	}
	
	protected void initView() {
		ScaleViewUtils.scaleView(itemView);
//		bt_downLoad = (Button) itemView.findViewById(R.id.promotion_detail_item_bt);
		tv_title = (TextView) itemView.findViewById(R.id.detail_item2_game_name);
		item_recyclerView = (RecyclerView) itemView.findViewById(R.id.promotion_detail_item_recyclerview);
		context.setBlackTitle2(itemView, true);
		context.setTitleData();
		
		manager = new LinearLayoutManager(context);
		itemAdapter = new IntegralItemAdapter(mRecyclerView,item_recyclerView,context);
		insetDecoration = new SecondItemInsetDecoration(context);
		
		manager.setOrientation(LinearLayoutManager.HORIZONTAL);
		item_recyclerView.addItemDecoration(insetDecoration);
		item_recyclerView.setLayoutManager(manager);
		item_recyclerView.setAdapter(itemAdapter);
		item_recyclerView.setOnFocusChangeListener(this);
//		bt_downLoad.setOnFocusChangeListener(this);
		
	}


	public void setData(GoodsInfo info) {
		this.info = info;
		tv_title.setText(info.getTitle());
		alog.info(info.toString());
		itemAdapter.setData(info);
	}
	
	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if(hasFocus){
			if(isLeft){
				if(v.getId() == item_recyclerView.getId()){
					Button bt_download = (Button) item_recyclerView.getChildAt(0).findViewById(R.id.item_download);
					bt_download.requestFocus();
				}
			}else{
				int last = item_recyclerView.getChildCount() - 1;
				if(last > 0){
					RelativeLayout topView = (RelativeLayout) item_recyclerView.getChildAt(last).findViewById(R.id.detail_item2_rl_top);
					topView.requestFocus();
					mRecyclerView.smoothScrollBy(-1000, 0);
				}else{
					RelativeLayout infoView = (RelativeLayout) item_recyclerView.getChildAt(0).findViewById(R.id.integral_rl_detail);
					infoView.requestFocus();
				}
				isLeft = true;
			}
		}else{
			
		}
		
	}

}
