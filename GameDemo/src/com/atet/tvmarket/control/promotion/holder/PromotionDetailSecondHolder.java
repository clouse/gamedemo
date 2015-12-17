package com.atet.tvmarket.control.promotion.holder;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.atet.common.logging.ALog;
import com.atet.tvmarket.R;
import com.atet.tvmarket.control.promotion.activity.PromotionDetailsActivity;
import com.atet.tvmarket.control.promotion.adapter.ItemAdapter;
import com.atet.tvmarket.control.promotion.decoration.SecondItemInsetDecoration;
import com.atet.tvmarket.control.promotion.fragment.PromotionFragment;
import com.atet.tvmarket.entity.dao.ActInfo;
import com.atet.tvmarket.utils.ScaleViewUtils;
import com.atet.tvmarket.view.BaseImageView;
import com.atet.tvmarket.view.CloseAcceTextView;

/*
 * File：PromotionDetailSecondHolder.java
 *
 * Copyright (C) 2015 MainActivity Project
 * Date：2015年7月8日 下午2:54:24
 * All Rights SXHL(New Space) Corporation Reserved.
 * http://www.at-et.cn
 *
 */
public class PromotionDetailSecondHolder extends ViewHolder implements OnFocusChangeListener {
	ALog alog = ALog.getLogger(PromotionDetailSecondHolder.class);
	private PromotionDetailsActivity context;
	public Button bt_download;
	public RecyclerView item_recyclerView;
	//private TextView tv_title;
	private LinearLayoutManager manager;
	private ItemAdapter itemAdapter;
	private SecondItemInsetDecoration insetDecoration;
	private ActInfo info;

	public PromotionDetailSecondHolder(View itemView,PromotionDetailsActivity context) {
		super(itemView);
		this.context = context;
		initView();
	}

	protected void initView() {
		ScaleViewUtils.scaleView(itemView);
//		bt_downLoad = (Button) itemView.findViewById(R.id.promotion_detail_item_bt);
		//tv_title = (TextView) itemView.findViewById(R.id.detail_item2_act_title);
		item_recyclerView = (RecyclerView) itemView.findViewById(R.id.promotion_detail_item_recyclerview);
		
		context.setBlackTitle2(itemView, true);
		context.setTitleData();
		
		manager = new LinearLayoutManager(context);
		itemAdapter = new ItemAdapter(item_recyclerView,context);
		insetDecoration = new SecondItemInsetDecoration(context);
		
		
		manager.setOrientation(LinearLayoutManager.HORIZONTAL);
		item_recyclerView.addItemDecoration(insetDecoration);
		item_recyclerView.setLayoutManager(manager);
		item_recyclerView.setAdapter(itemAdapter);
		item_recyclerView.setOnFocusChangeListener(this);
		
		
//		bt_downLoad.setOnFocusChangeListener(this);
		
	}

	
	public void setData(ActInfo info) {
		this.info = info;
		//tv_title.setText(info.getTitle());
		alog.info(info.toString());
		itemAdapter.setData(info);
	}
	
	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if(hasFocus){
			if(v.getId() == item_recyclerView.getId()){
				//NewToast.makeToast(context, "item_recyclerView", 0).show();
				bt_download = (Button) item_recyclerView.getChildAt(0).findViewById(R.id.promotion_detail_item_download);
				bt_download.requestFocus();
			}
		}else{
			
		}
		
	}
	
	public ItemAdapter getItemAdapter(){
		return itemAdapter;
	}

}
