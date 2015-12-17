package com.atet.tvmarket.control.classify.ranking;

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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.atet.common.logging.ALog;
import com.atet.tvmarket.R;
import com.atet.tvmarket.app.Constant;
import com.atet.tvmarket.control.common.BaseActivity;
import com.atet.tvmarket.entity.dao.GameInfo;
import com.atet.tvmarket.model.DataFetcher;
import com.atet.tvmarket.model.ReqCallback;
import com.atet.tvmarket.model.task.TaskResult;
import com.atet.tvmarket.utils.NetUtil;
import com.atet.tvmarket.utils.ScaleViewUtils;
import com.atet.tvmarket.view.BaseImageView;
import com.atet.tvmarket.view.CloseAcceTextView;
import com.atet.tvmarket.view.LoadingView;

public class GameRankingActivity extends BaseActivity {
	ALog alog = ALog.getLogger(GameRankingActivity.class);
	
	private RecyclerView mRecyclerView;
	private GameRankingAdapter mAdapter;
	private LoadingView loadingView;

	private SparseArray<List<GameInfo>> rankingList = new SparseArray<List<GameInfo>>();
	private int flag =0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(
				WindowManager.LayoutParams.FLAG_FULLSCREEN
						| WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM,
				WindowManager.LayoutParams.FLAG_FULLSCREEN
						| WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
		
		setContentView(R.layout.activity_gameranking);
		ScaleViewUtils.init(this);
		ScaleViewUtils.scaleView(getWindow().getDecorView());
		
		setBlackTitle(true);
		
		mRecyclerView = (RecyclerView)findViewById(R.id.rv_gameranking);
		loadingView = (LoadingView) findViewById(R.id.contentLoading);
		loadingView.setDataView(mRecyclerView);
		
		LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
		mRecyclerView.setLayoutManager(layoutManager);
		mAdapter = new GameRankingAdapter(this, mImageFetcher,mRecyclerView);
		mRecyclerView.setAdapter(mAdapter);
		
		registReceiver();
		
		getData();
	}
	@Override
	protected void onResume() {
		super.onResume();
		setTitleData();
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregistReceiver();
	}
	
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		int action = event.getAction();
		int keyCode = event.getKeyCode();
		if (action == KeyEvent.ACTION_DOWN) {
			if (keyCode == KeyEvent.KEYCODE_BUTTON_X || keyCode == KeyEvent.KEYCODE_X || keyCode == KeyEvent.KEYCODE_MENU) {
				getData();
				return true;
			}
		}
		return super.dispatchKeyEvent(event);
	}
	
	private void getData(){
		getGameRanking(1);
	}
	
	private void getGameRanking(final int type){
		ReqCallback<List<GameInfo>> reqCallback = new ReqCallback<List<GameInfo>>() {
			@Override
			public void onGetCacheData(String requestTag, boolean result) {
				if(!result){
					if(flag==0){
						loadingView.getmHandler().sendEmptyMessage(Constant.SHOWLOADING);
					}
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
						if(type==1){
							rankingList.put(0, info);
							flag=1;
							getGameRanking(2);
						}
						if(type==2){
							rankingList.put(1, info);
							flag = 2;
						}
						
						if(flag==2){
							flag=0;
							mHandler.sendEmptyMessage(0);
							loadingView.getmHandler().sendEmptyMessage(Constant.DISMISSLOADING);
						}
						
					}
					else{
						if(NetUtil.isNetworkAvailable(GameRankingActivity.this, true)){
							loadingView.getmHandler().sendEmptyMessage(Constant.NULLDATA);
						}
						else{
							loadingView.getmHandler().sendEmptyMessage(Constant.EXCEPTION);
						}
						
					}
					
					
				}
				else{
					if(NetUtil.isNetworkAvailable(GameRankingActivity.this, true)){
						loadingView.getmHandler().sendEmptyMessage(Constant.NULLDATA);
					}
					else{
						loadingView.getmHandler().sendEmptyMessage(Constant.EXCEPTION);
					}
				}
			}

			@Override
			public void onUpdate(TaskResult<List<GameInfo>> taskResult) {

			}
		};
		DataFetcher.getGameRanking(this, type, reqCallback, false)
				.registerUpdateListener(reqCallback).request(this);
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
						alog.debug( "netBroadcastReceiver isConnect = "
										+ isConnect);
						if (isConnect) {
							if(!isConnected){
								isConnected = true;
								getData();
							}
						}
					} else {
						// 网络断开了
						isConnected = false;
						alog.debug("当前网络断开了");
						loadingView.getmHandler().sendEmptyMessage(Constant.EXCEPTION);
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
		registerReceiver(netBroadcastReceiver, intentFilter);
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
		unregisterReceiver(netBroadcastReceiver);
	}
	
	Handler mHandler = new Handler(){
		public void handleMessage(Message msg) {
			mAdapter.setData(rankingList);
		};
	};
}
