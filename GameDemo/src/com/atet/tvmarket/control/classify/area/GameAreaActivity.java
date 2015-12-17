package com.atet.tvmarket.control.classify.area;

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
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.atet.common.logging.ALog;
import com.atet.tvmarket.R;
import com.atet.tvmarket.app.Constant;
import com.atet.tvmarket.control.common.BaseActivity;
import com.atet.tvmarket.entity.dao.GameTypeInfo;
import com.atet.tvmarket.entity.dao.TypeToGame;
import com.atet.tvmarket.model.DataFetcher;
import com.atet.tvmarket.model.ReqCallback;
import com.atet.tvmarket.model.task.TaskResult;
import com.atet.tvmarket.utils.NetUtil;
import com.atet.tvmarket.utils.ScaleViewUtils;
import com.atet.tvmarket.view.BaseImageView;
import com.atet.tvmarket.view.CloseAcceTextView;
import com.atet.tvmarket.view.LoadingView;

public class GameAreaActivity extends BaseActivity {
	ALog alog = ALog.getLogger(GameAreaActivity.class);
	private TextView title;
	private LinearLayout potLayout;
	private RecyclerView mRecyclerView;
	private GameAreaAdapter mAdapter;
	private GameAreaSingleAdapter msAreaSingleAdapter;
	private LinearLayoutManager linearLayoutManager,linearLayoutManager2;
	private GridLayoutManager gridLayoutManager;
	private List<TypeToGame> games = new ArrayList<TypeToGame>();
	private int pageSize=0;
	private int currentPage=0;
	private List<TypeToGame> pageGames = new ArrayList<TypeToGame>();
	
	private RelativeLayout contentView;
	private LoadingView loadingView;
	private int pos=0;

	private GameTypeInfo mGameTypeInfo;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(
				WindowManager.LayoutParams.FLAG_FULLSCREEN
						| WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM,
				WindowManager.LayoutParams.FLAG_FULLSCREEN
						| WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM); 
		
		setContentView(R.layout.activity_gameclassify_area);
		ScaleViewUtils.init(this);
		ScaleViewUtils.scaleView(getWindow().getDecorView());
		
		setBlackTitle(true);
		
		if(getIntent()!=null){
			pos = getIntent().getIntExtra("position", 0);
		}
		
		title = (TextView)findViewById(R.id.tv_title);
		
		mRecyclerView = (RecyclerView)findViewById(R.id.rv_area_gameclassify);
		mRecyclerView.setHasFixedSize(true);
		contentView = (RelativeLayout)findViewById(R.id.rl_content);
		loadingView = (LoadingView)findViewById(R.id.contentLoading);
		loadingView.setDataView(contentView);
		
		registReceiver();
		
		getData(pos,false);
	}
	@Override
	protected void onResume() {
		super.onResume();
		setTitleData();
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(mAdapter!=null){
			mAdapter.setLastFocusView(null);
		}
		unregistReceiver();
	}
	
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		int action = event.getAction();
		int keyCode = event.getKeyCode();
		if (action == KeyEvent.ACTION_DOWN) {
			if (keyCode == KeyEvent.KEYCODE_BUTTON_X || keyCode == KeyEvent.KEYCODE_X || keyCode == KeyEvent.KEYCODE_MENU ) {
				getData(pos,true);
				return true;
			}
		}
		return super.dispatchKeyEvent(event);
	}
	
	public void initData(){
		
		int total = games.size();
		if(total > 0){
			if(total%12==0){
				pageSize = total/12;
			}
			else{
				pageSize = total/12+1;
			}
		}
		
		if(pageSize>0){
			if(pageSize>1){
				potLayout = (LinearLayout)findViewById(R.id.ll_pot);
				potLayout.removeAllViews();
				for(int i=0;i<pageSize;i++){
					ImageView pot = new ImageView(this);
					LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
					layoutParams.leftMargin=5;
					layoutParams.rightMargin=5;
					pot.setLayoutParams(layoutParams);
					potLayout.addView(pot);
				}
			}
			setAdapters();
		}
	}
	
	public void getData(final int N,final boolean isRefresh){
		
		if(isRefresh){
			loadingView.getmHandler().sendEmptyMessage(Constant.SHOWLOADING);
		}
		ReqCallback<List<GameTypeInfo>> reqCallback = new ReqCallback<List<GameTypeInfo>>() {
			
			@Override
			public void onGetCacheData(String requestTag, boolean result) {
				if(!result){
					loadingView.getmHandler().sendEmptyMessage(Constant.SHOWLOADING);
				}
			}

			@Override
			public void onResult(TaskResult<List<GameTypeInfo>> taskResult) {
				alog.info("taskResult code:" + taskResult.getCode());
				List<GameTypeInfo> infos = taskResult.getData();
				if (infos == null) {
					if(NetUtil.isNetworkAvailable(GameAreaActivity.this, true)){
						loadingView.getmHandler().sendEmptyMessage(Constant.NULLDATA);
					}
					else{
						loadingView.getmHandler().sendEmptyMessage(Constant.EXCEPTION);
					}
					return;
				}

				alog.debug("获取分类成功");
				for (GameTypeInfo gameTypeInfo : infos) {
					alog.info("分类id:" + gameTypeInfo.getTypeId() + " 分类名称:"
							+ gameTypeInfo.getName());					
				}
				Collections.sort(infos, comp);
				GameTypeInfo gameTypeInfo = infos.get(N);
				mGameTypeInfo = gameTypeInfo;
				alog.debug("获取【"+gameTypeInfo.getName()+"】分类下的游戏 ");
				List<TypeToGame> typeToGames = infos.get(N).getTypeToGameList();
				if(typeToGames!=null && !typeToGames.isEmpty()){
					alog.debug("缓存已经存在该分类的游戏");
					for (TypeToGame info: typeToGames) {
						alog.info("游戏名称:"+ info.getGameInfo().getGameName());
					}
					
					games.clear();
					games.addAll(typeToGames);
					mHandler.sendEmptyMessage(0);
					/*if(flag==0){
						mHandler.sendEmptyMessage(0);
					}
					else{
						mHandler.sendEmptyMessage(1);
					}*/
					loadingView.getmHandler().sendEmptyMessage(Constant.DISMISSLOADING);
					return;
				} else {
					alog.warn("缓存不存在该分类的游戏,从网络获取,分类id:"+gameTypeInfo.getTypeId() + " 分类名称:"+gameTypeInfo.getName());
				}
				
				ReqCallback<GameTypeInfo> reqCallback = new ReqCallback<GameTypeInfo>() {
					@Override
					public void onResult(TaskResult<GameTypeInfo> taskResult) {
						int code = taskResult.getCode();
						alog.info("taskResult code:" + code);
						if (code == TaskResult.OK) {
							GameTypeInfo gameTypeInfo = taskResult.getData();
							if (gameTypeInfo != null) {
								// alog.info(infos.toString());
								List<TypeToGame> typeToGames = gameTypeInfo.getTypeToGameList();
								if(typeToGames!=null && !typeToGames.isEmpty()){
									alog.debug("从网络获取到的【"+gameTypeInfo.getName()+"】分类下的游戏 ");
									for (TypeToGame info: typeToGames) {
										alog.info("游戏名称:"+ info.getGameInfo().getGameName());
									}
									games.clear();
									games.addAll(typeToGames);
									/*if(flag==0){
										mHandler.sendEmptyMessage(0);
									}
									else{
										mHandler.sendEmptyMessage(1);
									}*/
									mHandler.sendEmptyMessage(0);
									loadingView.getmHandler().sendEmptyMessage(Constant.DISMISSLOADING);
								}
								else{
									if(NetUtil.isNetworkAvailable(GameAreaActivity.this, true)){
										loadingView.getmHandler().sendEmptyMessage(Constant.NULLDATA);
									}
									else{
										loadingView.getmHandler().sendEmptyMessage(Constant.EXCEPTION);
									}
								}
							}
							else{
								if(NetUtil.isNetworkAvailable(GameAreaActivity.this, true)){
									loadingView.getmHandler().sendEmptyMessage(Constant.NULLDATA);
								}
								else{
									loadingView.getmHandler().sendEmptyMessage(Constant.EXCEPTION);
								}
							}
						}
						else{
							if(NetUtil.isNetworkAvailable(GameAreaActivity.this, true)){
								loadingView.getmHandler().sendEmptyMessage(Constant.NULLDATA);
							}
							else{
								loadingView.getmHandler().sendEmptyMessage(Constant.EXCEPTION);
							}
						}
					}

					@Override
					public void onUpdate(TaskResult<GameTypeInfo> taskResult) {

					}
				};
				DataFetcher.getGameInfosFromGameType2(GameAreaActivity.this, gameTypeInfo, reqCallback, false)
						.registerUpdateListener(reqCallback)
						.request(GameAreaActivity.this);
			}

			@Override
			public void onUpdate(TaskResult<List<GameTypeInfo>> taskResult) {

			}
		};
		if(isRefresh){
			DataFetcher.getGameType(this, reqCallback, false)
			.registerUpdateListener(reqCallback).refresh(this);
		}
		else{
			DataFetcher.getGameType(this, reqCallback, false)
			.registerUpdateListener(reqCallback).request(this);
		}
		
	}
	
	private void setAdapters(){
		pageGames.clear();
		if((currentPage+1)<pageSize){
			for(int i=currentPage*12;i<(currentPage+1)*12;i++){
				pageGames.add(games.get(i));
			}
		}
		else{
			for(int i=currentPage*12;i<games.size();i++){
				pageGames.add(games.get(i));
			}
		}
		mAdapter.setData(pageGames);
		setPot();
	}
	
	private void setPot(){
		if(potLayout!=null){
			for(int i=0;i<potLayout.getChildCount();i++){
				ImageView imageView = (ImageView) potLayout.getChildAt(i);
				if(i==currentPage){
					imageView.setImageResource(R.drawable.red_num_bg);
				}
				else{
					imageView.setImageResource(R.drawable.point_gray);
				}
			}
		}
	}
	
	public void nextPage(){
		if(pageSize>1){
			if(currentPage<pageSize-1){
				currentPage++;
			}
			else{
				currentPage=0;
			}
			
			setAdapters();
			rightAnimator();
		}
	}
	
	public void previousPage(){
		if(pageSize>1){
			if(currentPage>0){
				currentPage--;
			}
			else{
				currentPage=pageSize-1;
			}
			setAdapters();
			leftAnimator();
		}
	}
	
	public void rightAnimator(){
		ViewCompat.setTranslationX(mRecyclerView, mRecyclerView.getWidth());
		 ViewCompat.animate(mRecyclerView).cancel();
	     ViewCompat.animate(mRecyclerView).translationX(0).translationY(0).setDuration(1000);
	}
	
	public void leftAnimator(){
		 ViewCompat.setTranslationX(mRecyclerView, -mRecyclerView.getWidth());
		 ViewCompat.animate(mRecyclerView).cancel();
	     ViewCompat.animate(mRecyclerView).translationX(0).translationY(0).setDuration(1000);
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
								getData(pos,false);
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
			if(mGameTypeInfo!=null){
				title.setText(mGameTypeInfo.getName());
			}
			if(msg.what==0){
				if(games.size()<12){
					android.widget.RelativeLayout.LayoutParams layoutParams = new android.widget.RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
					layoutParams.leftMargin=95;
					if(mRecyclerView!=null){
						mRecyclerView.setLayoutParams(layoutParams);
						if(linearLayoutManager==null){
							linearLayoutManager = new LinearLayoutManager(GameAreaActivity.this, LinearLayoutManager.HORIZONTAL,false);
							mRecyclerView.setLayoutManager(linearLayoutManager);
						}
						if(msAreaSingleAdapter==null){
							msAreaSingleAdapter = new GameAreaSingleAdapter(mRecyclerView,GameAreaActivity.this,mImageFetcher);
							mRecyclerView.setAdapter(msAreaSingleAdapter);
						}
						mRecyclerView.smoothScrollToPosition(0);
						msAreaSingleAdapter.setLastFocusView(null);
						msAreaSingleAdapter.setData(games);
					}
				}
				else{
					
					
					if(mRecyclerView!=null){
						if(linearLayoutManager2==null){
							linearLayoutManager2 = new LinearLayoutManager(GameAreaActivity.this, GridLayoutManager.HORIZONTAL,false);
							mRecyclerView.setLayoutManager(linearLayoutManager2);
						}
						if(mAdapter==null){
							mAdapter = new GameAreaAdapter(mRecyclerView,GameAreaActivity.this,mImageFetcher);
							mRecyclerView.setAdapter(mAdapter);
						}
						mAdapter.setLastFocusView(null);
						initData();
					}
				}
			}
			/*else if(msg.what==1){
				if(games.size()<12){
					msAreaSingleAdapter.setData(games);
				}
				else{
					initData();
				}
			}*/
		}
	};
	
	Comparator<GameTypeInfo> comp = new Comparator<GameTypeInfo>() {

		@Override
		public int compare(GameTypeInfo lhs, GameTypeInfo rhs) {
			return rhs.getOrderNum().compareTo(lhs.getOrderNum());
		}
	};
}
