package com.atet.tvmarket.control.promotion.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout.LayoutParams;

import com.atet.common.logging.ALog;
import com.atet.tvmarket.R;
import com.atet.tvmarket.app.Constant;
import com.atet.tvmarket.control.common.BaseFragment;
import com.atet.tvmarket.control.home.inf.OnRecyItemClickListener;
import com.atet.tvmarket.control.promotion.activity.GiftActivity;
import com.atet.tvmarket.control.promotion.activity.IntegralExchangeActivity;
import com.atet.tvmarket.control.promotion.activity.PromotionAreaActivity;
import com.atet.tvmarket.control.promotion.activity.PromotionDetailsActivity;
import com.atet.tvmarket.control.promotion.decoration.PromotionInsetDecoration;
import com.atet.tvmarket.control.promotion.holder.PromotionFirstHolder;
import com.atet.tvmarket.control.promotion.holder.PromotionSecondHolder;
import com.atet.tvmarket.control.promotion.holder.PromotionThirdHolder;
import com.atet.tvmarket.control.promotion.holder.PromotionfourthHolder;
import com.atet.tvmarket.control.promotion.panel.PromotionFirstPanel;
import com.atet.tvmarket.control.task.TaskActivity;
import com.atet.tvmarket.entity.dao.ActDetailPhoto;
import com.atet.tvmarket.entity.dao.ActInfo;
import com.atet.tvmarket.entity.dao.GoodsDetailPhoto;
import com.atet.tvmarket.entity.dao.GoodsInfo;
import com.atet.tvmarket.model.DataConfig;
import com.atet.tvmarket.model.DataConfig.UpdateInterface;
import com.atet.tvmarket.model.DataFetcher;
import com.atet.tvmarket.model.DataHelper;
import com.atet.tvmarket.model.ReqCallback;
import com.atet.tvmarket.model.task.TaskResult;
import com.atet.tvmarket.utils.NetUtil;
import com.atet.tvmarket.utils.ScaleViewUtils;
import com.atet.tvmarket.utils.UIUtils;
import com.atet.tvmarket.utils.UmengUtils;
import com.atet.tvmarket.view.LoadingView;
import com.atet.tvmarket.view.TvRecyclerView;

/*
 * File：PageFragment.java
 *
 * Copyright (C) 2015 SmartTabLayoutDemo Project
 * Date：2015年5月21日 下午12:05:00
 * All Rights SXHL(New Space) Corporation Reserved.
 * http://www.at-et.cn
 *
 */

/**
 * @description: 活动首页
 * 
 * @author: LiJie
 * @date: 2015年5月28日 下午5:52:00
 */
public class PromotionFragment extends BaseFragment implements
		OnRecyItemClickListener {
	ALog alog = ALog.getLogger(PromotionFragment.class);
	private static final int ACTINFOMSG = 0;
	private static final int GOODINFOMSG = 1;
	private static final int DATA_UPDATE = 2;

	private TvRecyclerView mRecyclerView;
	private LinearLayoutManager mManager;
	private PromotionAdapter mAdapter;
	private PromotionInsetDecoration insetDecoration;
	private PromotionThirdHolder thirdHolder;
	private Context context;
	private List<GoodsInfo> goodsInfos = new ArrayList<GoodsInfo>();
	private List<ActInfo> actInfos = new ArrayList<ActInfo>();
	private List<ActInfo> thirdHolderData = new ArrayList<ActInfo>();
	private List<ActInfo> fourthHolderData = new ArrayList<ActInfo>();
	private List<String> interfaceList;
	
	//private PromotionFirstPanel firstPanel;

	Message msg;
	private LoadingView loadingView;
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			// mAdapter.setData(gameTypeInfos);
			switch (msg.what) {
			case ACTINFOMSG:
				// 遍历取数据
				for (int i = 0; i < actInfos.size(); i++) {
					ActInfo actInfo = actInfos.get(i);
					System.out.println("+++"+actInfo.toString());
					List<ActDetailPhoto> detailPhotos = actInfo.getDetailPhotos();
					for (int j = 0; j < detailPhotos.size(); j++) {
						alog.info("---- " + detailPhotos.get(j).toString());
					}
				}
				sortActData();
				break;
			case GOODINFOMSG:
				// 遍历取数据
				for (int i = 0; i < goodsInfos.size(); i++) {
					GoodsInfo goodsInfo = goodsInfos.get(i);
					System.out.println("---"+goodsInfo.toString());
					List<GoodsDetailPhoto> detailPhotos = goodsInfo
							.getDetailPhotos();
					for (int j = 0; j < detailPhotos.size(); j++) {
						alog.info("---- " + detailPhotos.get(j).toString());
					}
				}
				
				break;
			case DATA_UPDATE:
				
				break;

			default:
				break;
			}
			mAdapter.setData();
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		context = getActivity();
		View view = View.inflate(context, R.layout.promotion_fragment, null);
		ScaleViewUtils.scaleView(view);
		mRecyclerView = (TvRecyclerView) view
				.findViewById(R.id.promotion_recycler_view);
		loadingView = (LoadingView) view
				.findViewById(R.id.promotion_contentLoading);
		loadingView.setDataView(mRecyclerView);
		mManager = new LinearLayoutManager(getActivity());
		mAdapter = new PromotionAdapter();
		insetDecoration = new PromotionInsetDecoration(getActivity());

		loadRecommendData(false);
		loadActData(false);

		mManager.setOrientation(LinearLayoutManager.HORIZONTAL);
		mRecyclerView.setLayoutManager(mManager);
		mRecyclerView.setAdapter(mAdapter);
		mRecyclerView.addItemDecoration(insetDecoration);
		mAdapter.setOnRecyItemClickListener(this);
		getNewContentInterface();
		
		
		return view;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// Umeng页面统计
		UmengUtils.onPageStart(UmengUtils.PROMOTION_FRAGMENT);
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		// Umeng页面统计
		UmengUtils.onPageEnd(UmengUtils.PROMOTION_FRAGMENT);
	}

	/**
	 * 
	 * @description: 对数据进行分类
	 * 
	 * @throws:
	 * @author: LiJie
	 * @date: 2015年8月10日 下午2:31:52
	 */
	private void sortActData() {
		thirdHolderData.clear();
		fourthHolderData.clear();
		alog.info("actInfos.size()" + actInfos.size());
		if (actInfos.size() > 0) {

			for (int i = 0; i < actInfos.size(); i++) {
				if ((i - 2) % 3 == 0) {
					thirdHolderData.add(actInfos.get(i));
				} else {
					fourthHolderData.add(actInfos.get(i));
				}
			}

		}

	}

	/**
	 * 
	 * @description: 加载活动数据
	 * 
	 * @throws:
	 * @author: LiJie
	 * @date: 2015年7月21日 下午8:38:53
	 */
	public void loadActData(boolean isRefresh) {
		msg = Message.obtain();
		msg.obj = mRecyclerView;
		msg.what = Constant.SHOWLOADING;
		loadingView.getmHandler().sendMessage(msg);

		ReqCallback<List<ActInfo>> reqCallback = new ReqCallback<List<ActInfo>>() {
			@Override
			public void onResult(TaskResult<List<ActInfo>> taskResult) {
				int code = taskResult.getCode();
				alog.info("taskResult code:" + code);
				if (code == TaskResult.OK) {
					actInfos = taskResult.getData();
					if (actInfos != null && !actInfos.isEmpty()) {
						alog.info(actInfos.toString());
						
						msg = Message.obtain();
						msg.what = ACTINFOMSG;
						mHandler.sendMessage(msg);

						msg = Message.obtain();
						msg.obj = mRecyclerView;
						msg.what = Constant.DISMISSLOADING;
						loadingView.getmHandler().sendMessage(msg);
					} else {
						msg = Message.obtain();
						msg.obj = mRecyclerView;
						msg.what = Constant.DISMISSLOADING;
						loadingView.getmHandler().sendMessage(msg);
					}
				} else {
					String errorMsg = taskResult.getMsg();
					if (NetUtil.isNetworkAvailable(context, true)) {
						msg = Message.obtain();
						msg.what = Constant.DISMISSLOADING;
						msg.obj = mRecyclerView;
						loadingView.getmHandler().sendMessage(msg);
					} else {
						msg = Message.obtain();
						msg.obj = mRecyclerView;
						msg.what = Constant.DISMISSLOADING;
						loadingView.getmHandler().sendMessage(msg);
					}
				}

			}

			@Override
			public void onUpdate(TaskResult<List<ActInfo>> taskResult) {

			}
		};
		if(isRefresh){
			DataFetcher.getAct(getActivity(), DataConfig.ACTIVITY_TYPE_RECOMMEND,reqCallback, false)
						.registerUpdateListener(reqCallback).refresh(getActivity());
		}else{
			DataFetcher.getAct(getActivity(), DataConfig.ACTIVITY_TYPE_RECOMMEND,reqCallback, false)
						.registerUpdateListener(reqCallback).request(getActivity());
		}
	}

	/**
	 * 
	 * @description: 加载商品数据
	 * 
	 * @throws:
	 * @author: LiJie
	 * @date: 2015年7月21日 下午8:38:07
	 */
	public void loadRecommendData(boolean isRefresh) {
		ReqCallback<List<GoodsInfo>> reqCallback = new ReqCallback<List<GoodsInfo>>() {
			@Override
			public void onResult(TaskResult<List<GoodsInfo>> taskResult) {
				int code = taskResult.getCode();
				alog.info("taskResult code:" + code);
				if (code == TaskResult.OK) {
					goodsInfos = taskResult.getData();
					if (goodsInfos != null && !goodsInfos.isEmpty()) {

						// 遍历取出图片
						for (int i = 0; i < goodsInfos.size(); i++) {
							msg = Message.obtain();
							msg.what = GOODINFOMSG;
							mHandler.sendMessage(msg);
						}
					}
				} else {
					alog.info("failed");
				}
				// mAdapter.setData();
			}

			@Override
			public void onUpdate(TaskResult<List<GoodsInfo>> taskResult) {

			}
		};
		if(isRefresh){
			DataFetcher.getGoods(getActivity(), DataConfig.GOODS_TYPE_RECOMMEND,reqCallback, false)
			.registerUpdateListener(reqCallback)
			.refresh(getActivity());
		}else{
			DataFetcher.getGoods(getActivity(), DataConfig.GOODS_TYPE_RECOMMEND,reqCallback, false)
			.registerUpdateListener(reqCallback)
			.request(getActivity());
		}
	}

	public RecyclerView getRecyclerView() {
		return mRecyclerView;
	}
	
	public void getNewContentInterface(){
		
		interfaceList = DataHelper.getNewContentInterface();
		if(interfaceList==null){
			ReqCallback<List<String>> reqCallback = new ReqCallback<List<String>>() {
				@Override
				public void onResult(TaskResult<List<String>> taskResult) {
					int code = taskResult.getCode();
					alog.info("taskResult code:" + code);
					if (code == TaskResult.OK) {
							mHandler.sendEmptyMessage(DATA_UPDATE);
						//checkNewContentInterface(taskResult.getData());
					}

				}
			};
			DataFetcher.getNewContentInterface(context, reqCallback, true);
		} else {
			mHandler.sendEmptyMessage(DATA_UPDATE);
		}
	}
	
	
	private View getThirdHolder(int index){
		View view = mRecyclerView.getLayoutManager().findViewByPosition(index);
		View more_game_new = view.findViewById(R.id.promotion_more_game_new);
		return more_game_new ;
	}

	class PromotionAdapter extends Adapter<ViewHolder> {
		private int viewType;
		private OnRecyItemClickListener mListener;

		@Override
		public int getItemCount() {
			int size = actInfos.size();
			if (size != 0) {
				if (size % 3 == 0) {
					return 4 + size / 3 * 2;
				} else {
					return 4 + (int) Math.floor(size / 3) * 2 + 1;
				}
			}
			return 4;
		}

		public void setData() {
			notifyDataSetChanged();
		}

		@Override
		public int getItemViewType(int position) {
			switch (position) {
			case 0:
				viewType = Constant.VIEWHOLDER_TYPE_0;
				break;
			case 1:
				viewType = Constant.VIEWHOLDER_TYPE_1;
			default:
				if (position > 1) {
					if (position > 3 && position % 2 == 0) {
						viewType = Constant.VIEWHOLDER_TYPE_3;
					} else {
						viewType = Constant.VIEWHOLDER_TYPE_2;
					}
				}
				break;

			}
			return viewType;
		}

		@Override
		public void onBindViewHolder(ViewHolder viewHolder, int position) {

			if (position == 0) {
				PromotionFirstHolder firstHolder = (PromotionFirstHolder) viewHolder;
				if(interfaceList != null && interfaceList.size() != 0){
					firstHolder.checkUpdate(interfaceList);
				}
			}

			if (position == 1) {
				PromotionSecondHolder holder = (PromotionSecondHolder) viewHolder;

				if (goodsInfos.size() != 0) {
					holder.setData(goodsInfos);
				}
			}

			if (position > 1) {

				if (position == 2 || position == 3) {
					thirdHolder = (PromotionThirdHolder) viewHolder;
					LayoutParams params = new LayoutParams(
							LayoutParams.MATCH_PARENT,
							LayoutParams.MATCH_PARENT);

					if (position == 2) {
						thirdHolder.more_game
								.setNextFocusLeftId(R.id.promotion_carousel_game);
						params.leftMargin = -(int) ScaleViewUtils
								.resetTextSize(20);
					}

					if (position == 3) {
						params.leftMargin = -(int) ScaleViewUtils.resetTextSize(5);
					}

					thirdHolder.itemView.setLayoutParams(params);
				}

				if (position == 2 || position == 3) {
					thirdHolder = (PromotionThirdHolder) viewHolder;
					thirdHolder.setDefaultData(position);
					if(interfaceList != null && interfaceList.size() != 0){
						thirdHolder.checkUpdate(interfaceList,position);
					}
				}

				// 两个方图
				if (position > 3) {
					if (position % 2 == 0) {
						PromotionfourthHolder fourthHolder = (PromotionfourthHolder) viewHolder;
						if (fourthHolderData.size() != 0) {
							fourthHolder.setData(fourthHolderData, position);
						}
					} else {
						thirdHolder = (PromotionThirdHolder) viewHolder;
						if (thirdHolderData.size() != 0) {
							alog.info("fourthHolderData" + "---"
									+ fourthHolderData.size() + "-----"
									+ position + "------" + (position - 5) / 2);
							thirdHolder.setData(thirdHolderData
									.get((position - 5) / 2));
						}
					}
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
				root = inflater.inflate(R.layout.promotion_first_panel,
						viewGroup, false);
				holder = new PromotionFirstHolder(root, mRecyclerView,
						mListener);
				break;
			case Constant.VIEWHOLDER_TYPE_1:
				root = inflater.inflate(R.layout.promotion_item2, viewGroup,
						false);
				holder = new PromotionSecondHolder(root, mRecyclerView,
						mListener, context);
				;
				break;
			case Constant.VIEWHOLDER_TYPE_2:
				root = inflater.inflate(R.layout.promotion_item3, viewGroup,
						false);
				holder = new PromotionThirdHolder(root, mRecyclerView,
						mListener, context);
				break;
			case Constant.VIEWHOLDER_TYPE_3:
				root = inflater.inflate(R.layout.promotion_fourth_panel,
						viewGroup, false);
				holder = new PromotionfourthHolder(root, mRecyclerView,
						mListener, context);
				break;
			default:
				break;
			}
			return holder;
		}

		public void setOnRecyItemClickListener(OnRecyItemClickListener listener) {
			this.mListener = listener;
		}
	}

	@Override
	public void onItemClick(View view, int position) {
		view.requestFocus();
		// Umeng统计交互事件
		UmengUtils.setOnEvent(context, UmengUtils.PROMOTION_INTERACTION);
		if (view.getId() == R.id.promotion_area) {
			PromotionFirstPanel firstPanel = (PromotionFirstPanel) mRecyclerView.getChildAt(0);
			if(firstPanel.getPromotion_area_new().isShown()){
				firstPanel.getPromotion_area_new().setVisibility(View.INVISIBLE);
				DataFetcher.removeNewContentInterface(UIUtils.getContext(), UpdateInterface.GAME_ACTIVITY);
			}
			
			// Umeng统计进入到"活动专区"的次数
			UmengUtils.setOnEvent(context, UmengUtils.PROMOTION_AREA_ENTER);
			Intent intent = new Intent(getActivity(),
					PromotionAreaActivity.class);
			startActivity(intent);
		}
		if (view.getId() == R.id.promotion_integral_exchange) {
			PromotionFirstPanel firstPanel = (PromotionFirstPanel) mRecyclerView.getChildAt(0);
			if(firstPanel.getIntegral_exchange_new().isShown()){
				firstPanel.getIntegral_exchange_new().setVisibility(View.INVISIBLE);
				DataFetcher.removeNewContentInterface(UIUtils.getContext(), UpdateInterface.GAME_GOODS);
			}
			
			UmengUtils.setOnEvent(context,
					UmengUtils.PROMOTION_INTEGRAL_EXCHANGE_ENTER);
			Intent intent = new Intent(getActivity(),
					IntegralExchangeActivity.class);
			startActivity(intent);
		}

		if (position == 2) {
			if(getThirdHolder(position).isShown()){
				DataFetcher.removeNewContentInterface(context, UpdateInterface.USER_TASK);
				getThirdHolder(position).setVisibility(View.INVISIBLE);
			}
			Intent intent = new Intent(getActivity(), TaskActivity.class);
			startActivity(intent);
		} else if (position == 3) {
			if(getThirdHolder(position).isShown()){
				getThirdHolder(position).setVisibility(View.INVISIBLE);
				DataFetcher.removeNewContentInterface(context, UpdateInterface.GAME_GIFT);
			}
			
			// Umeng统计点击进入“领取礼包”
			UmengUtils.setOnEvent(getActivity(),
					UmengUtils.PROMOTION_GIFT_RECEIVE_CLICK);
			Intent intent = new Intent(getActivity(), GiftActivity.class);
			startActivity(intent);
			
		} else if (position > 3) {
			Intent intent = new Intent(context, PromotionDetailsActivity.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable(Constant.ACTINFO,thirdHolderData.get((position - 5) / 2));
			intent.putExtras(bundle);
			context.startActivity(intent);
			
		}
	}

}
