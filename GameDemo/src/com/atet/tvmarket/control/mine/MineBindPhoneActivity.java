package com.atet.tvmarket.control.mine;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.atet.common.logging.ALog;
import com.atet.tvmarket.R;
import com.atet.tvmarket.app.BaseApplication;
import com.atet.tvmarket.entity.UserGetCaptchaReq;
import com.atet.tvmarket.entity.UserInfo;
import com.atet.tvmarket.entity.UserModifyReq;
import com.atet.tvmarket.model.DataFetcher;
import com.atet.tvmarket.model.ReqCallback;
import com.atet.tvmarket.model.task.TaskResult;
import com.atet.tvmarket.utils.KeyboardUtils;
import com.atet.tvmarket.utils.NetUtil;
import com.atet.tvmarket.utils.ScaleViewUtils;
import com.atet.tvmarket.utils.StringsVerifyUitls;
import com.atet.tvmarket.view.NewToast;
import com.atetpay.autologin.GetCode;

/**
 * 
 * @author songwei 该类中方法的说明，可以参照"MineAccountManagerActivity"中
 */
public class MineBindPhoneActivity extends KeyboardBaseActivity3 {
	private ALog alog = ALog.getLogger(MineBindPhoneActivity.class);
	private Toast toast = null;
	private TextView tvNewPhoneNo, tvVerificationCode;
	private Button btnVerificationCode, btnBindPhone;
	private TextView /* tvCurrentAccount, */tvBindedPhoneno;
	private String newPhoneNoStr = "", verificationCodeStr = "";
	private int focusViewFlag = 0;
	private ImageView loadingView;
	private AnimationDrawable mAnimationDrawable;
	Message msg;
	private TimeCount time;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_mine_accountsafe_bindphone);
		ScaleViewUtils.scaleView(this);
		setBlackTitle(false);
		initView();
		time = new TimeCount(60000, 1000);// 构造CountDownTimer对象
	}

	@Override
	protected void onResume() {
		super.onResume();
		setTitleData();
	}

	private void initView() {
		CurrentClickListener currentClickListener = new CurrentClickListener();
		initKeyboard(currentClickListener);
		// tvCurrentAccount = (TextView)
		// findViewById(R.id.mine_accountsafe_bindphone_accountno);
		tvBindedPhoneno = (TextView) findViewById(R.id.mine_accountsafe_bindphone_bindinfo);
		tvNewPhoneNo = (TextView) findViewById(R.id.mine_accountsafe_bindphone_phoneno_et);
		tvNewPhoneNo.setOnClickListener(currentClickListener);
		tvVerificationCode = (TextView) findViewById(R.id.mine_accountsafe_bindphone_verificationcode_et);
		tvVerificationCode.setOnClickListener(currentClickListener);
		btnVerificationCode = (Button) findViewById(R.id.mine_accountsafe_bindphone_getverificationcode);
		btnVerificationCode.setOnClickListener(currentClickListener);
		setEvents(btnVerificationCode);
		setNextFocusSelf(btnVerificationCode);
		btnBindPhone = (Button) findViewById(R.id.mine_accountsafe_bindphone_btn);
		btnBindPhone.setOnClickListener(currentClickListener);
		setEvents(btnBindPhone);
		setNextFocusSelf(btnBindPhone);
		setFocusView(focusViewFlag);

		loadingView = (ImageView) findViewById(R.id.iv_loading);
		mAnimationDrawable = (AnimationDrawable) loadingView.getDrawable();

		initData();
		setKeyboardNextFocus();
	}

	private void initData() {
		if (BaseApplication.userInfo != null) {
			// tvCurrentAccount.setText(BaseApplication.userInfo.getUserId() +
			// "");
			tvBindedPhoneno.setText(BaseApplication.userInfo.getPhoneNum());
		}
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			String errorMsg = "";
			switch (msg.what) {
			case 0:
				loadingView.setVisibility(View.VISIBLE);
				mAnimationDrawable.start();
				break;
			case 1:
				mAnimationDrawable.stop();
				loadingView.setVisibility(View.INVISIBLE);
				break;
			case 2:
				NewToast.makeToast(MineBindPhoneActivity.this, "获取验证码成功",
						Toast.LENGTH_SHORT).show();
				time.start();
				break;
			case 3:
				errorMsg = msg.obj.toString();
				if (errorMsg == "") {
					errorMsg = "系统异常";
				}
				NewToast.makeToast(MineBindPhoneActivity.this, errorMsg,
						Toast.LENGTH_SHORT).show();
				break;
			case 4:
				NewToast.makeToast(MineBindPhoneActivity.this, "绑定手机成功",
						Toast.LENGTH_SHORT).show();
				tvBindedPhoneno.setText(newPhoneNoStr);
				UserInfo userInfo = BaseApplication.userInfo;
				userInfo.setPhoneNum(newPhoneNoStr);
				BaseApplication.userInfo = userInfo;
				setTitleData();
				break;
			case 5:
				errorMsg = msg.obj.toString();
				if (errorMsg == "") {
					errorMsg = "系统异常";
				}
				NewToast.makeToast(MineBindPhoneActivity.this, errorMsg,
						Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
		};
	};

	private void getCaptcha(String phoneNum, int type) {
		mHandler.sendEmptyMessage(0);
		// 构造请求参数
		UserGetCaptchaReq reqInfo = new UserGetCaptchaReq();
		reqInfo.setPhoneNum(phoneNum);
		reqInfo.setType(type);

		ReqCallback<Void> reqCallback = new ReqCallback<Void>() {
			@Override
			public void onResult(TaskResult<Void> taskResult) {
				mHandler.sendEmptyMessage(1);
				int code = taskResult.getCode();
				alog.info("taskResult code:" + code);
				if (code == TaskResult.OK) {
					// 获取验证码成功
					mHandler.sendEmptyMessage(2);
				} else {
					// 显示失败原因
					alog.error(taskResult.getMsg());
					msg = Message.obtain();
					msg.obj = taskResult.getMsg();
					msg.what = 3;
					mHandler.sendMessage(msg);
				}

			}
		};
		DataFetcher.userGetCaptcha(this, reqCallback, reqInfo, false).request(
				this);
	}

	private void userUpdatePhoneNum(String newPhone, String captcha) {
		mHandler.sendEmptyMessage(0);
		// 构造请求参数
		UserModifyReq reqInfo = new UserModifyReq();
		reqInfo.setUserId(BaseApplication.userInfo.getUserId());
		reqInfo.setNewPhone(newPhone);
		reqInfo.setCaptcha(captcha);

		ReqCallback<Void> reqCallback = new ReqCallback<Void>() {
			@Override
			public void onResult(TaskResult<Void> taskResult) {
				mHandler.sendEmptyMessage(1);
				int code = taskResult.getCode();
				alog.info("taskResult code:" + code);
				if (code == TaskResult.OK) {
					// 成功
					mHandler.sendEmptyMessage(4);
				} else {
					// 显示失败原因
					alog.error(taskResult.getMsg());
					msg = Message.obtain();
					msg.obj = taskResult.getMsg();
					msg.what = 5;
					mHandler.sendMessage(msg);
				}

			}
		};
		DataFetcher.userUpdatePhoneNum(this, reqCallback, reqInfo, false)
				.request(this);
	}

	class CurrentClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			System.out.println("onClick...");
			String tempString = "";
			tempString = getCurrentTextViewText(focusViewFlag);
			int id = v.getId();
			if (id == R.id.btn_key_1) {
				tempString += "1";
				showPasswordText(tempString, focusViewFlag);
			} else if (id == R.id.btn_key_2) {
				tempString += "2";
				showPasswordText(tempString, focusViewFlag);
			} else if (id == R.id.btn_key_3) {
				tempString += "3";
				showPasswordText(tempString, focusViewFlag);
			} else if (id == R.id.btn_key_4) {
				tempString += "4";
				showPasswordText(tempString, focusViewFlag);
			} else if (id == R.id.btn_key_5) {
				tempString += "5";
				showPasswordText(tempString, focusViewFlag);
			} else if (id == R.id.btn_key_6) {
				tempString += "6";
				showPasswordText(tempString, focusViewFlag);
			} else if (id == R.id.btn_key_7) {
				tempString += "7";
				showPasswordText(tempString, focusViewFlag);
			} else if (id == R.id.btn_key_8) {
				tempString += "8";
				showPasswordText(tempString, focusViewFlag);
			} else if (id == R.id.btn_key_9) {
				tempString += "9";
				showPasswordText(tempString, focusViewFlag);
			} else if (id == R.id.btn_key_0) {
				tempString += "0";
				showPasswordText(tempString, focusViewFlag);
			} else if (id == R.id.btn_key_delete) {
				if (tempString.length() > 0) {
					tempString = tempString.substring(0,
							tempString.length() - 1);
				}
				showPasswordText(tempString, focusViewFlag);
			} else if (id == R.id.btn_key_clear) {
				tempString = "";
				showPasswordText(tempString, focusViewFlag);
			} else if (id == R.id.btn_key_foward) {
				if (focusViewFlag > 0) {
					focusViewFlag -= 1;
				}
				setFocusView(focusViewFlag);
			} else if (id == R.id.btn_key_next) {
				if (focusViewFlag < 3) {
					focusViewFlag += 1;
				}
				setFocusView(focusViewFlag);
			} else if (id == R.id.mine_accountsafe_bindphone_phoneno_et) {
				clearFocusView(focusViewFlag);
				focusViewFlag = 0;
				// setFocusToBtn1();
				setFocusView(focusViewFlag);
			} else if (id == R.id.mine_accountsafe_bindphone_verificationcode_et) {
				clearFocusView(focusViewFlag);
				focusViewFlag = 2;
				// setFocusToBtn1();
				setFocusView(focusViewFlag);
			} else if (id == R.id.mine_accountsafe_bindphone_getverificationcode) {
				clearFocusView(focusViewFlag);
				focusViewFlag = 1;
				setFocusView(focusViewFlag);
				sureAction();
			} else if (id == R.id.mine_accountsafe_bindphone_btn) {
				clearFocusView(focusViewFlag);
				focusViewFlag = 3;
				setFocusView(focusViewFlag);
				sureAction();
			} else {
			}
		}

		private void sureAction() {
			if (focusViewFlag == 1) {
				if (!NetUtil.isNetworkAvailable(MineBindPhoneActivity.this,
						true)) {
					NewToast.makeToast(MineBindPhoneActivity.this, "网络异常，请检查网络.",
							Toast.LENGTH_SHORT).show();
					return;
				}
				if (StringsVerifyUitls.verifyStringsFormat(
						StringsVerifyUitls.TYPE_PHONE, newPhoneNoStr.trim()) == StringsVerifyUitls.STATUS_CODE_NULL) {
					NewToast.makeToast(
							MineBindPhoneActivity.this,
							getResources().getString(
									R.string.account_phone_notnull),
							Toast.LENGTH_SHORT).show();
					return;
				}
				if (StringsVerifyUitls.verifyStringsFormat(
						StringsVerifyUitls.TYPE_PHONE, newPhoneNoStr.trim()) == StringsVerifyUitls.STAUS_CODE_INVALIDATE) {
					NewToast.makeToast(
							MineBindPhoneActivity.this,
							getResources().getString(
									R.string.phone_format_error),
							Toast.LENGTH_SHORT).show();
					return;
				}
				getCaptcha(newPhoneNoStr, GetCode.UPDATEPHONE);
			} else if (focusViewFlag == 3) {
				if (StringsVerifyUitls.verifyStringsFormat(
						StringsVerifyUitls.TYPE_PHONE, newPhoneNoStr.trim()) == StringsVerifyUitls.STATUS_CODE_NULL) {
					NewToast.makeToast(
							MineBindPhoneActivity.this,
							getResources().getString(
									R.string.account_phone_notnull),
							Toast.LENGTH_SHORT).show();
					return;
				}
				if (StringsVerifyUitls.verifyStringsFormat(
						StringsVerifyUitls.TYPE_PHONE, newPhoneNoStr.trim()) == StringsVerifyUitls.STAUS_CODE_INVALIDATE) {
					NewToast.makeToast(
							MineBindPhoneActivity.this,
							getResources().getString(
									R.string.phone_format_error),
							Toast.LENGTH_SHORT).show();
					return;
				}
				if (StringsVerifyUitls
						.verifyStringsFormat(StringsVerifyUitls.TYPE_PWD,
								verificationCodeStr.trim()) == StringsVerifyUitls.STATUS_CODE_NULL) {
					NewToast.makeToast(
							MineBindPhoneActivity.this,
							getResources().getString(
									R.string.account_verificationcode_notnull),
							Toast.LENGTH_SHORT).show();
					return;
				}
				if (StringsVerifyUitls
						.verifyStringsFormat(StringsVerifyUitls.TYPE_PWD,
								verificationCodeStr.trim()) == StringsVerifyUitls.STAUS_CODE_INVALIDATE) {
					NewToast.makeToast(
							MineBindPhoneActivity.this,
							getResources().getString(
									R.string.verificationcode_format_error),
							Toast.LENGTH_SHORT).show();
					return;
				}

				userUpdatePhoneNum(newPhoneNoStr, verificationCodeStr);
			}
		}

		private void clearFocusView(int flag) {
			// TODO Auto-generated method stub
			switch (flag) {
			case 0:
				deleteCursor(tvNewPhoneNo);
				tvNewPhoneNo.setBackgroundResource(R.drawable.account_input_bg);
				break;
			case 1:
				btnVerificationCode
						.setBackgroundResource(R.drawable.getverificationcode_btn_normal);
				break;
			case 2:
				deleteCursor(tvVerificationCode);
				tvVerificationCode
						.setBackgroundResource(R.drawable.account_input_bg);
				break;
			case 3:
				btnBindPhone.setBackgroundResource(R.color.account_btn_green);
				break;

			default:
				break;
			}
		}
	}

	private void setFocusView(int flag) {
		if (flag == 0) {
			addCursor(tvNewPhoneNo);
			// deleteCursor(tvVerificationCode);
			tvNewPhoneNo
					.setBackgroundResource(R.drawable.account_input_bg_focus);
			tvVerificationCode
					.setBackgroundResource(R.drawable.account_input_bg);
			btnVerificationCode
					.setBackgroundResource(R.drawable.getverificationcode_btn_normal);
			btnBindPhone.setBackgroundResource(R.color.account_btn_green);
			setFirstTextViewUI();
			setFocusToBtn1();
		} else if (flag == 1) {
			deleteCursor(tvVerificationCode);
			deleteCursor(tvNewPhoneNo);
			tvNewPhoneNo.setBackgroundResource(R.drawable.account_input_bg);
			tvVerificationCode
					.setBackgroundResource(R.drawable.account_input_bg);
			btnVerificationCode
					.setBackgroundResource(R.drawable.getverificationcode_btn_focus);
			btnBindPhone.setBackgroundResource(R.color.account_btn_green);
			btnVerificationCode.requestFocus();
			setAllUnUsable();
		} else if (flag == 2) {
			addCursor(tvVerificationCode);
			tvNewPhoneNo.setBackgroundResource(R.drawable.account_input_bg);
			tvVerificationCode
					.setBackgroundResource(R.drawable.account_input_bg_focus);
			btnVerificationCode
					.setBackgroundResource(R.drawable.getverificationcode_btn_normal);
			btnBindPhone.setBackgroundResource(R.color.account_btn_green);
			setMiddleTextViewUI();
			setFocusToBtn1();
		} else if (flag == 3) {
			tvNewPhoneNo.setBackgroundResource(R.drawable.account_input_bg);
			tvVerificationCode
					.setBackgroundResource(R.drawable.account_input_bg);
			btnVerificationCode
					.setBackgroundResource(R.drawable.getverificationcode_btn_normal);
			btnBindPhone.setBackgroundResource(R.drawable.account_btn_focus);
			btnBindPhone.requestFocus();
			setAllUnUsable();
		}
	}

	private void showPasswordText(String textString, int flag) {
		int length = textString.length();
		if (flag == 0) {
			if (length > KeyboardUtils.PHONENUM_LENGTH) {
				textString = textString.substring(0,
						KeyboardUtils.PHONENUM_LENGTH);
			}
			tvNewPhoneNo.setText(textString);
			newPhoneNoStr = textString;
			addCursor(tvNewPhoneNo);
		} else if (flag == 2) {
			if (length > KeyboardUtils.VERIFICATIONCODE_LENGTH) {
				textString = textString.substring(0,
						KeyboardUtils.VERIFICATIONCODE_LENGTH);
			}
			tvVerificationCode.setText(textString);
			verificationCodeStr = textString;
			addCursor(tvVerificationCode);
		}
	}

	private String getCurrentTextViewText(int flag) {
		if (flag == 0) {
			return newPhoneNoStr;
		} else if (flag == 2) {
			return verificationCodeStr;
		}
		return "";
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		alog.info("keyCode=" + keyCode);
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
			if (focusViewFlag == 1) {
				focusViewFlag = 3;
				setFocusView(focusViewFlag);
			}
		}

		if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
			if (focusViewFlag == 1) {
				focusViewFlag = 0;
				setFocusView(focusViewFlag);
			} else if (focusViewFlag == 3) {
				focusViewFlag = 2;
				setFocusView(focusViewFlag);
				setFocusToBtn1();
			}
		}

		if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
			if (focusViewFlag == 1) {
				focusViewFlag = 2;
				setFocusView(focusViewFlag);
				setFocusToBtn1();
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	private void setEvents(View view) {
		view.setOnClickListener(new CurrentClickListener());
		view.setOnFocusChangeListener(currentFocusChangeListener);
		view.setOnTouchListener(currentTouchListener);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		time.cancel();
		btnVerificationCode.setClickable(true);
		btnVerificationCode
				.setText(this
						.getString(R.string.mine_accountsafe_bindphone_getverification));
	}

	class TimeCount extends CountDownTimer {
		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
		}

		@Override
		public void onFinish() {// 计时完毕时触发
			btnVerificationCode.setText(MineBindPhoneActivity.this
					.getString(R.string.account_verificationcode_tip));
			btnVerificationCode.setClickable(true);
		}

		@Override
		public void onTick(long millisUntilFinished) {// 计时过程显示
			btnVerificationCode.setClickable(false);
			btnVerificationCode.setText(millisUntilFinished / 1000 + "s");
		}
	}
}
