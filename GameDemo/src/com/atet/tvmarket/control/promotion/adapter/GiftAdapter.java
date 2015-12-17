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
import com.atet.tvmarket.control.promotion.activity.GiftActivity;
import com.atet.tvmarket.control.promotion.holder.GiftHolder;
import com.atet.tvmarket.entity.dao.GameGiftInfo;
import com.atet.tvmarket.entity.dao.UserGameGiftInfo;
import com.atet.tvmarket.utils.UIUtils;

/*
 * File：GiftAdapter.java
 *
 * Copyright (C) 2015 MainActivity Project
 * Date：2015年7月10日 下午2:54:01
 * All Rights SXHL(New Space) Corporation Reserved.
 * http://www.at-et.cn
 *
 */
public class GiftAdapter extends Adapter {
	public static View lastFocusView=null;
	private OnRecyItemClickListener mListener;
	private RecyclerView recyclerView;
	private List<GameGiftInfo> games = new ArrayList<GameGiftInfo>();
	private List<UserGameGiftInfo> receivedGift = new ArrayList<UserGameGiftInfo>();
	private GiftActivity context;
	private GiftHolder holder;

	public GiftAdapter(RecyclerView recyclerView,GiftActivity context) {
		this.recyclerView = recyclerView;
		this.context = context;
	}
	
	public void setData(List<GameGiftInfo> pageGames, List<UserGameGiftInfo> gifts) {
		games.clear();
		games.addAll(pageGames);
		receivedGift.clear();
		receivedGift.addAll(gifts);
		
		recyclerView.setAdapter(this);
		//notifyDataSetChanged();
	}
	
	@Override
	public int getItemCount() {
		if(games.size() == 0){
			return 0;
		}
		return games.size();
	}

	@Override
	public void onBindViewHolder(ViewHolder viewHolder, int position) {
		holder = (GiftHolder) viewHolder;
		holder.panel.getGift_top_bt().setTag(position);
		
		if(games!=null){
			holder.setData(games.get(position),receivedGift);
		}
		if(position==0){
			if(lastFocusView==null){
				holder.panel.getGift_top_bt().requestFocus();
			}
			else{
				holder.panel.getGift_top_bt().clearFocus();
			}
		}
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
		LayoutInflater inflater = LayoutInflater.from(UIUtils.getContext());

		View root = inflater.inflate(R.layout.gift_item_panel, viewGroup, false);
		return new GiftHolder(root, recyclerView, mListener,context);
	}

	public void setOnRecyItemClickListener(OnRecyItemClickListener listener) {
		this.mListener = listener;
	}
	
	public GiftHolder getHolder(){
		return holder;
	}
}
