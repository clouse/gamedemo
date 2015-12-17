package com.atet.tvmarket.control.promotion.holder;

import java.util.ArrayList;
import java.util.List;

import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.atet.common.logging.ALog;
import com.atet.tvmarket.R;
import com.atet.tvmarket.control.promotion.activity.IntegralDetailsActivity;
import com.atet.tvmarket.control.promotion.decoration.IntegralThirdInsetDecoration;
import com.atet.tvmarket.entity.UserGoodsOrderInfo;
import com.atet.tvmarket.model.DataFetcher;
import com.atet.tvmarket.model.ReqCallback;
import com.atet.tvmarket.model.task.TaskResult;
import com.atet.tvmarket.utils.ScaleViewUtils;

/*
 * File：IntegralThirdHolder.java
 *
 * Copyright (C) 2015 DeviceIdActivity Project
 * Date：2015年7月17日 下午8:27:02
 * All Rights SXHL(New Space) Corporation Reserved.
 * http://www.at-et.cn
 *
 */
public class IntegralThirdHolder extends ViewHolder implements
		OnFocusChangeListener {
	ALog alog = ALog.getLogger(IntegralDetailsActivity.class);
	private static final int SUCCESS = 0;
	private static final int NULLDATA = 1;
	private static final int NONET = 2;
	public RecyclerView item_recyclerView;
	private LinearLayoutManager manager;
	private IntegralDetailsActivity context;
	private IntegralThirdAdapter mAdapter;
	private IntegralThirdInsetDecoration insetDecoration;
	private TextView tv_warm;
	private LinearLayout item3_progress;
	private List<UserGoodsOrderInfo> infos = new ArrayList<UserGoodsOrderInfo>();

	private Message msg;
	public Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case SUCCESS:
				item3_progress.setVisibility(View.INVISIBLE);
				mAdapter.notifyDataSetChanged();
				break;
			case NULLDATA:
				item3_progress.setVisibility(View.INVISIBLE);
				tv_warm.setText(R.string.no_order);
				tv_warm.setVisibility(View.VISIBLE);

			case NONET:
				item3_progress.setVisibility(View.INVISIBLE);
				tv_warm.setText(R.string.no_order);
				tv_warm.setVisibility(View.VISIBLE);
			default:
				break;
			}
		};
	};

	public IntegralThirdHolder(View itemView, IntegralDetailsActivity context) {
		super(itemView);
		this.context = context;
		ScaleViewUtils.scaleView(itemView);
		initView();
	}

	private void initView() {
		item_recyclerView = (RecyclerView) itemView
				.findViewById(R.id.integral_third_recycler);
		tv_warm = (TextView) itemView.findViewById(R.id.tv_warm);
		item3_progress = (LinearLayout) itemView
				.findViewById(R.id.item3_loading_layout);

		manager = new LinearLayoutManager(context);
		mAdapter = new IntegralThirdAdapter();
		insetDecoration = new IntegralThirdInsetDecoration(context);

		manager.setOrientation(LinearLayoutManager.VERTICAL);
		item_recyclerView.addItemDecoration(insetDecoration);
		item_recyclerView.setLayoutManager(manager);
		item_recyclerView.setAdapter(mAdapter);
		item_recyclerView.setOnFocusChangeListener(this);

		getExchangeData();
	}

	public void getExchangeData() {
		item3_progress.setVisibility(View.VISIBLE);

		// 先检测用户是否登录
		if (!DataFetcher.isUserLogin()) {
			// 提示用户未登录
			item3_progress.setVisibility(View.INVISIBLE);
			tv_warm.setText(R.string.gift_please_login);
			tv_warm.setVisibility(View.VISIBLE);
			return;
		}
		// 用户id
		String userId = DataFetcher.getUserId();
		
		ReqCallback<List<UserGoodsOrderInfo>> reqCallback = new ReqCallback<List<UserGoodsOrderInfo>>() {
			@Override
			public void onGetCacheData(boolean result) {
				super.onGetCacheData(result);
				if (!result) {
					// 不存在缓存数据，需要从网络获取，界面提示正在加载的进度条
					alog.info("no cache data");
				}
			}

			@Override
			public void onResult(TaskResult<List<UserGoodsOrderInfo>> taskResult) {
				int code = taskResult.getCode();
				alog.info("taskResult code:" + code);
				if (code == TaskResult.OK) {
					infos = taskResult.getData();
					if (infos != null && !infos.isEmpty()) {
						sendMessage(SUCCESS);
						alog.info(infos.toString());
					} else {
						sendMessage(NULLDATA);
					}
				} else {
					if (code == TaskResult.HTTP_NO_CONNECTION) {
						sendMessage(NULLDATA);
						alog.info("数据为空0");
					} else {
						sendMessage(NULLDATA);
						alog.info("网络异常1");
					}
				}
			}
		};
		DataFetcher.getUserGoodsOrderInfos(context, reqCallback, userId, false)
				.request(context);
	}

	private void sendMessage(int state) {
		msg = Message.obtain();
		msg.what = state;
		mHandler.sendMessage(msg);
	}

	/*
	 * public void setData(List<UserGoodsOrderInfo> infos) { this.infos = infos;
	 * }
	 */

	class IntegralThirdAdapter extends Adapter {

		@Override
		public int getItemCount() {
			if (infos.size() != 0) {
				/*for(int i = 0; i < infos.size();i++) {
					UserGoodsOrderInfo orderInfo = infos.get(i);
					if(null == orderInfo.getName() || orderInfo.getName().isEmpty()){
						infos.remove(i);
					}
				}*/
				return infos.size();
			}
			return 0;
		}

		@Override
		public void onBindViewHolder(ViewHolder viewHolder, int position) {
			IntegralThirdItemHolder holder = (IntegralThirdItemHolder) viewHolder;
			/*
			 * LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
			 * LayoutParams.MATCH_PARENT);
			 * 
			 * if (position == getItemCount() - 1) { params.bottomMargin =
			 * (int)ScaleViewUtils.resetTextSize(30); }
			 * 
			 * holder.itemView.setLayoutParams(params);
			 */

			if (infos.size() != 0) {
				holder.setData(infos.get(position));
			}

		}

		@Override
		public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
			LayoutInflater inflater = LayoutInflater.from(context);
			View root = inflater.inflate(R.layout.integral_third_item,
					viewGroup, false);
			return new IntegralThirdItemHolder(root, item_recyclerView, context);
		}

	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if (hasFocus) {
			IntegralDetailSecondHolder.isLeft = false;
			if (v.getId() == item_recyclerView.getId()) {
				if (item_recyclerView.getChildAt(0) != null) {
					RelativeLayout rl = (RelativeLayout) item_recyclerView
							.getChildAt(0).findViewById(R.id.integral_third);
					rl.requestFocus();
				} else {

				}
			}
		} else {

		}

	}

}
