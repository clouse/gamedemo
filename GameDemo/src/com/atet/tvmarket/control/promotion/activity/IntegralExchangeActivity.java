package com.atet.tvmarket.control.promotion.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RelativeLayout.LayoutParams;

import com.atet.common.logging.ALog;
import com.atet.tvmarket.R;
import com.atet.tvmarket.app.Constant;
import com.atet.tvmarket.control.common.BaseActivity;
import com.atet.tvmarket.control.home.decoration.PromotionAreaInsetDecoration;
import com.atet.tvmarket.control.home.inf.OnRecyItemClickListener;
import com.atet.tvmarket.control.promotion.holder.IntegralExchangeHolder;
import com.atet.tvmarket.control.promotion.view.RewardExchangeView;
import com.atet.tvmarket.entity.dao.GoodsDetailPhoto;
import com.atet.tvmarket.entity.dao.GoodsInfo;
import com.atet.tvmarket.model.DataConfig;
import com.atet.tvmarket.model.DataFetcher;
import com.atet.tvmarket.model.ReqCallback;
import com.atet.tvmarket.model.task.TaskResult;
import com.atet.tvmarket.utils.GamepadTool;
import com.atet.tvmarket.utils.NetUtil;
import com.atet.tvmarket.utils.ScaleViewUtils;
import com.atet.tvmarket.utils.UIUtils;
import com.atet.tvmarket.utils.UmengUtils;
import com.atet.tvmarket.view.LoadingView;

/*
 * File：PromotionArea.java
 *
 * Copyright (C) 2015 MainActivity Project
 * Date：2015年6月10日 下午2:29:28
 * All Rights SXHL(New Space) Corporation Reserved.
 * http://www.at-et.cn
 *
 */
public class IntegralExchangeActivity extends BaseActivity implements
		OnRecyItemClickListener {
	ALog alog = ALog.getLogger(IntegralExchangeActivity.class);
	private RecyclerView recyclerView;
	private LinearLayoutManager manager;
	private IntegralExchangeAdapter mAdapter;
	private PromotionAreaInsetDecoration insertDecoration;
	private boolean isFirstFocus = true;
	private List<GoodsInfo> infos = new ArrayList<GoodsInfo>();
	private IntegralExchangeHolder holder;
	private RewardExchangeView rewardView;
	private List<String> scrollStr = new ArrayList<String>();

	private LoadingView loadingView;

	private Handler mHander = new Handler() {
		public void handleMessage(Message msg) {
			// 遍历取出图片的数据
			for (int i = 0; i < infos.size(); i++) {
				GoodsInfo goodsInfo = infos.get(i);
				scrollStr.add(goodsInfo.getName());
				List<GoodsDetailPhoto> detailPhotos = goodsInfo
						.getDetailPhotos();
				for (int j = 0; j < detailPhotos.size(); j++) {
					alog.info("---- " + detailPhotos.get(j).toString());
				}
				rewardView.setGoods(scrollStr,false);
			}

			mAdapter.setData();
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initView();
		setBlackTitle(true);
	}

	@Override
	protected void onResume() {
		super.onResume();
		setTitleData();
	}

	private void initView() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_integral_exchange);
		ScaleViewUtils.init(this);
		ScaleViewUtils.scaleView(getWindow().getDecorView());
		recyclerView = (RecyclerView) findViewById(R.id.integral_exchange_recycler_view);
		rewardView = (RewardExchangeView) findViewById(R.id.rewardexchange);

		loadingView = (LoadingView) findViewById(R.id.integral_contentLoading);
		loadingView.setDataView(recyclerView);

		manager = new LinearLayoutManager(getApplicationContext());
		mAdapter = new IntegralExchangeAdapter();
		insertDecoration = new PromotionAreaInsetDecoration(this);

		mAdapter.setOnRecyClickListener(this);
		manager.setOrientation(LinearLayoutManager.HORIZONTAL);
		recyclerView.addItemDecoration(insertDecoration);
		recyclerView.setLayoutManager(manager);
		recyclerView.setAdapter(mAdapter);

		rewardView.setDesc(UIUtils.getString(R.string.has_exchange));
		loadData(false);
	}

	private void loadData(boolean isRefresh) {
		infos.clear();
		loadingView.getmHandler().sendEmptyMessage(Constant.SHOWLOADING);

		ReqCallback<List<GoodsInfo>> reqCallback = new ReqCallback<List<GoodsInfo>>() {
			@Override
			public void onResult(TaskResult<List<GoodsInfo>> taskResult) {
				int code = taskResult.getCode();
				alog.info("taskResult code:" + code);
				if (code == TaskResult.OK) {
					infos = taskResult.getData();
					if (infos != null && !infos.isEmpty()) {
						alog.info(infos.toString());
						alog.info("size = " + infos.size());
						mHander.sendEmptyMessage(0);
						loadingView.getmHandler().sendEmptyMessage(
								Constant.DISMISSLOADING);
					} else {
						if (NetUtil.isNetworkAvailable(
								IntegralExchangeActivity.this, true)) {
							alog.info("数据为空0");
							loadingView.getmHandler().sendEmptyMessage(
									Constant.NULLDATA);
						} else {
							alog.info("网络异常1");
							loadingView.getmHandler().sendEmptyMessage(
									Constant.EXCEPTION);
						}
					}
				} else {
					alog.info("failed");
					if (NetUtil.isNetworkAvailable(
							IntegralExchangeActivity.this, true)) {
						alog.info("数据为空0");
						loadingView.getmHandler().sendEmptyMessage(
								Constant.NULLDATA);
					} else {
						alog.info("网络异常1");
						loadingView.getmHandler().sendEmptyMessage(
								Constant.EXCEPTION);
					}
				}
			}

			@Override
			public void onUpdate(TaskResult<List<GoodsInfo>> taskResult) {

			}
		};
		if(isRefresh){
			
			DataFetcher.getGoods(getApplicationContext(), DataConfig.GOODS_TYPE_ALL,
					reqCallback, false).registerUpdateListener(reqCallback)
					.refresh(getApplicationContext());
		}else{
			DataFetcher.getGoods(getApplicationContext(), DataConfig.GOODS_TYPE_ALL,
					reqCallback, false).registerUpdateListener(reqCallback)
					.request(getApplicationContext());
			
		}
	}

	class IntegralExchangeAdapter extends Adapter<ViewHolder> {
		private OnRecyItemClickListener mListener;

		@Override
		public int getItemCount() {
			// TODO Auto-generated method stub
			if (infos.size() != 0) {
				return infos.size();
			}
			return 0;
		}

		public void setData() {
			recyclerView.setAdapter(this);
		}

		@Override
		public void onBindViewHolder(ViewHolder viewholder, int position) {
			holder = (IntegralExchangeHolder) viewholder;
			LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);

			if (position == 0) {
				params.leftMargin = (int) ScaleViewUtils.resetTextSize(80);
				holder.item_content.requestFocus();
			}

			if (position == getItemCount() - 1) {
				params.rightMargin = (int) ScaleViewUtils.resetTextSize(150);
			}

			holder.itemView.setLayoutParams(params);

			if (infos.size() != 0) {
				holder.setData(infos.get(position));
			}
		}

		@Override
		public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
			LayoutInflater inflater = LayoutInflater.from(UIUtils.getContext());
			View root = inflater.inflate(R.layout.integral_exchange_item,
					viewGroup, false);
			return new IntegralExchangeHolder(root, recyclerView, mListener);
		}

		public void setOnRecyClickListener(OnRecyItemClickListener listener) {
			this.mListener = listener;
		}
	}

	@Override
	public void onItemClick(View view, int position) {
		view.requestFocus();
		// Umeng统计"活动>积分兑换>物品"点击次数
		UmengUtils.setOnEvent(IntegralExchangeActivity.this,
				UmengUtils.PROMOTION_INTEGRAL_GOODS_CLICK);
		Intent intent = new Intent(this, IntegralDetailsActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable(Constant.GOODSINFO, infos.get(position));
		intent.putExtras(bundle);
		startActivity(intent);
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		int action = event.getAction();
		int keyCode = event.getKeyCode();

		// X键刷新
		if (action == KeyEvent.ACTION_DOWN) {
			if (GamepadTool.isButtonX(keyCode)
					|| keyCode == KeyEvent.KEYCODE_MENU) {
				loadData(true);
				return true;
			}
		}
		return super.dispatchKeyEvent(event);
	}

	/*
	 * class RecyclerViewFocus implements OnFocusChangeListener{
	 * 
	 * @Override public void onFocusChange(View v, boolean hasFocus) { if(view
	 * != null){ if(hasFocus){ View view =
	 * recyclerView.getChildAt(0).findViewById
	 * (R.id.integral_exchange_item_content); view.requestFocus(); } } } }
	 */
}
