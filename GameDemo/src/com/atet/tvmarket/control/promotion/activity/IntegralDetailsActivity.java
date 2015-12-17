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

import com.atet.common.logging.ALog;
import com.atet.tvmarket.R;
import com.atet.tvmarket.app.Constant;
import com.atet.tvmarket.control.common.BaseActivity;
import com.atet.tvmarket.control.promotion.holder.IntegralDetailSecondHolder;
import com.atet.tvmarket.control.promotion.holder.IntegralDetailfirstHolder;
import com.atet.tvmarket.control.promotion.holder.IntegralThirdHolder;
import com.atet.tvmarket.control.promotion.holder.IntegralfirstHolder;
import com.atet.tvmarket.entity.dao.GoodsInfo;
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
 * @description:积分详情
 *
 * @author: LiJie
 * @date: 2015年6月10日 下午5:18:00 
 */
public class IntegralDetailsActivity extends BaseActivity implements OnFocusChangeListener{
	ALog alog = ALog.getLogger(IntegralDetailsActivity.class);
	private RecyclerView recyclerView;
	private LinearLayoutManager manager;
	private IntegralDetailAdapter adapter;
	private GoodsInfo info;
	private IntegralThirdHolder thirdholder;
	private boolean isFirstIn = true;
	private long lastFocusRightTime = 0;
//	private int web_tag = 1; // 网页页面显示
	private int order_tag = 1;// 订单页面显示
	/*private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			getExchangeData();
		};
	};
	*/
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
		
		adapter = new IntegralDetailAdapter();
		manager = new LinearLayoutManager(getApplicationContext());
		getData();
		
		manager.setOrientation(LinearLayoutManager.HORIZONTAL);
		recyclerView.setLayoutManager(manager);
		recyclerView.setAdapter(adapter);
		recyclerView.setOnFocusChangeListener(this);
		
	}
	
	private void getData(){
		info = (GoodsInfo) getIntent().getSerializableExtra(Constant.GOODSINFO);
	}
	
	public IntegralThirdHolder getThirdHolder(){
		return thirdholder;
	}
	
	/**
	 * 
	 * @description: 判断活动是否有活动截图
	 *
	 * @return 
	 * @throws: 
	 * @author: LiJie
	 * @date: 2015年8月13日 下午8:16:03
	 */
	public boolean hasPhotos(){
		if(info.getDetailPhotos().size() != 0){
			return true;
		}
		return false;
	}
	
	class IntegralDetailAdapter extends Adapter{
		private int viewType;
		
		@Override
		public int getItemCount() {
			
			if(info != null){
				if((info.getUrl() == null || info.getUrl().isEmpty()) && order_tag == 0){
					return 1;
				}else if((info.getUrl() == null || info.getUrl().isEmpty()) || order_tag == 0){
					return 2;
				}
				return 3;
			}
			return 0;
		}
		
		public void setData() {
			
			notifyDataSetChanged();
		}

		@Override
		public int getItemViewType(int position) {
			int count = getItemCount();
			if(count == 1){
				viewType = Constant.VIEWHOLDER_TYPE_1;
			}else if(count == 2){
				if(info.getUrl() != null && !info.getUrl().isEmpty()){
					if(position == 0){
						viewType = Constant.VIEWHOLDER_TYPE_0;
					}else{
						viewType = Constant.VIEWHOLDER_TYPE_1;
					}
				}else{
					if(position == 0){
						viewType = Constant.VIEWHOLDER_TYPE_1;
					}else{
						viewType = Constant.VIEWHOLDER_TYPE_2;
					}
				}
			}else{
				if(position == 0){
					viewType = Constant.VIEWHOLDER_TYPE_0;
				}else if(position == 1){
					viewType = Constant.VIEWHOLDER_TYPE_1;
				}else{
					viewType = Constant.VIEWHOLDER_TYPE_2;
				}
			}
			return viewType;
		}

		@Override
		public void onBindViewHolder(ViewHolder viewHolder, int position) {	
			if(info.getUrl() == null || info.getUrl().isEmpty()){
				if(position == 0){
					setSecondHolderData(viewHolder);
				}else{
					thirdholder = (IntegralThirdHolder) viewHolder;
				}
			}else{
				if(position == 0){
					IntegralDetailfirstHolder holder = (IntegralDetailfirstHolder) viewHolder;
					if(info != null){
						holder.setData(info);
					}
				}else if(position == 1){
					setSecondHolderData(viewHolder);
				}else if(position == 2){
					thirdholder = (IntegralThirdHolder) viewHolder;
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
				root = inflater.inflate(R.layout.integral_detail_item1, viewGroup,false);
				holder = new IntegralDetailfirstHolder(root);
				break;
			case Constant.VIEWHOLDER_TYPE_1:
				root = inflater.inflate(R.layout.integral_detail_item2,viewGroup, false);
				holder = new IntegralDetailSecondHolder(root ,IntegralDetailsActivity.this,recyclerView);
				break;
				
			case Constant.VIEWHOLDER_TYPE_2:
				root = inflater.inflate(R.layout.integral_detail_item3,viewGroup, false);
				holder = new IntegralThirdHolder(root,IntegralDetailsActivity.this);
				break;
				 
			default:
				break;
			}
			return holder;
			
		}
		
	}
	
	public void setSecondHolderData(ViewHolder viewHolder){
		IntegralDetailSecondHolder holder = (IntegralDetailSecondHolder) viewHolder;
		if(info != null){
			holder.setData(info);
		}
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if(hasFocus){
			if(adapter.getItemCount() == 3){
				View view = recyclerView.getLayoutManager().findViewByPosition(0);
				view.findViewById(R.id.integral_detail_webview).requestFocus();
				/*IntegralDetailfirstHolder holder = (IntegralDetailfirstHolder) recyclerView.getChildViewHolder(recyclerView.getChildAt(0));
				holder.webView.requestFocus();*/
			}else{
				if(isFirstIn){
					View view = recyclerView.getLayoutManager().findViewByPosition(0);
					view.findViewById(R.id.item_download).requestFocus();
					isFirstIn = false;
					/*IntegralDetailSecondHolder holder = (IntegralDetailSecondHolder) recyclerView.getChildViewHolder(recyclerView.getChildAt(0));
					IntegralfirstHolder childHolder = (IntegralfirstHolder) holder.item_recyclerView.getChildViewHolder(holder.item_recyclerView.getChildAt(0));
					childHolder.bt_exchange.requestFocus();*/
				}
			}
		}
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == 1){
			setTitleData();
		}else if(requestCode == 0){
			if(thirdholder != null){
				thirdholder.getExchangeData();
			}
		}
	}
	
	public RecyclerView getRecyclerView(){
		return recyclerView;
	};
	
	/*@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		int action = event.getAction();
		int keyCode = event.getKeyCode();
		View nextFocus = null;

		if (action == KeyEvent.ACTION_DOWN) {

			if (keyCode == KeyEvent.KEYCODE_DPAD_UP
					|| keyCode == KeyEvent.KEYCODE_DPAD_DOWN
					|| keyCode == KeyEvent.KEYCODE_DPAD_LEFT
					|| keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
				
				View child = getFocusedChild();
				if (child != null) {
					//判断是上下移动还是左右移动
					if(keyCode == KeyEvent.KEYCODE_DPAD_UP
							|| keyCode == KeyEvent.KEYCODE_DPAD_DOWN){
						
						nextFocus= child.focusSearch(FOCUS_UP);
					}else{
						nextFocus = child.focusSearch(FOCUS_RIGHT);
					}
					if (nextFocus != null) {
						long curTime = System.currentTimeMillis();
						// 判断这次时间与上次按下的时间之差是不是小于200ms
						if (curTime - lastFocusRightTime < 200) {
							return true;
						}

						lastFocusRightTime = curTime;
					}
				}
			}

		}
		return super.dispatchKeyEvent(event);
	}
*/
}
