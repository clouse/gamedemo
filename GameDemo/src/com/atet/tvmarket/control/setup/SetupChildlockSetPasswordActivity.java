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
import com.atet.tvmarket.utils.UmengUtils;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
@SuppressLint("NewApi")
public class SetupChildlockSetPasswordActivity extends BaseActivity {
	private ALog alog = ALog.getLogger(SetupChildlockSetPasswordActivity.class);
	private Toast toast = null;
	private Intent intent;
	private Button btnSure, btnReset;
	private static RelativeLayout currentKeyView;
	private RelativeLayout mLayoutKeyFirst, mLayoutKeySecond, mLayoutKeyThird,
			mLayoutKeyForth;
	private boolean isKeyFlag = true;
	private boolean isOrientationKey = false; // 方向键
	private SharedPreferences childlockSharedPreferences;
	SharedPreferences.Editor editor;
	private int age;
	private String currentInputChildlockPassword = "";
	private String contextFlag = "";
	private String savedPassword = "";
	private TextView tvTip;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		intent = this.getIntent();
		setContentView(R.layout.dialog_setup_childlock_setpassword);
		ScaleViewUtils.scaleView(this);
		alog.info("SetupChildlockActivity");
		setBlackTitle(false);
		init();
	}

	private void init() {
		initView();
		childlockSharedPreferences = getSharedPreferences(
				ChildlockUtils.CHILDLOCK_SP_NAME, Activity.MODE_PRIVATE);
		editor = childlockSharedPreferences.edit();
		age = intent.getIntExtra(ChildlockUtils.AGE_KEY, ChildlockUtils.AGE1);
		contextFlag = intent.getStringExtra(ChildlockUtils.CONTEXT_FLAG);
		if (ChildlockUtils.CONTEXT_FLAG.equals(contextFlag)) {
			tvTip.setText(R.string.setup_childlock_inputtip_download);
			btnSure.setText(R.string.setup_childlock_download);
			btnSure.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (ChildlockUtils.isTheRightPassword(
							currentInputChildlockPassword,
							SetupChildlockSetPasswordActivity.this)) {
						alog.info("the right password");
						setResult(RESULT_OK);
						onBackPressed();
					} else {
						ToastUtils
								.ShowToast(
										SetupChildlockSetPasswordActivity.this,
										getString(R.string.setup_childlock_modifypasswordwrong_tip),
										toast);

						resetKeyIcon();
						currentInputChildlockPassword = "";
					}
				}
			});
		}
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
		btnSure = (Button) findViewById(R.id.childlock_setuppassword_sure_btn);
		btnSure.setNextFocusUpId(btnSure.getId());
		btnSure.setNextFocusDownId(btnSure.getId());
		btnSure.setNextFocusLeftId(btnSure.getId());
		btnSure.setOnClickListener(clickListener);
		btnSure.setOnFocusChangeListener(focusChangeListener);
		btnSure.setOnTouchListener(touchListener);
		btnReset = (Button) findViewById(R.id.childlock_setuppassword_reset_btn);
		btnReset.setNextFocusUpId(btnReset.getId());
		btnReset.setNextFocusDownId(btnReset.getId());
		btnReset.setNextFocusRightId(btnReset.getId());
		btnReset.setOnClickListener(clickListener);
		btnReset.setOnFocusChangeListener(focusChangeListener);
		btnReset.setOnTouchListener(touchListener);
		tvTip = (TextView) findViewById(R.id.childlock_input_tip);
	}

	class ClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			int id = v.getId();
			if (id == R.id.childlock_setuppassword_sure_btn) {
				if (currentInputChildlockPassword.length() == 4) {
					editor.putInt(ChildlockUtils.AGE_KEY, age);
					editor.putString(ChildlockUtils.PASSWORD_KEY,
							currentInputChildlockPassword);
					editor.apply();
					// umeng统计,设定所有游戏，设定适合18周岁以下，设定适合12周岁以下的所有游戏
					if (age == ChildlockUtils.AGE1) {
						// 设定所有游戏
						UmengUtils.setOnEvent(
								SetupChildlockSetPasswordActivity.this,
								UmengUtils.SETUP_CHILDLOCK_ALL);
					} else if (age == ChildlockUtils.AGE2) {
						// 设定18周岁以下
						UmengUtils.setOnEvent(
								SetupChildlockSetPasswordActivity.this,
								UmengUtils.SETUP_CHILDLOCK_18);
					} else if (age == ChildlockUtils.AGE3) {
						// 设定12周岁以下
						UmengUtils.setOnEvent(
								SetupChildlockSetPasswordActivity.this,
								UmengUtils.SETUP_CHILDLOCK_12);
					}
					ToastUtils
							.ShowToast(
									SetupChildlockSetPasswordActivity.this,
									getString(R.string.setup_childlock_initpasswordright_tip),
									toast);
					finish();
				}
			} else if (id == R.id.childlock_setuppassword_reset_btn) {
				currentInputChildlockPassword = "";
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
		if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
			if (isKeyFlag) {
				currentInputChildlockPassword += ChildlockUtils.UP;
				ivKeyIcon.setBackgroundResource(R.drawable.childlock_key_up);
				setFocusToNextKey(currentKeyView);
			}
		} else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
			isOrientationKey = true;
			if (isKeyFlag) {
				currentInputChildlockPassword += ChildlockUtils.DOWN;
				ivKeyIcon.setBackgroundResource(R.drawable.childlock_key_down);
				setFocusToNextKey(currentKeyView);
			}
		} else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
			isOrientationKey = true;
			if (isKeyFlag) {
				currentInputChildlockPassword += ChildlockUtils.LEFT;
				ivKeyIcon.setBackgroundResource(R.drawable.childlock_key_left);
				setFocusToNextKey(currentKeyView);
			}
		} else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
			isOrientationKey = true;
			if (isKeyFlag) {
				currentInputChildlockPassword += ChildlockUtils.RIGHT;
				ivKeyIcon.setBackgroundResource(R.drawable.childlock_key_right);
				setFocusToNextKey(currentKeyView);
			}
		} else {
			isOrientationKey = false;
			if (keyCode == KeyEvent.KEYCODE_BACK
					|| keyCode == KeyEvent.KEYCODE_B
					|| keyCode == KeyEvent.KEYCODE_BUTTON_B) {
				finish();
				return true;
			}
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
