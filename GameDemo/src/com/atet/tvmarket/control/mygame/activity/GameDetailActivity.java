package com.atet.tvmarket.control.mygame.activity;

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
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

import com.atet.common.logging.ALog;
import com.atet.tvmarket.R;
import com.atet.tvmarket.app.BaseApplication;
import com.atet.tvmarket.app.Constant;
import com.atet.tvmarket.control.common.BaseActivity;
import com.atet.tvmarket.control.mygame.view.GameDetailViewGroup;
import com.atet.tvmarket.entity.dao.GameInfo;
import com.atet.tvmarket.model.DataFetcher;
import com.atet.tvmarket.model.DataRequester;
import com.atet.tvmarket.model.GameStatisticsHelper;
import com.atet.tvmarket.model.ReqCallback;
import com.atet.tvmarket.model.task.TaskResult;
import com.atet.tvmarket.utils.NetUtil;
import com.atet.tvmarket.utils.ScaleViewUtils;
import com.atet.tvmarket.utils.StringTool;
import com.atet.tvmarket.view.LoadingView;

/**
 * 游戏详情
 * @author chenqingwen
 * 
 */
public class GameDetailActivity extends BaseActivity {

	private GameDetailViewGroup detailViewGroup; // 自定义ViewGroup
	private LoadingView loadingView; // 用于显示网络状态的view
	private GameInfo gameInfo; // 游戏信息
	ALog alog = ALog.getLogger(GameDetailActivity.class);
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) { // 获取到数据，更新界面
			case 0:
				List<GameInfo> info = (List<GameInfo>) msg.obj;
				if (info != null && info.size() > 0) {
					gameInfo = info.get(0);
					detailViewGroup.notifiDataChange(info.get(0));
				}
				break;
			}
		}

	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(
				WindowManager.LayoutParams.FLAG_FULLSCREEN
						| WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM,
				WindowManager.LayoutParams.FLAG_FULLSCREEN
						| WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
		setContentView(R.layout.game_detail_layout);
		ScaleViewUtils.init(this);
		ScaleViewUtils.scaleView(getWindow().getDecorView());
		initViews();
		init();
	}

	@Override
	protected void onResume() {
		super.onResume();
		setTitleData();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		detailViewGroup.recyleBtndownListener();
		unregistReceiver();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		super.onActivityResult(arg0, arg1, arg2);
		if (arg1 == RESULT_OK) {
			detailViewGroup.startDownloadGame();
		}
	}

	/**
	 * @description 初始化界面
	 * @throws
	 * @author：陈庆文
	 * @date 2015-8-28上午10:28:12
	 *
	 */
	private void initViews() {
		detailViewGroup = (GameDetailViewGroup) findViewById(R.id.game_detail_group);
		detailViewGroup.setClickable(false);
		loadingView = (LoadingView) findViewById(R.id.game_detail_center_loading);
		loadingView.setDataView(detailViewGroup);

		setBlackTitle(true);
	}

	/**
	 * 
	 * @description 初始化数据
	 * @throws
	 * @author：陈庆文
	 * @date 2015-8-28上午10:28:42
	 *
	 */
	private void init() {
		Intent intent = getIntent();
		String gameId = intent.getStringExtra("gameId"); // 游戏ID
		if (null == gameId || "".equals(gameId)) { // 未获取到游戏ID，则判断是否传递过来gameInfo对象
			gameInfo = (GameInfo) intent.getSerializableExtra("gameInfo");
			if (gameInfo != null) {
				detailViewGroup.notifiDataChange(gameInfo);
				showLoading(false);
			} else { // null则显示无数据
				showNullDataException();
			}
		} else {
			gameInfo = new GameInfo();
			gameInfo.setGameId(gameId);
			getGameInfoFromGameId(gameId, false); // 通过ID获取游戏信息
		}
		registReceiver(); // 注册网络监测广播
	}

	/**
	 * 
	 * @description 显示中间正在努力加载界面
	 * @param isShow true显示，false不显示
	 * @throws
	 * @author：陈庆文
	 * @date 2015-8-28上午10:29:06
	 *
	 */
	private void showLoading(boolean isShow) {
		if (isShow) {
			loadingView.getmHandler().sendEmptyMessage(Constant.SHOWLOADING);
		} else {
			loadingView.getmHandler().sendEmptyMessage(Constant.DISMISSLOADING);
		}
	}

	/**
	 * 
	 * @description 显示居中显示网络异常控件
	 * @throws
	 * @author：陈庆文
	 * @date 2015-8-28上午10:29:47
	 *
	 */
	private void ShowCenterNetException() {
		loadingView.getmHandler().sendEmptyMessage(Constant.EXCEPTION);
	}

	/**
	 * 
	 * @description 显示没有符合要求的数据 
	 * @throws
	 * @author：陈庆文
	 * @date 2015-8-28上午10:30:23
	 *
	 */
	private void showNullDataException() {
		loadingView.getmHandler().sendEmptyMessage(Constant.NULLDATA);
	}


	/**
	 * 
	 * @description 使用gameId获取游戏
	 * @param gameId
	 * @throws
	 * @author：陈庆文
	 * @date 2015-8-28上午10:30:41
	 *
	 */
	public void getGameInfoFromGameId(String gameId, boolean isRefresh) {

		ReqCallback<List<GameInfo>> reqCallback = new ReqCallback<List<GameInfo>>() {
			@Override
			public void onGetCacheData(String requestTag, boolean result) {
				// TODO Auto-generated method stub
				if (!result) { // 若不是从缓存中获取，则显示加载动画
					showLoading(true);
				}
			}

			@Override
			public void onResult(TaskResult<List<GameInfo>> taskResult) {
				int code = taskResult.getCode();
				if (code == TaskResult.OK) {
					List<GameInfo> info = taskResult.getData();
					if (info != null && info.size() > 0) { // 获取到数据
						Message message = handler.obtainMessage();
						message.what = 0;
						message.obj = info;
						handler.sendMessage(message);
						showLoading(false);

						clickStatistics(info.get(0));
					} else {
						if (NetUtil.isNetworkAvailable(getBaseContext(), true)) { // 网络正常，未获取到数据
							showNullDataException();
						} else { // 网络异常，未获取到数据
							ShowCenterNetException();
						}
					}
				} else {
					if (NetUtil.isNetworkAvailable(getBaseContext(), true)) {
						showNullDataException();
					} else {
						ShowCenterNetException();
					}
				}

			}
		};
		DataRequester dataRequester = DataFetcher.getGameInfoFromGameId(this, gameId, reqCallback, false);
		if(!isRefresh){
			dataRequester.request(this);
		} else {
			dataRequester.refresh(this);
		}
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		int keyCode = event.getKeyCode();
		int action = event.getAction();
		if (action == KeyEvent.ACTION_DOWN) {
			if ((keyCode == KeyEvent.KEYCODE_X || keyCode == KeyEvent.KEYCODE_BUTTON_X
					|| keyCode == KeyEvent.KEYCODE_MENU)) {
				// 处理点击XY键刷新问题
				if (!StringTool.isEmpty(gameInfo.getGameId()))
					getGameInfoFromGameId(gameInfo.getGameId(), true);
				return true;
			}
		}
		return super.dispatchKeyEvent(event);
	}

	private boolean isFirstRegister = false;
	
	/**
	 * @author chenqingwen
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
							if (!StringTool.isEmpty(gameInfo.getGameId()))
								getGameInfoFromGameId(gameInfo.getGameId(), false);
						}
					} else {
						// 网络断开了
						alog.debug("当前网络断开了");
						// loadingView.showContentNetExceptionOrNullData(true);
						loadingView.getmHandler().sendEmptyMessage(
								Constant.EXCEPTION);
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
	 * @description (注册广播监听网络变化)
	 * @throws
	 * @author：陈庆文
	 * @date 2015-8-28上午10:32:27
	 *
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
	 * @description (注销广播监听)
	 * @throws
	 * @author：陈庆文
	 * @date 2015-8-28上午10:32:48
	 *
	 */
	public void unregistReceiver() {
		alog.debug("unregistReceiver()");
		unregisterReceiver(netBroadcastReceiver);
	}

	private boolean mIsStatistics = false;
	
	/**
	 * @description: 点击统计
	 * 
	 * @throws:
	 * @author: LiuQin
	 * @date: 2015年8月25日 下午10:57:26
	 */
	private void clickStatistics(GameInfo gameInfo) {
		if(mIsStatistics){
			return;
		}
		mIsStatistics = true;
		Integer whereFrom = getIntent().getIntExtra(Constant.GAMECENTER, 0);
		if (whereFrom == null || gameInfo == null) {
			return;
		}
		GameStatisticsHelper.clickStatistics(gameInfo, whereFrom);
	}
}
