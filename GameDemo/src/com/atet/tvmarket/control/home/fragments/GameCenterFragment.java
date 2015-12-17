package com.atet.tvmarket.control.home.fragments;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.atet.common.logging.ALog;
import com.atet.tvmarket.R;
import com.atet.tvmarket.app.Constant;
import com.atet.tvmarket.control.gamerecommand.NewGameRecommandActivity;
import com.atet.tvmarket.control.home.activity.JoyPadBuyActivity;
import com.atet.tvmarket.control.home.adapter.GameCeneterAdapter;
import com.atet.tvmarket.control.home.decoration.GameCenterInsetDecoration;
import com.atet.tvmarket.control.home.inf.OnRecyItemClickListener;
import com.atet.tvmarket.control.home.view.GameCenterFirstPanel;
import com.atet.tvmarket.control.mygame.activity.MyGameActivity;
import com.atet.tvmarket.control.search.SearchActivity;
import com.atet.tvmarket.entity.dao.AdInfo;
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

public class GameCenterFragment extends TabFragment implements
		OnRecyItemClickListener {
	ALog alog = ALog.getLogger(GameCenterFragment.class);
	private static final int ALL_DATA = 0;
	private static final int UPDATA_DATA = 1;
	private TvRecyclerView mRecyclerView;
	private LinearLayoutManager mManager;
	private GameCeneterAdapter adapter;
	private GameCenterInsetDecoration insetDecoration;
	private Context context;
	private List<AdInfo> infos = new ArrayList<AdInfo>();
	private IntentFilter mFilter;
	private boolean isIntercepet = true;
	// private GameCenterFirstPanel firstPanel;

	private LoadingView loadingView;

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			// mAdapter.setData(gameTypeInfos);
			switch (msg.what) {
			case ALL_DATA:
				adapter.setData(infos);
				getNewContentInterface();
				break;
			case UPDATA_DATA:
				adapter.checkUpdata((List<String>)msg.obj);
				break;
			default:
				break;
			}
			// getNewContentInterface();
		}
	};

	@Override
	protected View initView() {
		context = getActivity();
		View view = View.inflate(context, R.layout.game_center_fragment, null);
		ScaleViewUtils.scaleView(view);
		mRecyclerView = (TvRecyclerView) view
				.findViewById(R.id.gamecenter_recycler_view);
		loadingView = (LoadingView) view.findViewById(R.id.game_center_Loading);
		loadingView.setDataView(mRecyclerView);
		mManager = new LinearLayoutManager(getActivity());
		adapter = new GameCeneterAdapter(mRecyclerView, context, infos);
		insetDecoration = new GameCenterInsetDecoration(getActivity());

		mRecyclerView.addItemDecoration(insetDecoration);
		mManager.setOrientation(LinearLayoutManager.HORIZONTAL);
		mRecyclerView.setLayoutManager(mManager);
		mRecyclerView.setAdapter(adapter);
		adapter.setOnRecyItemClickListener(this);

		mFilter = new IntentFilter();
		mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);

		// firstPanel = (GameCenterFirstPanel) mRecyclerView.getChildAt(0);
		loadData(false);
		// NetUtil.isWifiOpen(context);
		return view;
	}

	public void onItemClick(View view, int position) {
		// Umeng统计所有交互
		UmengUtils.setOnEvent(getActivity(), UmengUtils.GAMECENTER_INTERACTION);
		view.requestFocus();
		GameCenterFirstPanel firstPanel = (GameCenterFirstPanel) mRecyclerView
				.getChildAt(0);
		if (view.getId() == R.id.game_center_game_recommend) {
			// Umeng"新品推荐"统计
			UmengUtils.setOnEvent(getActivity(),UmengUtils.GAMECENTER_NEW_CLICK);
			// DataFetcher.getAppConfig().isGameRecommendExist()
			
			if(firstPanel.getGame_recommend_new().isShown()){
				firstPanel.getGame_recommend_new().setVisibility(View.INVISIBLE);
				DataFetcher.removeNewContentInterface(UIUtils.getContext(),UpdateInterface.GAME_NEW_UPLOADED);
			}
			
			Intent intent = null;
			if (DataFetcher.getAppConfig().isGameRecommendExist()) {
				intent = new Intent(getActivity(),NewGameRecommandActivity.class);
			} else {
				intent = new Intent(getActivity(), JoyPadBuyActivity.class);
			}
			startActivity(intent);
		} else if (view.getId() == R.id.game_center_quick_start) {
			// Umeng快速开始统计
			UmengUtils.setOnEvent(getActivity(),UmengUtils.GAMECENTER_QUICKSTART_CLICK);
			//设置更新标识不可见
			if(firstPanel.getQuick_start_new().isShown()){
				firstPanel.getQuick_start_new().setVisibility(View.INVISIBLE);
				DataFetcher.removeNewDownloadGame();
			}
			
			Intent intent = new Intent(getActivity(), MyGameActivity.class);
			startActivity(intent);
		} else if (view.getId() == R.id.game_center_game_search) {
			UmengUtils.setOnEvent(getActivity(),
					UmengUtils.GAMECENTER_SEARCH_CLICK);
			Intent intent = new Intent(getActivity(), SearchActivity.class);
			startActivity(intent);
		}
	}

	/**
	 * 
	 * @description: 去网络获取数据
	 * 
	 * @throws:
	 * @author: LiJie
	 * @date: 2015年8月10日 下午2:28:19
	 */
	public void loadData(boolean isRefresh) {

		loadingView.getmHandler().sendEmptyMessage(Constant.SHOWLOADING);

		ReqCallback<List<AdInfo>> reqCallback = new ReqCallback<List<AdInfo>>() {
			@Override
			public void onResult(TaskResult<List<AdInfo>> taskResult) {
				int code = taskResult.getCode();
				alog.info("taskResult code:" + code);
				if (code == TaskResult.OK) {
					infos = taskResult.getData();
					if (infos != null && !infos.isEmpty()) {
						alog.info(infos.toString());
						alog.info("infos.size = " + infos.size());
						mHandler.sendEmptyMessage(ALL_DATA);

						loadingView.getmHandler().sendEmptyMessage(
								Constant.DISMISSLOADING);
					} else {
						loadingView.getmHandler().sendEmptyMessage(
								Constant.DISMISSLOADING);
					}
				} else {
					if (NetUtil.isNetworkAvailable(context, true)) {
						loadingView.getmHandler().sendEmptyMessage(
								Constant.DISMISSLOADING);
					} else {
						loadingView.getmHandler().sendEmptyMessage(
								Constant.DISMISSLOADING);
					}
				}
			}

			@Override
			public void onUpdate(TaskResult<List<AdInfo>> taskResult) {

			}
		};
		if (isRefresh) {
			DataFetcher.getGameCenterInfo(context, reqCallback, false)
					.registerUpdateListener(reqCallback).refresh(context);
		} else {
			DataFetcher.getGameCenterInfo(context, reqCallback, false)
					.registerUpdateListener(reqCallback).request(context);
		}
	}

	/**
	 * 监听网络
	 */
	private BroadcastReceiver myNetReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			alog.info("onReceive");
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo netInfo = mConnectivityManager.getActiveNetworkInfo();
			if (netInfo != null) {
				if (netInfo.isConnectedOrConnecting()) {
					if (!isIntercepet) {
						loadData(false);
						isIntercepet = true;
					}
				}
				alog.info("net conn");
			} else {
				isIntercepet = false;
			}
		}
	};

	private void getNewContentInterface() {
		final List<String> interfaceList = DataHelper.getNewContentInterface();
		if (interfaceList == null) {
			ReqCallback<List<String>> reqCallback = new ReqCallback<List<String>>() {
				@Override
				public void onResult(TaskResult<List<String>> taskResult) {
					int code = taskResult.getCode();
					alog.info("taskResult code:" + code);
					if (code == TaskResult.OK) {
						sendMessage(interfaceList);
						// checkNewContentInterface(taskResult.getData());
					}
				}
			};
			DataFetcher.getNewContentInterface(context, reqCallback, true);
		} else {
			sendMessage(interfaceList);
			// checkNewContentInterface(interfaceList);
		}
	}
	
	private void sendMessage(List<String> interfaceList) {
		Message msg = Message.obtain();
		msg.what = UPDATA_DATA;
		msg.obj = interfaceList;
		mHandler.sendMessage(msg);
	}

	public RecyclerView getRecyclerVew() {
		return mRecyclerView;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		context.registerReceiver(myNetReceiver, mFilter);
		// Umeng页面统计
		UmengUtils.onPageStart(UmengUtils.GAMECENTER_FRAGMENT);
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		context.unregisterReceiver(myNetReceiver);
		// Umeng页面统计
		UmengUtils.onPageEnd(UmengUtils.GAMECENTER_FRAGMENT);
	}
}
