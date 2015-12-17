package com.atet.tvmarket.control.promotion.adapter;

import java.util.List;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.atet.common.logging.ALog;
import com.atet.tvmarket.R;
import com.atet.tvmarket.app.Constant;
import com.atet.tvmarket.control.promotion.activity.PromotionDetailsActivity;
import com.atet.tvmarket.control.promotion.fragment.PromotionFragment;
import com.atet.tvmarket.control.promotion.holder.DetailItemSecondHolder;
import com.atet.tvmarket.control.promotion.holder.DetailItemfirstHolder;
import com.atet.tvmarket.entity.dao.ActDetailPhoto;
import com.atet.tvmarket.entity.dao.ActInfo;
import com.atet.tvmarket.utils.UIUtils;

/*
 * File：ItemAdapter.java
 *
 * Copyright (C) 2015 MainActivity Project
 * Date：2015年7月12日 下午6:50:27
 * All Rights SXHL(New Space) Corporation Reserved.
 * http://www.at-et.cn
 *
 */
public class ItemAdapter extends Adapter {
	ALog alog = ALog.getLogger(ItemAdapter.class);
	private int viewType;
	private RecyclerView mRecyclerView;
	private PromotionDetailsActivity context;
	private ActInfo info;
	private DetailItemfirstHolder firstHolder;
	private int page_tag = 0;
	
	public ItemAdapter(RecyclerView recyclerView,PromotionDetailsActivity context) {
		this.mRecyclerView = recyclerView;
		this.context = context;
		
	}

	@Override
	public int getItemCount() {
		if(info != null){
			int count = info.getDetailPhotos().size();
			if(count > 1){
				return 3;
			}
			return 2;
		}
		return 1;
	}
	
	public void setData(ActInfo info) {
		this.info = info;
		notifyDataSetChanged();
	}


	@Override
	public int getItemViewType(int position) {
		if(position == 0){
			viewType = Constant.VIEWHOLDER_TYPE_0;
		}else{
			viewType = Constant.VIEWHOLDER_TYPE_1;
		}
		return viewType;
	}

	@Override
	public void onBindViewHolder(ViewHolder viewHolder, int position) {
		if(position == 0){
			firstHolder = (DetailItemfirstHolder) viewHolder;
			
			if(page_tag == 1){
				firstHolder.bt_download.requestFocus();
			}
			
			firstHolder.setData(info);
		}else{
			DetailItemSecondHolder holder = (DetailItemSecondHolder) viewHolder;
			
			holder.setData(info,position);
		}
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
		LayoutInflater inflater = LayoutInflater.from(UIUtils.getContext());
		
		View root = null;
		ViewHolder holder = null;
		switch (viewType) {

		case Constant.VIEWHOLDER_TYPE_0:
			root = inflater.inflate(R.layout.item_recyclerview_first_holder, viewGroup,false);
			holder = new DetailItemfirstHolder(root,context,mRecyclerView,info);
			break;
		case Constant.VIEWHOLDER_TYPE_1:
			root = inflater.inflate(R.layout.item_recyclerview_second_holder,viewGroup, false);
			holder = new DetailItemSecondHolder(root,mRecyclerView,context);
			break;
		default:
			break;
		}
		return holder;
	}
	
	public DetailItemfirstHolder getFirstHolder(){
		return firstHolder;
	}

}