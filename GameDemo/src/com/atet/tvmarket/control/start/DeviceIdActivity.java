package com.atet.tvmarket.control.start;

import java.util.Timer;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.atet.common.logging.ALog;
import com.atet.statistics.services.UpdateHardwareService;
import com.atet.statistics.utils.DeviceStatisticsUtils;
import com.atet.tvmarket.R;
import com.atet.tvmarket.app.BaseApplication;
import com.atet.tvmarket.app.Constant;
import com.atet.tvmarket.control.common.BaseActivity;
import com.atet.tvmarket.control.gamerecommand.NewGameRecommandActivity;
import com.atet.tvmarket.control.home.MainActivity;
import com.atet.tvmarket.control.mygame.activity.GameDetailActivity;
import com.atet.tvmarket.control.mygame.activity.MyGameActivity;
import com.atet.tvmarket.control.promotion.activity.GiftActivity;
import com.atet.tvmarket.control.promotion.activity.PromotionAreaActivity;
import com.atet.tvmarket.control.search.SearchActivity;
import com.atet.tvmarket.control.setup.SetupVideoActivity;
import com.atet.tvmarket.entity.UserInfo;
import com.atet.tvmarket.model.DataFetcher;
import com.atet.tvmarket.model.DataHelper;
import com.atet.tvmarket.model.ReqCallback;
import com.atet.tvmarket.model.task.TaskResult;
import com.atet.tvmarket.utils.NetUtil;
import com.atet.tvmarket.utils.ScaleViewUtils;
import com.atet.tvmarket.utils.UmengUtils;

public class DeviceIdActivity extends BaseActivity {
	private ALog alog = ALog.getLogger(DeviceIdActivity.class);

	private RelativeLayout loadingLayout;
	private ImageView loading;
	private TextView desc, laodingProgress;
	private AnimationDrawable animationDrawable;
	private int progress = 0;
	private Timer timer;

	private String jumpAction;
	private int currentAction;
	private String gameId;
	private String packageName;
	private long currTime;
	private boolean flag = false;// 用于记录是否能返回

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(
				WindowManager.LayoutParams.FLAG_FULLSCREEN
						| WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM,
				WindowManager.LayoutParams.FLAG_FULLSCREEN
						| WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

		setContentView(R.layout.activity_deviceid);
		ScaleViewUtils.init(this);
		ScaleViewUtils.scaleView(getWindow().getDecorView());

		loadingLayout = (RelativeLayout) findViewById(R.id.rl_loading);
		loading = (ImageView) findViewById(R.id.iv_loading);
		desc = (TextView) findViewById(R.id.tv_get_deviceid);
		getAction();

		// ========================= for umeng ========================
		// 开启调试模式
		UmengUtils.setDebugMode(true);
		// 禁止默认的activity页面统计方式
		UmengUtils.openActivityDurationTrack(false);
		// 设置发送策略
		UmengUtils.updateOnlineConfig(this);
		// 设置session的值
		UmengUtils.setSessionContinueMillis(10000);
	}

	@Override
	protected void onResume() {
		super.onResume();
		mHandler.post(new Runnable() {

			@Override
			public void run() {
				currTime = System.currentTimeMillis();
				animationDrawable = (AnimationDrawable) loading.getBackground();
				animationDrawable.start();
				getAction();
				getDeviceInfo();
			}
		});
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		int keyCode = event.getKeyCode();
		int action = event.getAction();
		if (action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_X
				|| keyCode == KeyEvent.KEYCODE_BUTTON_X) {
			desc.setVisibility(View.INVISIBLE);
			loadingLayout.setVisibility(View.VISIBLE);
			// animationDrawable.start();
			getDeviceInfo();
			return true;
		} else if (action == KeyEvent.ACTION_DOWN
				&& keyCode == KeyEvent.KEYCODE_B
				|| keyCode == KeyEvent.KEYCODE_BUTTON_B
				|| keyCode == KeyEvent.KEYCODE_BACK) {

			if (!flag) {
				return true;
			}

		}
		return super.dispatchKeyEvent(event);
	};

	// 获取设备号
	private void getDeviceInfo() {
		DataHelper.initDeviceInfo(this);
		alog.debug("device info:" + DataHelper.getDeviceInfo().toString());
		String serverId = DataHelper.getDeviceInfo().getServerId();
		if (TextUtils.isEmpty(serverId)) {
			// Umeng统计第一次进入终端数
			UmengUtils.setOnEvent(this, UmengUtils.MINE_ACCOUNT_FIRST_COME);
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					ReqCallback<String> reqCallback = new ReqCallback<String>() {
						@Override
						public void onResult(TaskResult<String> taskResult) {
							int code = taskResult.getCode();
							alog.info("taskResult code:" + code);
							if (code == TaskResult.OK) {
								alog.info(taskResult.getData());
								mHandler.sendEmptyMessage(0);
							} else {
								if (NetUtil.isNetworkAvailable(
										DeviceIdActivity.this, true)) {
									mHandler.sendEmptyMessage(1);
								} else {
									mHandler.sendEmptyMessage(2);
								}
							}

						}

						@Override
						public void onUpdate(TaskResult<String> taskResult) {

						}
					};
					DataFetcher
							.getServerId(DeviceIdActivity.this, reqCallback,
									false).registerUpdateListener(reqCallback)
							.request(DeviceIdActivity.this);
				}
			});
		} else {
			alog.debug("server id exist:"
					+ DataHelper.getDeviceInfo().getServerId());
			// jumpToOtherActivity();
			mHandler.sendEmptyMessage(0);

			DataFetcher.syncTime(getApplicationContext(), null, false).request(getApplicationContext());
			fetchDeviceId();
		}
	}

	/**
	 * 获取跳转进来的action
	 * */
	private void getAction() {
		gameId = getIntent().getStringExtra(Constant.GAME_ID);
		packageName = getIntent().getStringExtra(Constant.PACKAGE_NAME);
		jumpAction = getIntent().getAction();
		alog.debug("currentAction = " + jumpAction);
		if (null == jumpAction) {
			currentAction = Constant.ACTION_TO_MAIN;
		} else if (jumpAction.equals(Constant.MARKET_GAME_CENTER_ACTION)) {
			// 游戏中心主页面
			currentAction = Constant.ACTION_TO_MAIN;
		} else if (jumpAction.equals(Constant.MARKET_GAME_SETUP_ACTION)) {
			// 游戏设置
			currentAction = Constant.ACTION_TO_SETTING;
		} else if (jumpAction.equals(Constant.MARKET_MY_GAME_ACTION)) {
			// 我的游戏
			currentAction = Constant.ACTION_TO_MYGAME;
		} else if (jumpAction.equals(Constant.MARKET_GAME_CLASSIFY_ACTION)) {
			// 游戏分类
			currentAction = Constant.ACTION_TO_GAME_CLASSIFY;
		} else if (jumpAction.equals(Constant.MARKET_ACTIVITIES)) {
			// 活动专区
			currentAction = Constant.ACTION_TO_ACTIVITIES;
		} else if (jumpAction.equals(Constant.MARKET_GAME_CLASSIFY_UNICOM)) {
			// 联通沃
			currentAction = Constant.ACTION_TO_UNICOM;
		} else if (jumpAction.equals(Constant.MARKET_GAME_DETAIL_ACTION)) {
			// 游戏详情页面
			currentAction = Constant.ACTION_TO_GAME_DETAIL;
		} else if (jumpAction.equals(Constant.MARKET_GAMEPAD_SHOP)) {
			// 手柄购买页面
			currentAction = Constant.ACTION_TO_GAMEPAD_SHOP;
		} else if (jumpAction.equals(Constant.MAREKT_VIDEO_GUIDE)) {
			// 视频向导页面
			currentAction = Constant.ACTION_TO_VIDEO_GUIDE;
		} else if (jumpAction.equals(Constant.MARKET_GIFT_PACKAGE)) {
			// 跳转至礼包界面
			currentAction = Constant.ACTION_TO_GIFT_PACKAGE;
		} else {
			currentAction = Constant.ACTION_TO_MAIN;
		}
	}

	/**
	 * 跳转至其他页面
	 * */
	private void jumpToOtherActivity() {
		switch (currentAction) {
		case Constant.ACTION_TO_MAIN:
			// 跳转至游戏中心
			jumpToMainActivity();
			break;

		case Constant.ACTION_TO_SETTING:
			// 跳转至游戏设置
			jumpToMainActivity();
			break;

		case Constant.ACTION_TO_MYGAME:
			// 跳转至我的游戏
			jumpToMyGameActivity();
			break;

		case Constant.ACTION_TO_GAME_DETAIL:
			// 跳转至游戏详情
			jumpToDetailActivity();
			break;

		case Constant.ACTION_TO_GAMEPAD_SHOP:
			// 跳转至手柄购买页面
			jumpToGamePadShopActivity();
			break;

		case Constant.ACTION_TO_VIDEO_GUIDE:
			// 跳转至视频向导页面
			jumpToVideoGuideActivity();
			break;

		case Constant.ACTION_TO_GIFT_PACKAGE:
			// 跳转至礼包页面
			//jumpToGiftPackActivity();
			jumpToPromotionActivities();
			break;

		case Constant.ACTION_TO_GAME_CLASSIFY:
			// 跳转至游戏分类
			jumpToGameClassifyActivity();
			break;

		case Constant.ACTION_TO_ACTIVITIES:
			// 跳转至搜索分类
			break;

		default:
			break;
		}
	}

	/** 跳转至游戏中心主页 */
	private void jumpToMainActivity() {
		Intent intent = new Intent(DeviceIdActivity.this, MainActivity.class);
		intent.putExtra(Constant.ACTION_TO_JUMP, currentAction);
		intent.putExtra(Constant.EXTERNAL, true);
		startActivity(intent);
		finish();
	}
	
	/**跳转至新游推荐主页*/
	private void jumpToNewRecommendActivities(){
		Intent intent = new Intent(DeviceIdActivity.this, NewGameRecommandActivity.class);
		intent.putExtra(Constant.ACTION_TO_JUMP, currentAction);
		intent.putExtra(Constant.PACKAGE_NAME, packageName);
		intent.putExtra(Constant.EXTERNAL, true);
		startActivity(intent);
		finish();
	}

	/**跳转至活动专区主页*/
	private void jumpToPromotionActivities(){
		Intent intent = new Intent(DeviceIdActivity.this, PromotionAreaActivity.class);
		intent.putExtra(Constant.ACTION_TO_JUMP, currentAction);
		intent.putExtra(Constant.PACKAGE_NAME, packageName);
		intent.putExtra(Constant.EXTERNAL, true);
		startActivity(intent);
		finish();
	}

	/**
	 * 跳转至游戏详情页面
	 * */
	private void jumpToDetailActivity() {
		Intent intent = new Intent(DeviceIdActivity.this,
				GameDetailActivity.class);
		intent.putExtra(Constant.ACTION_TO_JUMP, currentAction);
		intent.putExtra(Constant.PACKAGE_NAME, packageName);
		intent.putExtra(Constant.EXTERNAL, true);
		intent.putExtra(Constant.GAME_ID, gameId);
		startActivity(intent);
		finish();
	}

	/**
	 * @author wenfuqiang
	 * @Title: jumpToGamePadShopActivity
	 * @Description: TODO(跳转至手柄购买页面)
	 * @param:
	 * @return: void
	 * @throws
	 */
	private void jumpToGamePadShopActivity() {
		/*
		 * Intent intent = new Intent(DeviceIdActivity.this,
		 * GamePadShopActivity1.class); intent.putExtra(Constant.ACTION_TO_JUMP,
		 * currentAction); intent.putExtra(Constant.EXTERNAL, true);// 是否外部端口调用
		 * startActivity(intent); finish();
		 */
	}

	/**
	 * 
	 * @Title: jumpToGiftPackActivity
	 * @Description: TODO(跳转至礼包界面)
	 * @param:
	 * @return: void
	 * @throws
	 */
	private void jumpToGiftPackActivity() {
		Intent intent = new Intent(DeviceIdActivity.this, GiftActivity.class);
		intent.putExtra(Constant.ACTION_TO_JUMP, currentAction);
		intent.putExtra(Constant.EXTERNAL, true);// 是否外部端口调用
		startActivity(intent);
		finish();
	}

	/**
	 * 
	 * @Title: jumpToGameClassifyActivity
	 * @Description: TODO(跳转至游戏分类界面)
	 * @param:
	 * @return: void
	 * @throws
	 */
	private void jumpToGameClassifyActivity() {
		Intent intent = new Intent(DeviceIdActivity.this, MainActivity.class);
		intent.putExtra(Constant.ACTION_TO_JUMP, currentAction);
		intent.putExtra(Constant.EXTERNAL, true);// 是否外部端口调用
		startActivity(intent);
		finish();
	}

	/**
	 * 
	 * @Title: jumpToSearchClassifyActivity
	 * @Description: TODO(跳转至搜索分类页面)
	 * @param:
	 * @return: void
	 * @throws
	 */
	private void jumpToSearchClassifyActivity() {
		Intent intent = new Intent(DeviceIdActivity.this, SearchActivity.class);
		intent.putExtra(Constant.ACTION_TO_JUMP, currentAction);
		intent.putExtra(Constant.EXTERNAL, true);// 是否外部端口调用
		startActivity(intent);
		finish();
	}

	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				flag = false;
				if (System.currentTimeMillis() - currTime > 4000) {
					animationDrawable.stop();
					initUserInfo();
					jumpToOtherActivity();
				} else {
					sendEmptyMessage(0);
				}
				break;
			case 1:
				loadingLayout.setVisibility(View.INVISIBLE);
				animationDrawable.stop();
				desc.setVisibility(View.VISIBLE);
				desc.setText(getResources().getString(
						R.string.code_device_id_no_required_data));
				flag = true;
				break;
			case 2:
				loadingLayout.setVisibility(View.INVISIBLE);
				animationDrawable.stop();
				desc.setVisibility(View.VISIBLE);
				desc.setText(getResources().getString(
						R.string.update_network_not_available));
				flag = true;
				break;
			default:
				break;
			}
		}

	};

	/**
	 * @description: 如果没有存在atetId就去网络上获取。
	 * 
	 * @throws:
	 * @author: LiuQin
	 * @date: 2015年8月17日 下午6:23:22
	 */
	private void fetchDeviceId() {
		String atetId = DeviceStatisticsUtils.getAtetId(this);
		if (atetId.equals("1") || TextUtils.isEmpty(atetId)) {
			Intent intent = new Intent(DeviceIdActivity.this,
					UpdateHardwareService.class);
			startService(intent);
		}
	}

	/**
	 * @author
	 * @Title: jumpToVideoGuideActivity
	 * @Description: TODO(跳转至视频向导页面)
	 * @param:
	 * @return: void
	 * @throws
	 */
	private void jumpToVideoGuideActivity() {
		Intent intent = new Intent(DeviceIdActivity.this,
				SetupVideoActivity.class);
		intent.putExtra(Constant.ACTION_TO_JUMP, currentAction);
		intent.putExtra(Constant.EXTERNAL, true);// 是否外部端口调用
		startActivity(intent);
		finish();
	}
	
	/**
	 * @description: 跳转到我的游戏(快速开始)
	 * 
	 * @author: LiuQin
	 * @date: 2015年9月24日 上午4:00:52
	 */
	private void jumpToMyGameActivity(){
		Intent intent = new Intent(DeviceIdActivity.this, MyGameActivity.class);
		intent.putExtra(Constant.ACTION_TO_JUMP, currentAction);
		intent.putExtra(Constant.EXTERNAL, true);
		startActivity(intent);
		finish();
	}
	
	/**
	 * @description: 初始化用户信息
	 * 
	 * @author: LiuQin
	 * @date: 2015年9月24日 上午4:08:23
	 */
	private void initUserInfo() {
		if(BaseApplication.userInfo != null){
			return;
		}
		ReqCallback<UserInfo> reqCallback = new ReqCallback<UserInfo>() {
			@Override
			public void onResult(TaskResult<UserInfo> taskResult) {
				int code = taskResult.getCode();
				alog.info("taskResult code:" + code);
				if (code == TaskResult.OK) {
					UserInfo info = taskResult.getData();
					BaseApplication.userInfo = info;
					alog.info(info);
				} else if (code == TaskResult.NO_DATA) {
					// 没有登录过的用户
					alog.error("用户暂时未登陆");
				} else {
					// 显示失败原因
					alog.error(taskResult.getMsg());
				}
			}
		};
		DataFetcher.userGetLastLoginedUser(this, reqCallback, false).request(
				this);
	}
}
