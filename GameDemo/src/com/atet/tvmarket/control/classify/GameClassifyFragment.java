package com.atet.tvmarket.control.classify;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ItemDecoration;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.atet.common.logging.ALog;
import com.atet.tvmarket.R;
import com.atet.tvmarket.app.BaseApplication;
import com.atet.tvmarket.app.Constant;
import com.atet.tvmarket.control.home.MainActivity;
import com.atet.tvmarket.entity.dao.GameInfo;
import com.atet.tvmarket.entity.dao.GameTypeInfo;
import com.atet.tvmarket.model.DataFetcher;
import com.atet.tvmarket.model.DataHelper;
import com.atet.tvmarket.model.DataRequester;
import com.atet.tvmarket.model.ReqCallback;
import com.atet.tvmarket.model.DataConfig.UpdateInterface;
import com.atet.tvmarket.model.task.TaskResult;
import com.atet.tvmarket.utils.NetUtil;
import com.atet.tvmarket.utils.ScaleViewUtils;
import com.atet.tvmarket.utils.UmengUtils;
import com.atet.tvmarket.view.LoadingView;
import com.atet.tvmarket.view.recyclerview.BaseAdapter;
import com.atet.tvmarket.view.recyclerview.RecyclerFragment;

/**
 * @author liuhongtao desc: 游戏分类fragment createdtime 20150528
 */
public class GameClassifyFragment extends RecyclerFragment {

	ALog alog = ALog.getLogger(GameClassifyFragment.class);
	private BaseApplication mApp;
	private MainActivity context;
	private RecyclerView recyclerView;
	private GameClassifyAdapter mAdapter;
	private List<GameTypeInfo> gameTypeInfos = new ArrayList<GameTypeInfo>();
	private List<String> myInterfaceList = new ArrayList<String>();
	List<GameInfo> games = new ArrayList<GameInfo>();
	private GameClassifyRankingPanel rankingPanel;

	private LoadingView loadingView;

	public GameClassifyFragment newInstance() {
		GameClassifyFragment fragment = new GameClassifyFragment();
		Bundle args = new Bundle();
		fragment.setArguments(args);
		return fragment;
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				getNewContentInterface();
				break;
			case 1:
				int flag = (Integer) msg.obj;
				if(flag==0){
					getData(false);
				}
				else{
					getData(true);
				}
				break;
			case 2:
				List<GameInfo> myGameInfos = new ArrayList<GameInfo>();
				Collections.sort(games, comparator);
				if (games.size() > 6) {
					for (int i = 0; i < 6; i++) {
						myGameInfos.add(games.get(i));
					}
				} else {
					myGameInfos.addAll(games);
				}
				rankingPanel.setGames(myGameInfos);
				mAdapter.checkNewContentInterface(myInterfaceList);
				mAdapter.setData(gameTypeInfos);
				recyclerView.smoothScrollToPosition(0);
				break;
			default:
				break;
			}
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		context = (MainActivity) getActivity();
		registReceiver();
		View rootView = inflater.inflate(R.layout.fragment_gameclassify,
				container, false);
		ScaleViewUtils.scaleView(rootView);
		recyclerView = (RecyclerView) rootView.findViewById(R.id.section_list);
		loadingView = (LoadingView) rootView.findViewById(R.id.contentLoading);
		loadingView.setDataView(recyclerView);
		rankingPanel = new GameClassifyRankingPanel(getActivity());

		getRankingData(0,false);

		return rootView;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		unregistReceiver();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// Umeng页面统计
		UmengUtils.onPageStart(UmengUtils.GAMECLASSIFY_FRAGMENT);
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		// Umeng页面统计
		UmengUtils.onPageEnd(UmengUtils.GAMECLASSIFY_FRAGMENT);
	}

	@Override
	protected RecyclerView getRecyclerView() {
		return recyclerView;
	}

	public void getNewContentInterface(){
		List<String> interfaceList = DataHelper.getNewContentInterface();
		if(interfaceList==null){
			ReqCallback<List<String>> reqCallback = new ReqCallback<List<String>>() {
				@Override
				public void onResult(TaskResult<List<String>> taskResult) {
					int code = taskResult.getCode();
					alog.info("taskResult code:" + code);
					if (code == TaskResult.OK) {
						myInterfaceList.clear();
						myInterfaceList.addAll(taskResult.getData());
					}
				}
			};
			DataFetcher.getNewContentInterface(getActivity(), reqCallback, false);
		} else {
			myInterfaceList.clear();
			myInterfaceList.addAll(interfaceList);
		}
		mHandler.sendEmptyMessage(2);
		loadingView.getmHandler().sendEmptyMessage(
				Constant.DISMISSLOADING);
	}
	
	public void getRankingData(int type,final boolean isRefresh) {
		if(isRefresh){
			loadingView.getmHandler().sendEmptyMessage(
					Constant.SHOWLOADING);
		}
		ReqCallback<List<GameInfo>> reqCallback = new ReqCallback<List<GameInfo>>() {
			@Override
			public void onGetCacheData(String requestTag, boolean result) {
				if (!result) {
					loadingView.getmHandler().sendEmptyMessage(
							Constant.SHOWLOADING);
				}
			}

			@Override
			public void onResult(TaskResult<List<GameInfo>> taskResult) {
				int code = taskResult.getCode();
				alog.info("taskResult code:" + code);
				if (code == TaskResult.OK) {
					List<GameInfo> info = taskResult.getData();
					if (info != null && !info.isEmpty()) {
						alog.info(info.toString());
						games.clear();
						games.addAll(info);
						Message msg = Message.obtain();
						if(isRefresh){
							msg.obj=1;
						}
						else{
							msg.obj=0;
						}
						msg.what=1;
						mHandler.sendMessage(msg);
					} else {
						Message msg = Message.obtain();
						if(isRefresh){
							msg.obj=1;
						}
						else{
							msg.obj=0;
						}
						msg.what=1;
						mHandler.sendMessage(msg);
					}
				} else {
					Message msg = Message.obtain();
					if(isRefresh){
						msg.obj=1;
					}
					else{
						msg.obj=0;
					}
					msg.what=1;
					mHandler.sendMessage(msg);
				}

			}

			@Override
			public void onUpdate(TaskResult<List<GameInfo>> taskResult) {

			}
		};
		if(isRefresh){
			DataFetcher.getGameRanking(context, type, reqCallback, false)
			.registerUpdateListener(reqCallback).refresh(context);
		}
		else{
			DataFetcher.getGameRanking(context, type, reqCallback, false)
			.registerUpdateListener(reqCallback).request(context);
		}
	}

	public void getData(final boolean isRefresh) {
		// 创建结果回调监听，如果不需要知道结果，可不创建
		ReqCallback<List<GameTypeInfo>> reqCallback = new ReqCallback<List<GameTypeInfo>>() {

			@Override
			public void onGetCacheData(String requestTag, boolean result) {
				if (!result) {
					// loadingView.getmHandler().sendEmptyMessage(Constant.SHOWLOADING);
				}
			}

			// 调用 DataRequester的request方法后结果会回调到这个接口
			@Override
			public void onResult(TaskResult<List<GameTypeInfo>> taskResult) {
				int code = taskResult.getCode();
				alog.info("taskResult code:" + code);
				if (code == TaskResult.OK) {
					// 数据请求成功
					List<GameTypeInfo> infos = taskResult.getData();
					if (infos != null && !infos.isEmpty()) {

						alog.info(infos.toString());
						gameTypeInfos.clear();
						gameTypeInfos.addAll(infos);
						mHandler.sendEmptyMessage(0);

						/*loadingView.getmHandler().sendEmptyMessage(
								Constant.DISMISSLOADING);*/
					} else {
						loadingView.getmHandler().sendEmptyMessage(
								Constant.DISMISSLOADING);
					}
				} else {
					// 数据请求失败，获取失败原因，可用于提示用户
					String errorMsg = taskResult.getMsg();
					if (NetUtil.isNetworkAvailable(context, true)) {
						loadingView.getmHandler().sendEmptyMessage(
								Constant.DISMISSLOADING);
					} else {
						loadingView.getmHandler().sendEmptyMessage(
								Constant.DISMISSLOADING);
					}
				}

			}

			// 调用
			// DataRequester的registerUpdateListener方法可监听数据更新，当有数据更新时会回调此接口，使用完成后调用unregisterUpdateLister方法注销
			@Override
			public void onUpdate(TaskResult<List<GameTypeInfo>> taskResult) {

			}
		};

		// 构造DataRequester
		DataRequester dataRequester = DataFetcher.getGameType(getActivity(),
				reqCallback, false);
		// 注册数据更新监听，如不需要监听可不调用
		// dataRequester.registerUpdateListener(reqCallback);
		// 立即请求数据
		if(isRefresh){
			dataRequester.refresh(getActivity());
		}
		else{
			dataRequester.request(getActivity());
		}

		// 如果注册了数据更新监听，使用完成后注销监听
		// dataRequester.unregisterUpdateListener();
	}

	Comparator<GameInfo> comparator = new Comparator<GameInfo>() {

		@Override
		public int compare(GameInfo lhs, GameInfo rhs) {
			return rhs.getGameDownCount().compareTo(lhs.getGameDownCount());
		}
	};

	@Override
	public LayoutManager getLayoutManager() {
		return new LinearLayoutManager(getActivity(),
				LinearLayoutManager.HORIZONTAL, false);
	}

	@Override
	public ItemDecoration getItemDecoration() {
		return new GameClassifyInsetDecoration(getActivity());
	}

	@Override
	public BaseAdapter getAdapter() {
		mAdapter = new GameClassifyAdapter(context, recyclerView,
				context.mImageFetcher, rankingPanel);
		return mAdapter;
	}

	@Override
	public void setItemFocus() {
		if (recyclerView != null) {
			recyclerView.smoothScrollToPosition(0);
			View view = recyclerView.getLayoutManager().findViewByPosition(0);
			if (view instanceof GameClassifyRankingPanel) {
				GameClassifyRankingPanel panel = (GameClassifyRankingPanel) view;
				RecyclerView rankingRecyclerView = panel.getGameRankingList();
				if (rankingRecyclerView.getChildCount() > 0) {
					View v = rankingRecyclerView.getLayoutManager()
							.findViewByPosition(0);
					v.requestFocus();
				}
				context.setLastFcousView(null);
			}
		}
	}

	private boolean isFirstRegister = false;
	private boolean isConnected = true;
	/**
	 * @author htliu
	 * @description 用于监听网络状态变化的广播接受者
	 */
	BroadcastReceiver netBroadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context mContext, Intent intent) {
			try {
				if (!isFirstRegister) {
					NetworkInfo networkInfo = null;
					ConnectivityManager cm = (ConnectivityManager) mContext
							.getSystemService(Context.CONNECTIVITY_SERVICE);
					networkInfo = cm.getActiveNetworkInfo();
					alog.debug("接收网络监听广播");
					if (null != networkInfo) {
						boolean isConnect = networkInfo
								.isConnectedOrConnecting();
						alog.debug("netBroadcastReceiver isConnect = "
								+ isConnect);
						if (isConnect) {
							if(!isConnected){
								isConnected=true;
								getRankingData(0,false);
							}
						}
					} else {
						// 网络断开了
						isConnected = false;
						loadingView.getmHandler().sendEmptyMessage(
								Constant.DISMISSLOADING);
					}
				} else {
					isFirstRegister = false;
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	};

	/**
	 * 
	 * @Title: registReceiver
	 * @Description: TODO(注册广播监听网络变化)
	 * @param:
	 * @return: void
	 * @throws
	 */
	public void registReceiver() {
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		alog.debug("registReceiver()");
		context.registerReceiver(netBroadcastReceiver, intentFilter);
		isFirstRegister = true;
	}

	/**
	 * 
	 * @Title: unregistReceiver
	 * @Description: TODO(注销广播监听)
	 * @param:
	 * @return: void
	 * @throws
	 */
	public void unregistReceiver() {
		alog.debug("unregistReceiver()");
		context.unregisterReceiver(netBroadcastReceiver);
	}

	@Override
	public void smoothScrollToTop() {
		
	}
}
