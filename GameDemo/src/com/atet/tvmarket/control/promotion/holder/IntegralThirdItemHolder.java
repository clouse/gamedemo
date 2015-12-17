package com.atet.tvmarket.control.promotion.holder;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.atet.tvmarket.R;
import com.atet.tvmarket.control.promotion.activity.IntegralDetailsActivity;
import com.atet.tvmarket.entity.UserGoodsOrderInfo;
import com.atet.tvmarket.utils.ScaleViewUtils;
import com.atet.tvmarket.utils.UIUtils;

/*
 * File：IntegralThirdItemHolder.java
 *
 * Copyright (C) 2015 DeviceIdActivity Project
 * Date：2015年7月18日 下午4:04:39
 * All Rights SXHL(New Space) Corporation Reserved.
 * http://www.at-et.cn
 *
 */
public class IntegralThirdItemHolder extends ViewHolder implements
		OnFocusChangeListener {
	public RelativeLayout rl;
	private IntegralDetailsActivity context;
	private RecyclerView item_RecyclerView;
	private TextView tv_time;
	private TextView tv_list;
	private TextView tv_price;
	private TextView tv_state;
	private TextView tv_deliver;
	private TextView tv_order_number;
	private TextView tv_name;
	private TextView tv_tel;
	private TextView tv_address;

	public IntegralThirdItemHolder(View itemView,
			RecyclerView item_recyclerView, IntegralDetailsActivity context) {
		super(itemView);
		this.context = context;
		this.item_RecyclerView = item_recyclerView;
		initView();
	}

	private void initView() {
		ScaleViewUtils.scaleView(itemView);
		rl = (RelativeLayout) itemView.findViewById(R.id.integral_third);
		tv_time = (TextView) itemView.findViewById(R.id.integral_third_time);
		tv_list = (TextView) itemView.findViewById(R.id.integral_third_list);
		tv_price = (TextView) itemView.findViewById(R.id.integral_third_price);
		tv_state = (TextView) itemView.findViewById(R.id.integral_third_state);
		tv_deliver = (TextView) itemView.findViewById(R.id.integral_third_deliver);
		tv_order_number = (TextView) itemView.findViewById(R.id.integral_third_number);
		tv_name = (TextView) itemView.findViewById(R.id.integral_third_name);
		tv_tel = (TextView) itemView.findViewById(R.id.integral_third_tel);
		tv_address = (TextView) itemView.findViewById(R.id.integral_third_address);
		rl.setOnFocusChangeListener(this);
		// rl.setOnKeyListener(this);
	}

	public void setData(UserGoodsOrderInfo orderInfo) {
		tv_time.setText(UIUtils.getString(R.string.order_date)+ UIUtils.changeTimeStyle(orderInfo.getTradeTime()));
		//System.out.println("orderInfo.getPhone()" + orderInfo.getPhone());
		
		tv_list.setText(UIUtils.getString(R.string.order_desc) + getDesc(orderInfo.getGoodsName()));
		tv_price.setText(UIUtils.getString(R.string.order_price) + orderInfo.getPrice());
		tv_deliver.setText(UIUtils.getString(R.string.order_wechat) + getDesc(orderInfo.getExpress()));
		tv_order_number.setText(UIUtils.getString(R.string.order_id)+ getDesc(orderInfo.getOrderId()));
		tv_name.setText(UIUtils.getString(R.string.order_name) + getDesc(orderInfo.getName()));
		tv_tel.setText(UIUtils.getString(R.string.order_tel)+ getDesc(orderInfo.getPhone()));
		tv_address.setText(UIUtils.getString(R.string.order_address)+ getDesc(orderInfo.getAddress()));
		if(orderInfo.getState()  == 1){
			tv_state.setText(UIUtils.getString(R.string.order_state_handling));
		}else if(orderInfo.getState()  == 2){
			tv_state.setText(UIUtils.getString(R.string.order_state_send));
		}else{
			tv_state.setText(UIUtils.getString(R.string.order_state_receiver));
		}
	}
	
	private String  getDesc(String desc){
			if(null == desc || desc.isEmpty()){
				return "";
			}
			return desc;
		}
	@Override
	public void onFocusChange(View v, boolean hasFocus) {

		if (hasFocus) {
			int[] location = new int[2];
			v.getLocationOnScreen(location);

			if (location[1] + v.getHeight() * 1.1 > item_RecyclerView.getBottom()) {
				 item_RecyclerView.smoothScrollBy(0, v.getHeight() + 30);
				 //item_RecyclerView.smoothScrollToPosition(getPosition() + 1);
			}
			if (location[1] - v.getHeight() * 0.1 < item_RecyclerView.getTop()) {
				item_RecyclerView.smoothScrollBy(0, -(v.getHeight() + 30));
				//item_RecyclerView.smoothScrollToPosition(getPosition() - 1);
			}
			// 如果没有游戏截图，让游戏视频获取焦点
			if (!context.hasPhotos()) {
				rl.setNextFocusLeftId(R.id.integral_rl_detail);
			}
			rl.setBackgroundResource(R.drawable.order_fcous);
		} else {
			rl.setBackgroundResource(android.R.color.transparent);
		}

	}

}
