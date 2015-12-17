package com.atet.tvmarket.control.promotion.adapter;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.atet.common.logging.ALog;
import com.atet.tvmarket.R;
import com.atet.tvmarket.app.Constant;
import com.atet.tvmarket.control.promotion.activity.IntegralDetailsActivity;
import com.atet.tvmarket.control.promotion.activity.PromotionDetailsActivity;
import com.atet.tvmarket.control.promotion.fragment.PromotionFragment;
import com.atet.tvmarket.control.promotion.holder.DetailItemSecondHolder;
import com.atet.tvmarket.control.promotion.holder.DetailItemfirstHolder;
import com.atet.tvmarket.control.promotion.holder.IntegralSecondHolder;
import com.atet.tvmarket.control.promotion.holder.IntegralThirdHolder;
import com.atet.tvmarket.control.promotion.holder.IntegralfirstHolder;
import com.atet.tvmarket.entity.dao.ActInfo;
import com.atet.tvmarket.entity.dao.GoodsInfo;
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
public class IntegralItemAdapter extends Adapter {
	ALog alog = ALog.getLogger(PromotionFragment.class);
	private int viewType;
	private RecyclerView item_recyclerView;
	private RecyclerView recyclerView;
	private IntegralDetailsActivity context;
	private GoodsInfo info;
	private int page_tag = 0;
	
	public IntegralItemAdapter(RecyclerView recyclerView,RecyclerView item_recyclerView, IntegralDetailsActivity context) {
		this.item_recyclerView = item_recyclerView;
		this.recyclerView = recyclerView;
		this.context = context;
		
	}

	@Override
	public int getItemCount() {
		if(info != null){
			int count = info.getDetailPhotos().size();
			int size = count % 2 == 0 ? count/2 :count/2 + 1;
			return size + 1;
		}
		return 1;
	}
	
	public void setData(GoodsInfo info) {
		this.info = info;
		notifyDataSetChanged();
	}


	@Override
	public int getItemViewType(int position) {
		switch (position) {
		case 0:
			viewType = Constant.VIEWHOLDER_TYPE_0;
			break;
		default:
			viewType = Constant.VIEWHOLDER_TYPE_1;
			break;
		}
		return viewType;
	}

	@Override
	public void onBindViewHolder(ViewHolder viewHolder, int position) {
		if(position == 0){
			IntegralfirstHolder holder = (IntegralfirstHolder) viewHolder;
			
			if(page_tag == 1){
				holder.bt_exchange.requestFocus();
			}
			if(info != null){
				holder.setData(info);
			}
		}else{
			IntegralSecondHolder holder = (IntegralSecondHolder) viewHolder;
			if(info != null){
				holder.setData(info,position);
			}
		}
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
		LayoutInflater inflater = LayoutInflater.from(UIUtils.getContext());
		
		View root = null;
		ViewHolder holder = null;
		switch (viewType) {

		case Constant.VIEWHOLDER_TYPE_0:
			root = inflater.inflate(R.layout.integral_first_holder, viewGroup,false);
			holder = new IntegralfirstHolder(root,context,item_recyclerView,recyclerView);
			break;
		case Constant.VIEWHOLDER_TYPE_1:
			root = inflater.inflate(R.layout.integral_second_holder,viewGroup, false);
			holder = new IntegralSecondHolder(root,item_recyclerView,recyclerView,context);
			break;
		default:
			break;
		}
		return holder;
		
	}

}