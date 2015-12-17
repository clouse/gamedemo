package com.atet.tvmarket.control.home.adapter;

import java.util.ArrayList;
import java.util.List;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.atet.tvmarket.control.home.entity.BaseGameInfo;

public abstract class BaseAdapter extends RecyclerView.Adapter<BaseViewHolder>{

	public abstract void setItems(List<BaseGameInfo> myItems);
	
	public List<BaseGameInfo> items = new ArrayList<BaseGameInfo>();
	
	public BaseAdapter(){
		
	}
	
	/*public void setItems(List<BaseGameInfo> items){
		this.items.clear();
		this.items.addAll(items);
		notifyDataSetChanged();
	}*/
	
	@Override
	public int getItemCount() {
		return items.size();
	}

	@Override
	public void onBindViewHolder(BaseViewHolder vh, int position) {
		
	}

	@Override
	public BaseViewHolder onCreateViewHolder(ViewGroup container, int viewType) {
		return null;
	}

}
