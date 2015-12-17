package com.atet.tvmarket.control.classify.detail;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.text.method.MovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.atet.common.logging.ALog;
import com.atet.tvmarket.R;
import com.atet.tvmarket.app.Constant;
import com.atet.tvmarket.control.classify.special.GameSpecialActivity;
import com.atet.tvmarket.control.common.BaseActivity;
import com.atet.tvmarket.control.mygame.activity.MyGameActivity;
import com.atet.tvmarket.entity.dao.GameInfo;
import com.atet.tvmarket.entity.dao.ThirdGameDownInfo;
import com.atet.tvmarket.entity.dao.ThirdGameInfo;
import com.atet.tvmarket.model.DataFetcher;
import com.atet.tvmarket.model.GameStatisticsHelper;
import com.atet.tvmarket.model.ReqCallback;
import com.atet.tvmarket.model.task.TaskResult;
import com.atet.tvmarket.utils.GameAdapterTypeUtil;
import com.atet.tvmarket.utils.NetUtil;
import com.atet.tvmarket.utils.ScaleViewUtils;
import com.atet.tvmarket.utils.ScreenShot;
import com.atet.tvmarket.view.LoadingView;
import com.atet.tvmarket.view.blur.BlurringView;

public class ThirdGameDetailActivity extends BaseActivity {
	private ALog alog = ALog.getLogger(ThirdGameDetailActivity.class);

	private ImageView icon;
	private TextView gameSize,handleType,gameName,gameDesc;
	private RecyclerView mRecyclerView;
	private GameSourceAdapter mAdapter;
	
	private RelativeLayout contentView;
	private LoadingView loadingView;
	private ThirdGameInfo thirdGameInfo;
	private String mGameId;
	private LinearLayout gameSourceLayout;
	List<ThirdGameDownInfo> thirdGameDownInfos = new ArrayList<ThirdGameDownInfo>();
	private  static final String GAMEID = "ThirdGameId";
	public GameInfo downloadGameInfo;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(
				WindowManager.LayoutParams.FLAG_FULLSCREEN
						| WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM,
				WindowManager.LayoutParams.FLAG_FULLSCREEN
						| WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
		
		setContentView(R.layout.activity_third_game_detail);
		ScaleViewUtils.init(this);
		ScaleViewUtils.scaleView(getWindow().getDecorView());
		
		setBlackTitle(false);
		
		icon = (ImageView)findViewById(R.id.iv_icon);
		gameSize = (TextView)findViewById(R.id.tv_game_size);
		handleType = (TextView)findViewById(R.id.tv_game_handle);
		gameName = (TextView)findViewById(R.id.tv_game_name);
		
		gameDesc = (TextView)findViewById(R.id.tv_game_desc);
		
		gameDesc.setMovementMethod(ScrollingMovementMethod.getInstance());
		gameDesc.setOnFocusChangeListener(onFocusChangeListener);
		
		mRecyclerView = (RecyclerView)findViewById(R.id.rv_game_source);
		
		LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
		mRecyclerView.setLayoutManager(layoutManager);
		
		mAdapter = new GameSourceAdapter(this,mRecyclerView, mImageFetcher);
		mRecyclerView.setAdapter(mAdapter);
		
		//gameSourceLayout = (LinearLayout)findViewById(R.id.ll_game_source);
		
		contentView = (RelativeLayout)findViewById(R.id.rl_desc);
		loadingView = (LoadingView)findViewById(R.id.contentLoading);
		loadingView.setDataView(contentView);
		
		if(getIntent()!=null){
			mGameId = getIntent().getStringExtra(GAMEID);
		}
		if(mGameId!=null && mGameId!=""){
			Log.i("life", "mGameId:"+mGameId);
			getData(mGameId);
		}
		registReceiver();
	}
	@Override
	protected void onResume() {
		super.onResume();
		setTitleData();
	}
	
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
		if (arg1 == RESULT_OK){
			if(downloadGameInfo!=null){
				MyGameActivity.addToMyGameList(ThirdGameDetailActivity.this,downloadGameInfo);
			}
			
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregistReceiver();
		if(ScreenShot.screenShotBmp!=null){
			ScreenShot.screenShotBmp.recycle();
			ScreenShot.screenShotBmp=null;
		}
	}
	
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		int action = event.getAction();
		int keyCode = event.getKeyCode();
		if (action == KeyEvent.ACTION_DOWN) {
			if (keyCode == KeyEvent.KEYCODE_BUTTON_X || keyCode == KeyEvent.KEYCODE_X ||keyCode == KeyEvent.KEYCODE_MENU) {
				getData(mGameId);
				return true;
			}
		}
		return super.dispatchKeyEvent(event);
	}
	
	OnFocusChangeListener onFocusChangeListener = new OnFocusChangeListener() {
		
		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			if(v instanceof TextView){
				if(hasFocus){
				}
				else{
					mRecyclerView.getLayoutManager().findViewByPosition(0).requestFocus();
				}
			}
		}
	};
	
	
	private void initGame(ThirdGameInfo gameInfo){
		if(gameInfo!=null){
			mImageFetcher.loadImage(gameInfo.getMinPhoto(), icon, R.drawable.gameranking_item_icon);
			gameSize.setText(gameInfo.getGameSize()/1024/1024+"M");
			handleType.setText(GameAdapterTypeUtil.decideAdapter(gameInfo.getHandleType()));
			gameName.setText(gameInfo.getGameName());
			gameDesc.setText("内容介绍 : "+gameInfo.getRemark());
		}
	}
	
	public void getData(String gameId){
		ReqCallback<List<ThirdGameInfo>> reqCallback = new ReqCallback<List<ThirdGameInfo>>() {
			
			@Override
			public void onGetCacheData(String requestTag, boolean result) {
				if(!result){
					loadingView.getmHandler().sendEmptyMessage(Constant.SHOWLOADING);
					
				}
			}
			
			@Override
			public void onResult(TaskResult<List<ThirdGameInfo>> taskResult) {
				int code = taskResult.getCode();
				alog.info("taskResult code:" + code);
				if (code == TaskResult.OK) {
					List<ThirdGameInfo> info = taskResult.getData();
					if (info != null && info.size() > 0) {
						for(ThirdGameInfo i:info){
							//alog.info(i.toString());
						}
						loadingView.getmHandler().sendEmptyMessage(Constant.DISMISSLOADING);
						thirdGameInfo = info.get(0);
						mHandler.sendEmptyMessage(0);
						
						Integer whereFrom = getIntent().getIntExtra(Constant.GAMECENTER, 0);
						//统计点击数
						GameStatisticsHelper.updateThirdGameClickCount(thirdGameInfo, whereFrom);
					}
					else{
						if(NetUtil.isNetworkAvailable(ThirdGameDetailActivity.this, true)){
							loadingView.getmHandler().sendEmptyMessage(Constant.NULLDATA);
						}
						else{
							loadingView.getmHandler().sendEmptyMessage(Constant.EXCEPTION);
						}
					}
				}
				else{
					if(NetUtil.isNetworkAvailable(ThirdGameDetailActivity.this, true)){
						loadingView.getmHandler().sendEmptyMessage(Constant.NULLDATA);
					}
					else{
						loadingView.getmHandler().sendEmptyMessage(Constant.EXCEPTION);
					}
				}

			}

			@Override
			public void onUpdate(TaskResult<List<ThirdGameInfo>> taskResult) {

			}
		};
		DataFetcher.getThirdGameInfoFromGameId(this, gameId, reqCallback, false)
				.registerUpdateListener(reqCallback).request(this);
	}
	
	Handler mHandler = new Handler(){
		public void handleMessage(Message msg) {
			if(thirdGameInfo!=null && thirdGameInfo.getDownloadInfo()!=null){
				thirdGameDownInfos.clear();
				thirdGameDownInfos.addAll(thirdGameInfo.getDownloadInfo());
			}
			mAdapter.setData(thirdGameDownInfos);
			initGame(thirdGameInfo);
			/*gameSourceLayout.removeAllViews();
			for(int i=0;i<5;i++){
				GameSourceView view = new GameSourceView(ThirdGameDetailActivity.this);
				gameSourceLayout.addView(view);
			}
			
			if(gameSourceLayout.getChildCount()>0){
				gameSourceLayout.getChildAt(0).requestFocus();
			}*/
		};
	};
	
	private boolean isFirstRegister = false;
	private boolean isConnteced = true;
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
							if(!isConnteced){
								isConnteced = true;
								getData(mGameId);
							}
						}
					} else {
						// 网络断开了
						alog.debug("当前网络断开了");
						isConnteced = false;
						//loadingView.showContentNetExceptionOrNullData(true);
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
	
}
