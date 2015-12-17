package com.atet.tvmarket.control.mine;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
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
import com.atet.tvmarket.entity.UserModifyReq;
import com.atet.tvmarket.model.DataFetcher;
import com.atet.tvmarket.model.ReqCallback;
import com.atet.tvmarket.model.task.TaskResult;
import com.atet.tvmarket.utils.KeyboardUtils;
import com.atet.tvmarket.utils.NetUtil;
import com.atet.tvmarket.utils.ScaleViewUtils;
import com.atet.tvmarket.utils.StringsVerifyUitls;
import com.atet.tvmarket.view.NewToast;

/**
 * 
 * @author songwei 该类中方法的说明，可以参照"MineAccountManagerActivity"中
 */
public class MineModifyPasswordActivity extends KeyboardBaseActivity3 {
	private ALog alog = ALog.getLogger(MineModifyPasswordActivity.class);
	private Toast toast = null;
	private TextView tvOldPassword, tvNewPassword, tvConfirmPassword;
	private Button btnConfirm;
	private String oldPasswordStr = "", newPasswordStr = "",
			confirmPasswordStr = ""; // 用于保存当前编辑框的字符串
	private TextView account;
	private int focusViewFlag = 0;
	private int lastFlag = 0;
	private ImageView loadingView;
	private AnimationDrawable mAnimationDrawable;
	Message msg;
	private boolean forward_next_flag = false; // 表征点击"上一个"或"下一个"且聚焦视图为编辑框

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_mine_modifypassword);
		ScaleViewUtils.scaleView(this);
		setBlackTitle(false);
		initView();
		// showBlurBg();

	}

	@Override
	protected void onResume() {
		super.onResume();
		setTitleData();
	}

	private void initView() {
		CurrentClickListener currentClickListener2 = new CurrentClickListener();
		initKeyboard(currentClickListener2);
		account = (TextView) findViewById(R.id.tv_account);
		tvOldPassword = (TextView) findViewById(R.id.mine_accountsafe_modifypassword_oldpassword_et);
		tvOldPassword.setOnClickListener(currentClickListener2);
		tvNewPassword = (TextView) findViewById(R.id.mine_accountsafe_modifypassword_newpassword_et);
		tvNewPassword.setOnClickListener(currentClickListener2);
		tvConfirmPassword = (TextView) findViewById(R.id.mine_accountsafe_modifypassword_confirmpassword_et);
		tvConfirmPassword.setOnClickListener(currentClickListener2);
		btnConfirm = (Button) findViewById(R.id.mine_accountsafe_modifypassword_btn);
		btnConfirm.setOnClickListener(currentClickListener2);
		setEvents(btnConfirm);
		setNextFocusSelf(btnConfirm);
		setFocusView(focusViewFlag);

		loadingView = (ImageView) findViewById(R.id.iv_loading);
		mAnimationDrawable = (AnimationDrawable) loadingView.getDrawable();

		initData();
		setKeyboardNextFocus();
	}

	private void initData() {
		if (BaseApplication.userInfo != null) {
			account.setText(BaseApplication.userInfo.getUserId() + "");
		}
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
				forward_next_flag = true;
				setFocusView(focusViewFlag);
			} else if (id == R.id.btn_key_next) {
				if (focusViewFlag < 3) {
					focusViewFlag += 1;
				}
				if (focusViewFlag == 3) {
					forward_next_flag = false;
				} else {
					forward_next_flag = true;
				}
				setFocusView(focusViewFlag);
			} else if (id == R.id.btn_key_sure) {
				sureAction();
			} else if (id == R.id.mine_accountsafe_modifypassword_oldpassword_et) {
				forward_next_flag = false;
				clearFocusView(focusViewFlag);
				focusViewFlag = 0;
				setFocusView(focusViewFlag);
				setFocusToBtn1();
			} else if (id == R.id.mine_accountsafe_modifypassword_newpassword_et) {
				forward_next_flag = false;
				clearFocusView(focusViewFlag);
				focusViewFlag = 1;
				setFocusView(focusViewFlag);
				setFocusToBtn1();
			} else if (id == R.id.mine_accountsafe_modifypassword_confirmpassword_et) {
				forward_next_flag = false;
				clearFocusView(focusViewFlag);
				focusViewFlag = 2;
				setFocusView(focusViewFlag);
				setFocusToBtn1();
			} else if (id == R.id.mine_accountsafe_modifypassword_btn) {
				forward_next_flag = false;
				clearFocusView(focusViewFlag);
				focusViewFlag = 3;
				setFocusView(focusViewFlag);
				sureAction();
			}
		}

		private void sureAction() {
			if (!NetUtil.isNetworkAvailable(MineModifyPasswordActivity.this,
					true)) {
				NewToast.makeToast(MineModifyPasswordActivity.this, "网络异常，请检查网络.",
						Toast.LENGTH_SHORT).show();
				return;
			}
			if (StringsVerifyUitls.verifyStringsFormat(
					StringsVerifyUitls.TYPE_PWD, oldPasswordStr.trim()) == StringsVerifyUitls.STATUS_CODE_NULL) {
				NewToast.makeToast(
						MineModifyPasswordActivity.this,
						getResources().getString(
								R.string.account_oldpassword_notnull),
						Toast.LENGTH_SHORT).show();
				return;
			}
			if (StringsVerifyUitls.verifyStringsFormat(
					StringsVerifyUitls.TYPE_PWD, oldPasswordStr.trim()) == StringsVerifyUitls.STAUS_CODE_INVALIDATE) {
				NewToast.makeToast(
						MineModifyPasswordActivity.this,
						getResources().getString(
								R.string.oldpassword_format_error),
						Toast.LENGTH_SHORT).show();
				return;
			}

			if (StringsVerifyUitls.verifyStringsFormat(
					StringsVerifyUitls.TYPE_PWD, newPasswordStr.trim()) == StringsVerifyUitls.STATUS_CODE_NULL) {
				NewToast.makeToast(
						MineModifyPasswordActivity.this,
						getResources().getString(
								R.string.account_newpassword_notnull),
						Toast.LENGTH_SHORT).show();
				return;
			}

			if (StringsVerifyUitls.verifyStringsFormat(
					StringsVerifyUitls.TYPE_PWD, newPasswordStr.trim()) == StringsVerifyUitls.STAUS_CODE_INVALIDATE) {
				NewToast.makeToast(
						MineModifyPasswordActivity.this,
						getResources().getString(
								R.string.newpassword_format_error),
						Toast.LENGTH_SHORT).show();
				return;
			}

			if (!newPasswordStr.equals(confirmPasswordStr)) {
				NewToast.makeToast(MineModifyPasswordActivity.this,
						getResources().getString(R.string.password_notsame),
						Toast.LENGTH_SHORT).show();
				return;
			}
			userUpdatePassword(oldPasswordStr, newPasswordStr);
		}

		private void clearFocusView(int flag) {
			// TODO Auto-generated method stub
			switch (flag) {
			case 0:
				deleteCursor(tvOldPassword);
				tvOldPassword
						.setBackgroundResource(R.drawable.account_input_bg);
				break;
			case 1:
				deleteCursor(tvNewPassword);
				tvNewPassword
						.setBackgroundResource(R.drawable.account_input_bg);
				break;
			case 2:
				deleteCursor(tvConfirmPassword);
				tvConfirmPassword
						.setBackgroundResource(R.drawable.account_input_bg);
				break;
			case 3:
				btnConfirm.setBackgroundResource(R.color.common_key_normal);
				break;

			default:
				break;
			}
		}
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
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
				NewToast.makeToast(MineModifyPasswordActivity.this, "修改密码成功",
						Toast.LENGTH_SHORT).show();
				finish();
				break;
			case 3:
				String errorMsg = msg.obj.toString();
				if (errorMsg == "") {
					errorMsg = "系统异常";
				}
				NewToast.makeToast(MineModifyPasswordActivity.this, errorMsg,
						Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
		};
	};

	private void userUpdatePassword(String oldPassword, String newPassword) {

		mHandler.sendEmptyMessage(0);

		// 构造请求参数
		UserModifyReq reqInfo = new UserModifyReq();
		reqInfo.setUserId(BaseApplication.userInfo.getUserId());
		reqInfo.setOldPassword(oldPassword);
		reqInfo.setNewPassword(newPassword);

		ReqCallback<Void> reqCallback = new ReqCallback<Void>() {
			@Override
			public void onResult(TaskResult<Void> taskResult) {

				mHandler.sendEmptyMessage(1);

				int code = taskResult.getCode();
				alog.info("taskResult code:" + code);
				if (code == TaskResult.OK) {
					mHandler.sendEmptyMessage(2);
					// 成功
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
		DataFetcher.userUpdatePassword(this, reqCallback, reqInfo, false)
				.request(this);

	}

	private void setFocusView(int flag) {
		if (flag == 0) {
			addCursor(tvOldPassword);
			deleteCursor(tvNewPassword);
			tvOldPassword
					.setBackgroundResource(R.drawable.account_input_bg_focus);
			tvNewPassword.setBackgroundResource(R.drawable.account_input_bg);
			tvConfirmPassword
					.setBackgroundResource(R.drawable.account_input_bg);
			btnConfirm.setBackgroundResource(R.color.account_btn_green);
			setFirstTextViewUI();
			setFocusToBtn1();
		} else if (flag == 1) {
			addCursor(tvNewPassword);
			deleteCursor(tvOldPassword);
			deleteCursor(tvConfirmPassword);
			tvOldPassword.setBackgroundResource(R.drawable.account_input_bg);
			tvNewPassword
					.setBackgroundResource(R.drawable.account_input_bg_focus);
			btnConfirm.setBackgroundResource(R.color.account_btn_green);
			tvConfirmPassword
					.setBackgroundResource(R.drawable.account_input_bg);
			setMiddleTextViewUI();
			if (!forward_next_flag) {
				setFocusToBtn1();
			}

		} else if (flag == 2) {
			addCursor(tvConfirmPassword);
			deleteCursor(tvNewPassword);
			tvOldPassword.setBackgroundResource(R.drawable.account_input_bg);
			tvNewPassword.setBackgroundResource(R.drawable.account_input_bg);
			tvConfirmPassword
					.setBackgroundResource(R.drawable.account_input_bg_focus);
			btnConfirm.setBackgroundResource(R.color.account_btn_green);
			setMiddleTextViewUI();
			if (!forward_next_flag) {
				setFocusToBtn1();
			}

		} else if (flag == 3) {
			deleteCursor(tvConfirmPassword);
			tvOldPassword.setBackgroundResource(R.drawable.account_input_bg);
			tvNewPassword.setBackgroundResource(R.drawable.account_input_bg);
			tvConfirmPassword
					.setBackgroundResource(R.drawable.account_input_bg);
			btnConfirm.setBackgroundResource(R.drawable.account_btn_focus);
			btnConfirm.requestFocus();
			setAllUnUsable();

		}
	}

	private void showPasswordText(String textString, int flag) {
		int length = textString.length();
		String showString = "";
		if (length > KeyboardUtils.PASSWORD_LENGTH) {
			showString = KeyboardUtils.STARTS.substring(0,
					KeyboardUtils.PASSWORD_LENGTH);
			textString = textString.substring(0, KeyboardUtils.PASSWORD_LENGTH);
		} else {
			showString = KeyboardUtils.STARTS.substring(0, length);
		}
		if (flag == 0) {
			tvOldPassword.setText(showString);
			oldPasswordStr = textString;
			addCursor(tvOldPassword);
		} else if (flag == 1) {
			tvNewPassword.setText(showString);
			newPasswordStr = textString;
			addCursor(tvNewPassword);
		} else if (flag == 2) {
			tvConfirmPassword.setText(showString);
			confirmPasswordStr = textString;
			addCursor(tvConfirmPassword);
		}
	}

	private String getCurrentTextViewText(int flag) {
		if (flag == 0) {
			return oldPasswordStr;
		} else if (flag == 1) {
			return newPasswordStr;
		} else if (flag == 2) {
			return confirmPasswordStr;
		}
		return "";
	}

	private void setEvents(View view) {
		view.setOnClickListener(new CurrentClickListener());
		view.setOnFocusChangeListener(currentFocusChangeListener);
		view.setOnTouchListener(currentTouchListener);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		alog.info("keyCode=" + keyCode);

		if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
			if (focusViewFlag == 3) {
				focusViewFlag = 2;
				setFocusView(focusViewFlag);
				setFocusToBtn1();
			}
		}
		return super.onKeyDown(keyCode, event);
	}

}
