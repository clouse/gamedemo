package com.atet.tvmarket.control.setup;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.atet.common.logging.ALog;
import com.atet.tvmarket.R;
import com.atet.tvmarket.control.common.BaseActivity;
import com.atet.tvmarket.utils.ChildlockUtils;
import com.atet.tvmarket.utils.ScaleViewUtils;
import com.atet.tvmarket.utils.ToastUtils;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
@SuppressLint("NewApi")
public class SetupChildlockModifyPasswordActivity extends BaseActivity {
	private ALog alog = ALog
			.getLogger(SetupChildlockModifyPasswordActivity.class);
	private Toast toast = null;
	private Button btnSure, btnReset;
	private static RelativeLayout currentKeyView;
	private RelativeLayout mLayoutKeyFirst, mLayoutKeySecond, mLayoutKeyThird,
			mLayoutKeyForth;
	private boolean isKeyFlag = true; // 判断当前是否聚焦在密码按键上
	private boolean isOrientationKey = false; // 判断是否是方向键操作的标记
	private SharedPreferences childlockSharedPreferences;
	SharedPreferences.Editor editor;
	private String currentinputChildlockPassword = "";// 当前输入的密码
	private TextView tvInputTip;
	private int flag = 0; // 0表示当前处于输入原始密码阶段，1表示当前处于输入新密码阶段
	private Intent intent = null;
	private int age = 0;// 当前设定的年龄段

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_setup_childlock_modifypassword);
		ScaleViewUtils.scaleView(this);
		intent = this.getIntent();
		age = intent.getIntExtra(ChildlockUtils.AGE_KEY, ChildlockUtils.AGE1);
		setBlackTitle(false);
		init();
	}

	private void init() {
		initView();
		childlockSharedPreferences = getSharedPreferences(
				ChildlockUtils.CHILDLOCK_SP_NAME, Activity.MODE_PRIVATE);
		editor = childlockSharedPreferences.edit();
		initView();
	}

	@Override
	protected void onResume() {
		super.onResume();
		setTitleData();
	}

	private void initView() {
		ClickListener clickListener = new ClickListener();
		mLayoutKeyFirst = (RelativeLayout) findViewById(R.id.childlock_setuppassword_key_first);
		setKeyUnMoveable(mLayoutKeyFirst);
		mLayoutKeySecond = (RelativeLayout) findViewById(R.id.childlock_setuppassword_key_second);
		setKeyUnMoveable(mLayoutKeySecond);
		mLayoutKeyThird = (RelativeLayout) findViewById(R.id.childlock_setuppassword_key_third);
		setKeyUnMoveable(mLayoutKeyThird);
		mLayoutKeyForth = (RelativeLayout) findViewById(R.id.childlock_setuppassword_key_forth);
		setKeyUnMoveable(mLayoutKeyForth);
		currentKeyView = mLayoutKeyFirst;
		btnSure = (Button) findViewById(R.id.childlock_modifypassword_sure_btn);
		btnSure.setNextFocusUpId(btnSure.getId());
		btnSure.setNextFocusDownId(btnSure.getId());
		btnSure.setNextFocusLeftId(btnSure.getId());
		btnSure.setNextFocusRightId(btnSure.getId());
		btnSure.setOnClickListener(clickListener);
		btnSure.setOnFocusChangeListener(focusChangeListener);
		btnSure.setOnTouchListener(touchListener);
		btnReset = (Button) findViewById(R.id.childlock_modifypassword_reset_btn);
		btnReset.setNextFocusUpId(btnReset.getId());
		btnReset.setNextFocusDownId(btnReset.getId());
		btnReset.setNextFocusRightId(btnReset.getId());
		btnReset.setOnClickListener(clickListener);
		btnReset.setOnFocusChangeListener(focusChangeListener);
		btnReset.setOnTouchListener(touchListener);
		tvInputTip = (TextView) findViewById(R.id.childlock_input_tip);
		tvInputTip.setText(R.string.setup_childlock_srcpassword_tip);
	}

	class ClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			int id = v.getId();
			if (id == R.id.childlock_modifypassword_sure_btn) {
				// 如果处于输入原始密码阶段,但密码输入错误
				if (!ChildlockUtils.isTheRightPassword(
						currentinputChildlockPassword,
						SetupChildlockModifyPasswordActivity.this)
						&& flag == 0) {
					ToastUtils
							.ShowToast(
									SetupChildlockModifyPasswordActivity.this,
									getString(R.string.setup_childlock_modifypasswordwrong_tip),
									toast);
					resetKeyIcon();
					// 密码清空
					currentinputChildlockPassword = "";
				}
				// 如果处于输入原始密码阶段,并且密码输入正确
				if (ChildlockUtils.isTheRightPassword(
						currentinputChildlockPassword,
						SetupChildlockModifyPasswordActivity.this)
						&& flag == 0) {
					ToastUtils
							.ShowToast(
									SetupChildlockModifyPasswordActivity.this,
									getString(R.string.setup_childlock_modifypasswordright_tip),
									toast);
					flag = 1;// 表示处于新密码输入阶段
					resetKeyIcon();
					tvInputTip
							.setText(R.string.setup_childlock_newpassword_tip);
					currentinputChildlockPassword = "";
					btnReset.setVisibility(View.VISIBLE);
					btnSure.setNextFocusRightId(btnReset.getId());
				}
				// 如果当前处于输入新密码阶段，且已经输入了密码
				if (flag == 1 && currentinputChildlockPassword.length() == 4) {
					// 存储新的密码
					editor.putString(ChildlockUtils.PASSWORD_KEY,
							currentinputChildlockPassword);
					editor.apply();
					ToastUtils
							.ShowToast(
									SetupChildlockModifyPasswordActivity.this,
									getString(R.string.setup_childlock_modify_success_tip),
									toast);
					finish();
				}
			} else if (id == R.id.childlock_modifypassword_reset_btn) {
				currentinputChildlockPassword = "";
				resetKeyIcon();
			} else {
			}
		}
	}

	// 设置焦点在按键上时值方向键无法控制焦点的移动
	private void setKeyUnMoveable(View key) {
		key.setNextFocusUpId(key.getId());
		key.setNextFocusDownId(key.getId());
		key.setNextFocusLeftId(key.getId());
		key.setNextFocusRightId(key.getId());
	}

	// 焦点移动到下一个key控件上
	private void setFocusToNextKey(View view) {
		if (view.getId() == R.id.childlock_setuppassword_key_first) {
			mLayoutKeySecond.requestFocus();
			mLayoutKeySecond.requestFocusFromTouch();
			currentKeyView = mLayoutKeySecond;
			isKeyFlag = true;
		} else if (view.getId() == R.id.childlock_setuppassword_key_second) {
			mLayoutKeyThird.requestFocus();
			mLayoutKeyThird.requestFocusFromTouch();
			currentKeyView = mLayoutKeyThird;
			isKeyFlag = true;
		} else if (view.getId() == R.id.childlock_setuppassword_key_third) {
			mLayoutKeyForth.requestFocus();
			mLayoutKeyForth.requestFocusFromTouch();
			currentKeyView = mLayoutKeyForth;
			isKeyFlag = true;
		} else if (view.getId() == R.id.childlock_setuppassword_key_forth) {
			btnSure.requestFocus();
			btnSure.requestFocusFromTouch();
			currentKeyView = mLayoutKeyFirst;
			isKeyFlag = false;
		}
	}

	/**
	 * 
	 * @Title: resetKeyIcon
	 * @Description: TODO(重新设置密码框中的图标)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	@SuppressLint("NewApi")
	private void resetKeyIcon() {
		alog.info("[resetKeyIcon]");
		mLayoutKeyFirst.getChildAt(0).setBackgroundDrawable(null);
		mLayoutKeySecond.getChildAt(0).setBackgroundDrawable(null);
		mLayoutKeyThird.getChildAt(0).setBackgroundDrawable(null);
		mLayoutKeyForth.getChildAt(0).setBackgroundDrawable(null);
		currentKeyView = mLayoutKeyFirst;
		mLayoutKeyFirst.requestFocus();
		mLayoutKeyFirst.requestFocusFromTouch();
		isKeyFlag = true;
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		alog.info("[onKeyUp]1:event=" + event.toString());

		ImageView ivKeyIcon = (ImageView) currentKeyView.getChildAt(0);
		alog.info("[onKeyUp]currentKeyView:" + currentKeyView.toString()
				+ ";event" + event.toString());
		switch (keyCode) {
		case KeyEvent.KEYCODE_DPAD_UP:
			//当前的焦点在密码输入框上
			if (isKeyFlag) {
				currentinputChildlockPassword += ChildlockUtils.UP;
				ivKeyIcon.setBackgroundResource(R.drawable.childlock_key_up);
				setFocusToNextKey(currentKeyView);
			}
			break;
		case KeyEvent.KEYCODE_DPAD_DOWN:
			isOrientationKey = true;
			if (isKeyFlag) {
				currentinputChildlockPassword += ChildlockUtils.DOWN;
				ivKeyIcon.setBackgroundResource(R.drawable.childlock_key_down);
				setFocusToNextKey(currentKeyView);
			}
			break;
		case KeyEvent.KEYCODE_DPAD_LEFT:
			isOrientationKey = true;
			if (isKeyFlag) {
				currentinputChildlockPassword += ChildlockUtils.LEFT;
				ivKeyIcon.setBackgroundResource(R.drawable.childlock_key_left);
				setFocusToNextKey(currentKeyView);
			}
			break;
		case KeyEvent.KEYCODE_DPAD_RIGHT:
			isOrientationKey = true;
			if (isKeyFlag) {
				currentinputChildlockPassword += ChildlockUtils.RIGHT;
				ivKeyIcon.setBackgroundResource(R.drawable.childlock_key_right);
				setFocusToNextKey(currentKeyView);
			}
			break;

		default:
			isOrientationKey = false;
			if (keyCode == KeyEvent.KEYCODE_BACK
					|| keyCode == KeyEvent.KEYCODE_B
					|| keyCode == KeyEvent.KEYCODE_BUTTON_B) {
				finish();
				return true;
			}
			break;
		}

		// 当前焦点在方向键view上，并且按下的键不是方向键时，不做任何处理
		if (isKeyFlag || !isOrientationKey) {
			return false;
		}
		return super.onKeyUp(keyCode, event);

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		alog.info("[onKeyDown]");
		// TODO Auto-generated method stub
		return false;
	}
}
