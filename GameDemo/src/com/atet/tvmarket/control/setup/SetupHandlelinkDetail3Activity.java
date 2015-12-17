package com.atet.tvmarket.control.setup;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.atet.tvmarket.R;
import com.atet.tvmarket.app.Constant;
import com.atet.tvmarket.control.common.BaseActivity;
import com.atet.tvmarket.utils.AppUtil;
import com.atet.tvmarket.utils.GamepadTool;
import com.atet.tvmarket.utils.ScaleViewUtils;

/**
 * 
 * @ClassName: GamepadConnectActivity
 * @Description:TODO(无线手柄适配向导界面)
 * @author
 * @date: 2014-11-13 下午9:24:24
 */
public class SetupHandlelinkDetail3Activity extends BaseActivity implements
		OnClickListener {
	private static final String TAG = "GamepadConnectActivity";
	private static final String PATH_TURNON_GAMEPAD = "turnon_gamepad";
	private static final String PATH_TURNOFF_GAMEPAD = "turnoff_gamepad";
	private static final String REMOTE_TURNON_GAMEPAD_URI = "content://com.atet.tvgamepad.provider/"
			+ PATH_TURNON_GAMEPAD;
	private static final String REMOTE_TURNOFF_GAMEPAD_URI = "content://com.atet.tvgamepad.provider/"
			+ PATH_TURNOFF_GAMEPAD;
	private static final String IME_ID = "com.atet.tvgamepad/.Input";
	private RelativeLayout switchLyout;
	private TextView tvSwitch;
	private CheckBox cbSwitch;
	private Button btnBack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_handlelind_detail3);
		ScaleViewUtils.scaleView(this);
		initView();
		setBlackTitle(false);
		addListener();
		initNextFocus();
	}

	/**
	 * 
	 * @Title: initView
	 * @Description: TODO(初始化控件)
	 * @param:
	 * @return: void
	 * @throws
	 */
	private void initView() {
		switchLyout = (RelativeLayout) findViewById(R.id.switch_layout);
		tvSwitch = (TextView) findViewById(R.id.tv_swith_content);
		cbSwitch = (CheckBox) findViewById(R.id.cb_switch);
		btnBack = (Button) findViewById(R.id.handlelink_exit_btn);
		/*
		 * mTvConnTv = (TextView) findViewById(R.id.start_bluetooth_conn_tip);
		 * mIvBottomTip1 = (ImageView) findViewById(R.id.imgBottomTip1);
		 * mIvBottomTip2 = (ImageView) findViewById(R.id.imgBottomTip2);
		 * mIvBottomTip2.setVisibility(View.VISIBLE);
		 */
	}

	/**
	 * 
	 * @Title: addListener
	 * @Description: TODO(给控件添加监听器)
	 * @param:
	 * @return: void
	 * @throws
	 */
	private void addListener() {
		switchLyout.setOnClickListener(this);
		btnBack.setOnClickListener(this);
		btnBack.setOnTouchListener(touchListener);
		btnBack.setOnFocusChangeListener(focusChangeListener);
	}

	/**
	 * 
	 * @Title: initNextFocus
	 * @Description: TODO(初始化焦点控制)
	 * @param:
	 * @return: void
	 * @throws
	 */
	private void initNextFocus() {
		switchLyout.setNextFocusLeftId(switchLyout.getId());
		switchLyout.setNextFocusRightId(switchLyout.getId());
		switchLyout.setNextFocusUpId(switchLyout.getId());
		switchLyout.setNextFocusDownId(btnBack.getId());

		btnBack.setNextFocusUpId(switchLyout.getId());
		btnBack.setNextFocusDownId(btnBack.getId());
		btnBack.setNextFocusLeftId(btnBack.getId());
		btnBack.setNextFocusRightId(btnBack.getId());
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.switch_layout) {
			// switch 开关点击事件
			toggleGamepadState();
		} else if (id == R.id.handlelink_exit_btn) {
			// 返回按钮
			finish();
		} else {
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		setTitleData();
		setGamepadState();
	}

	/**
	 * 
	 * @Title: resetCheckContent
	 * @Description: TODO(重置开关按钮的文字提示内容)
	 * @param:
	 * @return: void
	 * @throws
	 */
	private void resetCheckContent(boolean isChecked) {
		cbSwitch.setChecked(isChecked);
		if (isChecked) {
			// 状态为打开
			tvSwitch.setText(getResources().getString(
					R.string.setup_handlelink_detail_3g_switch_on));
		} else {
			// 状态为关闭
			tvSwitch.setText(getResources().getString(
					R.string.setup_handlelink_detail_3g_switch_off));
		}
	}

	/**
	 * 
	 * @Title: toggleGamepadState
	 * @Description: TODO 开启/关闭手柄状态
	 * @param
	 * @return void
	 * @throws
	 */
	private void toggleGamepadState() {
		if (getGamepadState()) {
			showConfirmToTurnOffGamepad();
		} else {
			if (toggleGamepadFromRemote(this, !cbSwitch.isChecked())) {
				setGamepadState();
			}
		}
		// if (toggleGamepadFromRemote(this, !cbSwitch.isChecked())) {
		// setGamepadState();
		// }
	}

	/**
	 * 
	 * @Title: setGamepadState
	 * @Description: TODO 设置 手柄操控链接与否 的状态
	 * @param
	 * @return void
	 * @throws
	 */
	private void setGamepadState() {
		// DebugTool.info(TAG, "GamepadState():" + getGamepadState());
		resetCheckContent(getGamepadState());
	}

	/**
	 * 
	 * @Title: getGamepadState
	 * @Description: TODO 获取手柄操控是否开启状态
	 * @param @return
	 * @return boolean false表示关闭，true表示开启
	 * @throws
	 */
	private boolean getGamepadState() {
		String defaultIME = Settings.Secure.getString(getContentResolver(),
				Settings.Secure.DEFAULT_INPUT_METHOD); // 获取默认的输入法
		return defaultIME.equals(IME_ID);
	}

	/**
	 * 手柄操控开关
	 * 
	 * @param context
	 *            从远程切换手柄操控状态
	 * @param isOn
	 * @return
	 * @throws
	 */
	private boolean toggleGamepadFromRemote(Context context, boolean isOn) {
		try {
			ContentResolver contentResolver = context.getContentResolver();
			ContentValues cv = new ContentValues();
			Uri url;
			if (isOn) {
				// 打开手柄操控
				url = Uri.parse(REMOTE_TURNON_GAMEPAD_URI);
			} else {
				// 关闭手柄操控
				url = Uri.parse(REMOTE_TURNOFF_GAMEPAD_URI);
			}
			int res = contentResolver.update(url, cv, null, null);// 更新的行数
			return res != 0;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 
	 * @Title: showConfirmToTurnOffGamepad
	 * @Description: TODO 显示确认关闭手柄操控
	 * @param
	 * @return void
	 * @throws
	 */
	private void showConfirmToTurnOffGamepad() {
		AlertDialog alertDialog = new AlertDialog.Builder(this)
				.setTitle(getString(R.string.down_title_tip))
				.setMessage(
						R.string.setup_handlelink_detail_3g_closehandleset_tip)
				.setPositiveButton(
						R.string.setup_handlelink_detail_3g_dialog_sure,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								if (toggleGamepadFromRemote(
										SetupHandlelinkDetail3Activity.this,
										false)) {
									setGamepadState();
								}
							}
						})
				.setNegativeButton(
						R.string.setup_handlelink_detail_3g_dialog_cancel, null)
				.setOnKeyListener(mOnDialogKeyListener).show();
		AppUtil.setDialogAlpha(alertDialog, Constant.DIALOG_BACKGROUND_ALPHA);
	}

	/**
	 * 对话框按键事件监听
	 */
	private DialogInterface.OnKeyListener mOnDialogKeyListener = new DialogInterface.OnKeyListener() {
		@Override
		public boolean onKey(DialogInterface dialogInterface, int keyCode,
				KeyEvent event) {
			// TODO Auto-generated method stub
			View focusView = ((Dialog) dialogInterface).getCurrentFocus();
			if (focusView == null) {
				return false;
			}

			if (GamepadTool.isButtonA(keyCode)) {
				// 模拟焦点按钮的点击事件
				KeyEvent keyevent = new KeyEvent(event.getAction(),
						KeyEvent.KEYCODE_ENTER);
				focusView.dispatchKeyEvent(keyevent);
				return true;
			} else if (GamepadTool.isButtonB(keyCode)) {
				Dialog dialog = (Dialog) dialogInterface;
				if (event.getAction() == KeyEvent.ACTION_UP && dialog != null
						&& dialog.isShowing()) {
					dialog.dismiss();
				}
				return true;
			} else if (GamepadTool.isButtonXY(keyCode)) {
				return true;
			}

			return false;
		}
	};
}
