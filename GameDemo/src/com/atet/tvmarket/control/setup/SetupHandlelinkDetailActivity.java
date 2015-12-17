package com.atet.tvmarket.control.setup;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.atet.common.logging.ALog;
import com.atet.tvmarket.R;
import com.atet.tvmarket.control.common.BaseActivity;
import com.atet.tvmarket.utils.GamepadConnectHelper;
import com.atet.tvmarket.utils.ScaleViewUtils;
import com.atet.tvmarket.view.NewToast;

public class SetupHandlelinkDetailActivity extends BaseActivity {
	private ALog alog = ALog.getLogger(SetupHandlelinkDetailActivity.class);
	private Toast toast = null;
	private Intent intent;
	private Button btnBlueTooth;
	private Button btnExit; // "下一步"/"退出"
	private Button btnContinue; // "继续连接"
	private RelativeLayout mLayoutGamepad1, mLayoutGamepad2;
	private RelativeLayout mLayoutConn;
	private ImageView mIvConnIconTween;
	private ImageView mHandleIcon1, mHandleIcon2;
	private TextView tvHandlelinkingmsg;
	Animation operatingAnim;
	private int currentPlayerCount = 0;
	private boolean isFromSetting = false;
	private Handler handlerFinish = new Handler();
	private Runnable runnableFinish = new Runnable() {

		public void run() {
			finish();
		}
	};
	private Handler handlerJump = new Handler();

	private GamepadConnectHelper mGamepadConnectService;
	private boolean mIsStop = false;
	private int handleType;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		intent = this.getIntent();
		handleType = intent.getIntExtra("handleType", 0);
		alog.info("handleType=" + handleType);
		if (handleType == 1) {
			setContentView(R.layout.dialog_setup_handledetail);
		} else {
			setContentView(R.layout.dialog_setup_handledetail2);
		}
		ScaleViewUtils.scaleView(this);
		setBlackTitle(false);
		initAnimation();
		initViews();
		initGamepadConnectService();
		mIsStop = false;
	}

	private void initViews() {
		mLayoutGamepad1 = (RelativeLayout) findViewById(R.id.layoutConnGamepad1);
		mLayoutGamepad2 = (RelativeLayout) findViewById(R.id.layoutConnGamepad2);
		mLayoutConn = (RelativeLayout) findViewById(R.id.layoutConn);
		mIvConnIconTween = (ImageView) findViewById(R.id.handlelink_detail_progress_icon);
		tvHandlelinkingmsg = (TextView) findViewById(R.id.handlelink_detail_handlelinking_message);
		mHandleIcon1 = (ImageView) findViewById(R.id.handlelink_detail_handle_icon1);
		mHandleIcon2 = (ImageView) findViewById(R.id.handlelink_detail_handle_icon2);
		// 启动旋转动画
		mIvConnIconTween.startAnimation(operatingAnim);

		btnExit = (Button) findViewById(R.id.handlelink_exit_btn);
		// 如果是设置中的手柄连接，按钮显示为"退出"

		btnExit.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				finish();
			}
		});

		btnExit.setOnTouchListener(touchListener);
		btnExit.setOnFocusChangeListener(focusChangeListener);
		btnContinue = (Button) findViewById(R.id.handlelink_goon_btn);
		btnContinue.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mLayoutConn.setVisibility(View.VISIBLE);
				if (currentPlayerCount == 1) {
					tvHandlelinkingmsg
							.setText(R.string.setup_handlelink_detail_linkingmsg2);
				}
				btnContinue.setVisibility(View.GONE);
				mGamepadConnectService.startScanBTdevice();
			}
		});
		btnContinue.setOnTouchListener(touchListener);
		btnContinue.setOnFocusChangeListener(focusChangeListener);
	}

	/**
	 * 
	 * @Title: initAnimation
	 * @Description: TODO 初始化动画
	 * @param
	 * @return void
	 * @throws
	 */
	private void initAnimation() {
		operatingAnim = AnimationUtils.loadAnimation(this,
				R.anim.start_bluetooth_conn_tween);
		LinearInterpolator lin = new LinearInterpolator();
		operatingAnim.setInterpolator(lin);
	}

	/**
	 * 
	 * @Title: setBlueToothBtn
	 * @Description: TODO 设置蓝牙连接(手柄已/未链接)
	 * @param
	 * @return void
	 * @throws
	 */
	private void setBlueToothBtn() {
		if (mGamepadConnectService == null) {
			return;
		}
		int connectedDeviceCount = mGamepadConnectService
				.getConnectedDeviceCount();
		alog.info("connectedDeviceCount=" + connectedDeviceCount);
		if (connectedDeviceCount == 0) {
			mLayoutGamepad1.setVisibility(View.GONE);
			mLayoutGamepad2.setVisibility(View.GONE);
			mLayoutConn.setVisibility(View.VISIBLE);
			tvHandlelinkingmsg
					.setText(R.string.setup_handlelink_detail_linkingmsg1);
			btnContinue.setVisibility(View.GONE);

		} else if (connectedDeviceCount == 1) {
			currentPlayerCount = connectedDeviceCount;
			/*
			 * if (GamepadConnectHelper.CURRENT_DEVICE_CLASS ==
			 * GamepadConnectHelper.ATET_DEVICE_CLASS) {
			 * mHandleIcon1.setBackgroundResource(R.drawable.handle_icon); }
			 * else if (GamepadConnectHelper.CURRENT_DEVICE_CLASS ==
			 * GamepadConnectHelper.ATETBJ9_DEVICE_CLASS) { mHandleIcon1
			 * .setBackgroundResource(R.drawable.handlelink_newhandle_icon); }
			 */
			mLayoutGamepad1.setVisibility(View.VISIBLE);
			mLayoutGamepad2.setVisibility(View.GONE);
			mLayoutConn.setVisibility(View.GONE);
			btnContinue.setVisibility(View.VISIBLE);
			alog.info("btnContinue is visible");
			btnExit.requestFocusFromTouch();
			if (GamepadConnectHelper.deviceClasses.get(0) == GamepadConnectHelper.ATET_DEVICE_CLASS) {
				mHandleIcon1.setBackgroundResource(R.drawable.handle_icon);
			} else if (GamepadConnectHelper.deviceClasses.get(0) == GamepadConnectHelper.ATETBJ9_DEVICE_CLASS) {
				mHandleIcon1
						.setBackgroundResource(R.drawable.handlelink_newhandle_icon);
			}
		} else if (connectedDeviceCount >= 2) {
			NewToast.makeToast(getApplicationContext() , R.string.setup_handlelink_finish, 0).show();
			mLayoutGamepad1.setVisibility(View.VISIBLE);
			mLayoutGamepad2.setVisibility(View.VISIBLE);
			mLayoutConn.setVisibility(View.GONE);
			// 如果已经连接上了两个手柄了，那么按钮就直接失去作用
			setButEnable(btnExit);
			setButEnable(btnContinue);
			handlerFinish.postDelayed(runnableFinish, 3000);
			if (GamepadConnectHelper.deviceClasses.get(0) == GamepadConnectHelper.ATET_DEVICE_CLASS) {
				mHandleIcon1.setBackgroundResource(R.drawable.handle_icon);
			} else if (GamepadConnectHelper.deviceClasses.get(0) == GamepadConnectHelper.ATETBJ9_DEVICE_CLASS) {
				mHandleIcon1
						.setBackgroundResource(R.drawable.handlelink_newhandle_icon);
			}
			if (GamepadConnectHelper.deviceClasses.get(1) == GamepadConnectHelper.ATET_DEVICE_CLASS) {
				mHandleIcon2.setBackgroundResource(R.drawable.handle_icon);
			} else if (GamepadConnectHelper.deviceClasses.get(1) == GamepadConnectHelper.ATETBJ9_DEVICE_CLASS) {
				mHandleIcon2.setBackgroundResource(R.drawable.handlelink_newhandle_icon);
			}
		}
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		alog.info("[onStart] ");
		super.onStart();
		if (mIsStop) {
			mIsStop = false;
			setBlueToothBtn();
			if (mLayoutConn.getVisibility() == View.VISIBLE) {
				alog.info("[onStart] searching");
				mGamepadConnectService.startScanBTdevice();
			}
		}
	}

	protected void onStop() {
		// TODO Auto-generated method stub
		alog.info("onStop()");
		super.onStop();
		mGamepadConnectService.stopScanBTdevice();
		mIsStop = true;
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		alog.info("onResume()");
		setTitleData();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		alog.info("onPause()");
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		alog.info("onDestroy()");
		handlerFinish.removeCallbacks(runnableFinish);
		mGamepadConnectService.recycle();
		mGamepadConnectService = null;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_B
				|| keyCode == KeyEvent.KEYCODE_BUTTON_B) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_B
				|| keyCode == KeyEvent.KEYCODE_BUTTON_B) {
			finish();
			return true;
		}
		return super.onKeyUp(keyCode, event);
	}

	/**
	 * 启动手柄设置后台服务
	 * 
	 * @throws
	 * @author:LiuQin
	 * @date 2014-4-18
	 */
	private void startDaemon() {
		try {
			Intent in = new Intent("com.atet.tvgamepad.DaemonService");
			startService(in);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public boolean isFirstStart() {
		// 读取SharedPreferences中需要的数据
		SharedPreferences preferences = getSharedPreferences(
				"chargefirststart", MODE_WORLD_READABLE);
		boolean isFirstStartLauncher = preferences.getBoolean(
				"isFirstStartLauncher", true);
		// 判断程序是第几次运行，如果是第一次运行则跳转到引导页面，同时保存标记
		if (isFirstStartLauncher) {
			Editor editor = preferences.edit();
			// 存入数据
			editor.putBoolean("isFirstStartLauncher", false);
			// 提交修改
			editor.commit();
		}
		return isFirstStartLauncher;
	}

	/**
	 * 不支持蓝牙处理
	 * 
	 * @author:LiuQin
	 * @date 2015-1-14
	 */
	private void notSupportBThandle() {
		NewToast.makeToast(this, getString(R.string.setup_handlelink_dongle_tip),
				Toast.LENGTH_LONG).show();
		finish();
	}

	private void initGamepadConnectService() {
		mGamepadConnectService = new GamepadConnectHelper(this, mListener);
		mGamepadConnectService.init();
	}

	private void setButEnable(Button btn) {
		btn.setEnabled(false);
		btn.setFocusable(false);
		btn.setFocusableInTouchMode(false);
	}

	private GamepadConnectHelper.OnGamepadConnectListener mListener = new GamepadConnectHelper.OnGamepadConnectListener() {
		@Override
		public void onServiceConnected() {
			// TODO Auto-generated method stub
			// 连上服务后，启动蓝牙扫描
			alog.info("[onServiceConnected] ");
			setBlueToothBtn();
			if (!mIsStop && mLayoutConn.getVisibility() == View.VISIBLE) {
				mGamepadConnectService.startScanBTdevice();
			}
		}

		@Override
		public void onNotSupportBT() {
			// TODO Auto-generated method stub
			// 设备可能不支持蓝牙
			notSupportBThandle();
		}

		@Override
		public void onAtetBTgamepadDisconnected(BluetoothDevice device) {
			// TODO Auto-generated method stub
			alog.info("device:" + device.getBluetoothClass().getDeviceClass());
			// 手柄断开连接
			if (!mIsStop) {
				setBlueToothBtn();
				mGamepadConnectService.startScanBTdevice();
			}
			// GamepadConnectHelper.removeDevice(device.getBluetoothClass()
			// .getDeviceClass());
		}

		@Override
		public void onAtetBTgamepadConnected(BluetoothDevice device) {
			// TODO Auto-generated method stub
			alog.info("device:" + device.getBluetoothClass().getDeviceClass());
			// 手柄成功连接
			if (!mIsStop) {
				setBlueToothBtn();
			}
			// GamepadConnectHelper.addNewDevice(device.getBluetoothClass()
			// .getDeviceClass());
		}

		@Override
		public void onScanTimeout() {
			// TODO Auto-generated method stub
			if (!mIsStop) {
				mGamepadConnectService.startScanBTdevice();
			}
		}
	};
}
