package com.atet.tvmarket.control.promotion.activity;

import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.text.ClipboardManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.atet.common.logging.ALog;
import com.atet.tvmarket.R;
import com.atet.tvmarket.app.Constant;
import com.atet.tvmarket.control.common.BaseActivity;
import com.atet.tvmarket.control.home.inf.OnRecyItemClickListener;
import com.atet.tvmarket.control.promotion.decoration.DepositBoxInsetDecoration;
import com.atet.tvmarket.control.promotion.holder.DepositBoxHolder;
import com.atet.tvmarket.entity.dao.UserGameToGift;
import com.atet.tvmarket.utils.ScaleViewUtils;
import com.atet.tvmarket.utils.UIUtils;
import com.atet.tvmarket.view.CloseAcceTextView;
import com.atet.tvmarket.view.NewToast;

/*
 * File：GiftReceived.java
 *
 * Copyright (C) 2015 MainActivity Project
 * Date：2015年6月11日 上午11:40:57
 * All Rights SXHL(New Space) Corporation Reserved.
 * http://www.at-et.cn
 *
 */
public class DepositBoxActivity extends BaseActivity implements OnRecyItemClickListener {
	ALog alog = ALog.getLogger(DepositBoxActivity.class);
	private RecyclerView recyclerView;
	private LinearLayoutManager manager;
	private DepositBoxAdapter adapter;
	private DepositBoxInsetDecoration insetDecoration;
	private CloseAcceTextView deposit_box_tag;
	private List<UserGameToGift> userGameToGiftList;
	private ClipboardManager clip;
	
	private boolean isFocus = true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initView();
		setBlackTitle(false);
	}
	@Override
	protected void onResume() {
		super.onResume();
		setTitleData();
	}
	private void initView() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_deposit_box);
		ScaleViewUtils.init(this);
		ScaleViewUtils.scaleView(getWindow().getDecorView());
		
		
		recyclerView = (RecyclerView) findViewById(R.id.deposit_box_recyclerview);
		deposit_box_tag = (CloseAcceTextView) findViewById(R.id.deposit_box_tag);
		manager = new LinearLayoutManager(getApplicationContext());
		adapter = new DepositBoxAdapter();
		insetDecoration = new DepositBoxInsetDecoration(getApplicationContext());
		
		initData();
		adapter.setOnRecyItemClickListener(this);
		manager.setOrientation(LinearLayoutManager.VERTICAL);
		recyclerView.addItemDecoration(insetDecoration);
		recyclerView.setLayoutManager(manager);
		recyclerView.setAdapter(adapter);
	}
	
	private void initData() {
		userGameToGiftList = (List<UserGameToGift>) getIntent().getSerializableExtra(Constant.USERGIFTINFO);
		alog.info("userGameToGiftList" + userGameToGiftList.size());
		alog.info(userGameToGiftList.toString());
//		this.info = info;
	}

	class DepositBoxAdapter extends Adapter<ViewHolder>{
		OnRecyItemClickListener mListener;
		
		@Override
		public int getItemCount() {
				
				if(userGameToGiftList.size() != 0){
					
					return userGameToGiftList.size();
				}
			return 0;
		}

		@Override
		public void onBindViewHolder(ViewHolder viewHolder, int position) {
			DepositBoxHolder holder = (DepositBoxHolder) viewHolder;
			
			//解决recyclerview的滑到第二个的时候直接跳到第一个
			if(isFocus){
				if(position == 0){
					holder.deposit_box_copy.requestFocus();
				}
				isFocus = false;
			}
			if(userGameToGiftList.size() != 0){
				holder.setData(userGameToGiftList.get(position));
			}
			
//			holder.setData(getIntent().getSerializableExtra(Constant.USERGIFTINFO));
		}

		@Override
		public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
			LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
			View root = inflater.inflate(R.layout.deposit_box_item, viewGroup, false);
			return new DepositBoxHolder(root , mListener,recyclerView);
		}
		
		public void setOnRecyItemClickListener(OnRecyItemClickListener listener){
			this.mListener = listener;
		}
		
	}

	@Override
	public void onItemClick(View view, int position) {
		view.requestFocus();
		clip = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
		
		UserGameToGift userGameToGift = userGameToGiftList.get(position);
		clip.setText(userGameToGift.getUserGiftInfo().getGiftCode()); // 复制
		NewToast.makeToast(getApplicationContext(), UIUtils.getString(R.string.gift_code) + clip.getText(), 0).show();
	}
	
}
