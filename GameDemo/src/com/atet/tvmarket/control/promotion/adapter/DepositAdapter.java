package com.atet.tvmarket.control.promotion.adapter;

import java.util.ArrayList;
import java.util.List;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.atet.tvmarket.R;
import com.atet.tvmarket.control.home.inf.OnRecyItemClickListener;
import com.atet.tvmarket.control.promotion.activity.DepositActivity;
import com.atet.tvmarket.control.promotion.holder.DepositHolder;
import com.atet.tvmarket.entity.dao.UserGameGiftInfo;
import com.atet.tvmarket.utils.UIUtils;

/*
 * File：DepositAdapter.java
 *
 * Copyright (C) 2015 MainActivity Project
 * Date：2015年7月11日 上午10:02:01
 * All Rights SXHL(New Space) Corporation Reserved.
 * http://www.at-et.cn
 *
 */
public class DepositAdapter extends Adapter<ViewHolder> {
	public static View lastFocusView=null;
	private OnRecyItemClickListener mListener;
	private RecyclerView recyclerView;
	private List<UserGameGiftInfo> pageinfos = new ArrayList<UserGameGiftInfo>();
	
	private DepositActivity context;
	
	public DepositAdapter(RecyclerView recyclerView,DepositActivity context){
		this.recyclerView = recyclerView;
		this.context = context;
	}

	@Override
	public int getItemCount() {
		if(pageinfos.size() != 0){
			return pageinfos.size();
		}
		return 0;
	}

	public void setData(List<UserGameGiftInfo> pageGames) {
		this.pageinfos = pageGames;
		notifyDataSetChanged();
	}
	

	@Override
	public void onBindViewHolder(ViewHolder viewholder, int position){
		DepositHolder holder = (DepositHolder) viewholder;
		holder.deposit_item.setTag(position);
		if(pageinfos!=null){
			holder.setData(pageinfos.get(position));
		}
		if(position==0){
			if(lastFocusView==null){
				holder.deposit_item.requestFocus();
			}
			else{
				holder.deposit_item.clearFocus();
			}
		}
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
		LayoutInflater inflater = LayoutInflater.from(UIUtils.getContext());
		View root = inflater.inflate(R.layout.deposit_item,viewGroup, false);
		return new DepositHolder(root ,recyclerView,context);
	}

	public void setOnRecyItemClickListener(OnRecyItemClickListener listener) {
		this.mListener = listener;
		
	}
}
