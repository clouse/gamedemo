package com.atet.tvmarket.control.setup;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.atet.common.logging.ALog;
import com.atet.tvmarket.R;
import com.atet.tvmarket.control.common.BaseActivity;
import com.atet.tvmarket.utils.ChildlockUtils;
import com.atet.tvmarket.utils.ScaleViewUtils;
import com.atet.tvmarket.utils.ScreenShot;

public class SetupChildlockActivity extends BaseActivity {
	private ALog alog = ALog.getLogger(SetupChildlockActivity.class);
	private Toast toast = null;
	private RadioGroup radGrpChoice;
	private RadioButton radBtnChoice1, radBtnChoice2, radBtnChoice3;
	private ImageView ivChildlockIcon1, ivChildlockIcon2, ivChildlockIcon3;
	private TextView tvChoiceTip1, tvChoiceTip2, tvChoiceTip3;
	private Button btnModifyPassword;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_setup_childlock);
		ScaleViewUtils.scaleView(this);
		setBlackTitle(false);
		initView();
	}

	@Override
	protected void onResume() {
		super.onResume();
		setTitleData();
		setCurrentFlag(ChildlockUtils.getAgeState(this));
		if (ChildlockUtils.hasSetChildlockPassword(this)) {
			btnModifyPassword.setVisibility(View.VISIBLE);
		}
	}

	private void initView() {

		TouchListener cTouchListener = new TouchListener();
		FocusChangeListener cFocusChangeListener = new FocusChangeListener();
		ClickListener cClickListener = new ClickListener();

		radBtnChoice1 = (RadioButton) findViewById(R.id.setup_childlock_choice1_radbtn);
		radBtnChoice2 = (RadioButton) findViewById(R.id.setup_childlock_choice2_radbtn);
		radBtnChoice3 = (RadioButton) findViewById(R.id.setup_childlock_choice3_radbtn);
		radBtnChoice1.setOnClickListener(cClickListener);
		radBtnChoice1.setOnTouchListener(cTouchListener);
		radBtnChoice1.setOnFocusChangeListener(cFocusChangeListener);
		radBtnChoice2.setOnTouchListener(cTouchListener);
		radBtnChoice2.setOnClickListener(cClickListener);
		radBtnChoice2.setOnFocusChangeListener(cFocusChangeListener);
		radBtnChoice3.setOnTouchListener(cTouchListener);
		radBtnChoice3.setOnClickListener(cClickListener);
		radBtnChoice3.setOnFocusChangeListener(cFocusChangeListener);
		ivChildlockIcon1 = (ImageView) findViewById(R.id.childlock_icon1_iv);
		ivChildlockIcon2 = (ImageView) findViewById(R.id.childlock_icon2_iv);
		ivChildlockIcon3 = (ImageView) findViewById(R.id.childlock_icon3_iv);
		tvChoiceTip1 = (TextView) findViewById(R.id.choice_tip1);
		tvChoiceTip2 = (TextView) findViewById(R.id.choice_tip2);
		tvChoiceTip3 = (TextView) findViewById(R.id.choice_tip3);
		btnModifyPassword = (Button) findViewById(R.id.childlock_modifypassword_btn);
		btnModifyPassword.setOnTouchListener(cTouchListener);
		btnModifyPassword.setOnClickListener(cClickListener);
		btnModifyPassword.setOnFocusChangeListener(cFocusChangeListener);
	}

	class TouchListener implements OnTouchListener {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (event.getAction() == MotionEvent.ACTION_UP) {
				OnFocusChangeListener listener = v.getOnFocusChangeListener();
				if (listener != null && listener instanceof FocusChangeListener) {
					((FocusChangeListener) listener).setView(v);
				}
			}
			return false;
		}
	}

	class FocusChangeListener implements OnFocusChangeListener {

		private View view;

		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			if (hasFocus) {
				if (v.getId() == R.id.setup_childlock_choice1_radbtn) {
					tvChoiceTip1.setVisibility(View.VISIBLE);
					tvChoiceTip2.setVisibility(View.GONE);
					tvChoiceTip3.setVisibility(View.GONE);
				} else if (v.getId() == R.id.setup_childlock_choice2_radbtn) {
					tvChoiceTip1.setVisibility(View.GONE);
					tvChoiceTip2.setVisibility(View.VISIBLE);
					tvChoiceTip3.setVisibility(View.GONE);
				} else if (v.getId() == R.id.setup_childlock_choice3_radbtn) {
					tvChoiceTip1.setVisibility(View.GONE);
					tvChoiceTip2.setVisibility(View.GONE);
					tvChoiceTip3.setVisibility(View.VISIBLE);
				} else {
					tvChoiceTip1.setVisibility(View.GONE);
					tvChoiceTip2.setVisibility(View.GONE);
					tvChoiceTip3.setVisibility(View.GONE);
				}

				if (v.isInTouchMode() && v == view) {
					v.performClick();
				} else {
					view = null;
				}
			}
		}

		public View getView() {
			return view;
		}

		public void setView(View view) {
			this.view = view;
		}
	}

	class ClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			v.requestFocus();
			v.requestFocusFromTouch();
			int id = v.getId();
			if (id == R.id.setup_childlock_choice1_radbtn) {
				// 没有设置密码
				if (!ChildlockUtils
						.hasSetChildlockPassword(SetupChildlockActivity.this)) {
					// jumpToSetInitPassword(ChildlockUtils.AGE1);
				} else { // 已经设定密码
					// 点击的年龄段为已经设置的年龄段
					if (ChildlockUtils.getAgeState(SetupChildlockActivity.this) == ChildlockUtils.AGE1) {
						jumpToConfirmpassword(ChildlockUtils.AGE1);
					} else { // 点击的年龄段不是已经设置的年龄段
						jumpToSetNewpassword(ChildlockUtils.AGE1);
					}
				}
			} else if (id == R.id.setup_childlock_choice2_radbtn) {
				// 没有设置密码
				if (!ChildlockUtils
						.hasSetChildlockPassword(SetupChildlockActivity.this)) {
					jumpToSetInitPassword(ChildlockUtils.AGE2);
				} else { // 已经设定密码
					// 点击的年龄段为已经设置的年龄段
					if (ChildlockUtils.getAgeState(SetupChildlockActivity.this) == ChildlockUtils.AGE2) {
						jumpToConfirmpassword(ChildlockUtils.AGE2);
					} else { // 点击的年龄段不是已经设置的年龄段
						jumpToSetNewpassword(ChildlockUtils.AGE2);
					}
				}
			} else if (id == R.id.setup_childlock_choice3_radbtn) {
				// 没有设置密码
				if (!ChildlockUtils
						.hasSetChildlockPassword(SetupChildlockActivity.this)) {
					jumpToSetInitPassword(ChildlockUtils.AGE3);
				} else { // 已经设定密码
					// 点击的年龄段为已经设置的年龄段
					if (ChildlockUtils.getAgeState(SetupChildlockActivity.this) == ChildlockUtils.AGE3) {
						jumpToConfirmpassword(ChildlockUtils.AGE3);
					} else { // 点击的年龄段不是已经设置的年龄段
						jumpToSetNewpassword(ChildlockUtils.AGE3);
					}
				}
			} else if (id == R.id.childlock_modifypassword_btn) {
				jumpToModifypassword();
			}
		}
	}

	/**
	 * 
	 * @Title: jumpToSetInitPassword
	 * @Description: TODO(跳转到初始输入密码界面)
	 * @param @param ageState 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	private void jumpToSetInitPassword(int ageState) {
		Intent intent = new Intent(SetupChildlockActivity.this,
				SetupChildlockSetPasswordActivity.class);
		intent.putExtra(ChildlockUtils.AGE_KEY, ageState);
		startActivity(intent);
	}

	/**
	 * 
	 * @Title: jumpToConfirmpassword
	 * @Description: TODO(跳转到显示确定已经设定密码界面)
	 * @param @param ageState 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	private void jumpToConfirmpassword(int ageState) {
		Intent intent = new Intent(SetupChildlockActivity.this,
				SetupChildlockConfirmpasswordActivity.class);
		intent.putExtra(ChildlockUtils.AGE_KEY, ageState);
		startActivity(intent);
	}

	/**
	 * 
	 * @Title: jumpToModifypassword
	 * @Description: TODO(// 跳转到密码修改界面)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	private void jumpToModifypassword() {
		Intent intent = new Intent(SetupChildlockActivity.this,
				SetupChildlockModifyPasswordActivity.class);
		startActivity(intent);
	}

	/**
	 * 
	 * @Title: jumpToSetNewpassword
	 * @Description: TODO(跳转到设置新密码界面)
	 * @param @param ageState 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	private void jumpToSetNewpassword(int ageState) {
		Intent intent = new Intent(SetupChildlockActivity.this,
				SetupChildlockSetNewAgePasswordActivity.class);
		intent.putExtra(ChildlockUtils.AGE_KEY, ageState);
		startActivity(intent);
	}

	/**
	 * 
	 * @Title: setCurrentFlag
	 * @Description: TODO(显示当前童锁设定年龄的标志)
	 * @param @param age 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	private void setCurrentFlag(int age) {
		alog.info("age=" + age);
		if (age == ChildlockUtils.AGE1) {
			radBtnChoice1.requestFocus();
			ivChildlockIcon1.setVisibility(View.VISIBLE);
			ivChildlockIcon2.setVisibility(View.INVISIBLE);
			ivChildlockIcon3.setVisibility(View.INVISIBLE);
		} else if (age == ChildlockUtils.AGE2) {
			radBtnChoice2.requestFocus();
			ivChildlockIcon1.setVisibility(View.INVISIBLE);
			ivChildlockIcon2.setVisibility(View.VISIBLE);
			ivChildlockIcon3.setVisibility(View.INVISIBLE);
		} else if (age == ChildlockUtils.AGE3) {
			radBtnChoice3.requestFocus();
			ivChildlockIcon1.setVisibility(View.INVISIBLE);
			ivChildlockIcon2.setVisibility(View.INVISIBLE);
			ivChildlockIcon3.setVisibility(View.VISIBLE);
		}
	}
}
