package com.atet.tvmarket.control.mine;

import com.atet.common.logging.ALog;
import com.atet.tvmarket.R;
import com.atet.tvmarket.app.BaseApplication;
import com.atet.tvmarket.entity.UserGetCaptchaReq;
import com.atet.tvmarket.entity.UserInfo;
import com.atet.tvmarket.entity.UserLoginRegisterReq;
import com.atet.tvmarket.entity.UserWeChatLoginReq;
import com.atet.tvmarket.model.DataFetcher;
import com.atet.tvmarket.model.DataHelper;
import com.atet.tvmarket.model.ReqCallback;
import com.atet.tvmarket.model.TimeHelper;
import com.atet.tvmarket.model.task.TaskResult;
import com.atet.tvmarket.model.usertask.UserTaskDaoHelper;
import com.atet.tvmarket.utils.KeyboardUtils;
import com.atet.tvmarket.utils.NetUtil;
import com.atet.tvmarket.utils.ScaleViewUtils;
import com.atet.tvmarket.utils.StringsVerifyUitls;
import com.atet.tvmarket.utils.UmengUtils;
import com.atet.tvmarket.view.NewToast;
import com.atetpay.autologin.GetCode;
import com.atetpay.common.utils.StrUtils;
import com.atetpay.login.bean.resp.WechatLoginResp;
import com.atetpay.login.data.LOGIN;
import com.atetpay.login.fragment.utils.JavaScriptUtils;
import com.atetpay.login.fragment.utils.OnWechatLoginListener;
import com.atetpay.login.fragment.utils.WechatUserInfo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class MineAccountManagerActivity extends KeyboardBaseActivity3 {
	private ALog alog = ALog.getLogger(MineAccountManagerActivity.class);
	private static final int CURRENT_RADIO_LOGIN = 1;
	private static final int CURRENT_RADIO_REGISTER = 2;
	private Intent intent;
	private RadioGroup radGrpTitle;
	private RadioButton radBtnLoginTitle, radBtnRegisterTitle; // 登录/注册 选择
	private Button btnGetVerificationCode, btnSubmit, btnLogin;
	private TextView tvForgetPassword;
	private LinearLayout mLayoutLogin, mLayoutRegister, mLayoutQrcode,
			mLayoutKeyboard;
	private TextView tvRegisterPhoneNo, tvVerificationCode, tvRegisterPassword,
			tvLoginPhoneNo, tvLoginPassword;
	private String loginPhoneNoStr = "", loginPasswordStr = "",
			registerPhoneNoStr = "", verificationCodeStr = "",
			registerPasswordStr = ""; // 用于保存当前编辑框的字符串
	private String type = "";
	private int focusViewFlag = 0;
	private ImageView loadingView;
	private AnimationDrawable mAnimationDrawable;
	private Message msg;
	private WebView wv_wechat;
	private TimeCount time;
	// private static final String WECHAT_LOGIN_URL =
	// "https://open.weixin.qq.com/connect/qrconnect?appid=wx337d17d3f9dbf1eb&redirect_uri=http://58.60.169.67/myuser/callback.do&response_type=code&scope=snsapi_login&state=3d6be0a4035d839573b04816624a415e#wechat_redirect";
//	private static final String WECHAT_LOGIN_URL = "https://open.weixin.qq.com/connect/qrconnect?appid=wx337d17d3f9dbf1eb&redirect_uri=http://61.145.164.122/myuser/callback.do&response_type=code&scope=snsapi_login&state=3d6be0a4035d839573b04816624a415e#wechat_redirect";
	private static final String WECHAT_LOGIN_URL = "https://open.weixin.qq.com/connect/qrconnect?appid=wx337d17d3f9dbf1eb&redirect_uri=http://user.at-et.com/myuser/callback.do&response_type=code&scope=snsapi_login&state=3d6be0a4035d839573b04816624a415e#wechat_redirect";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_mine_accountmanager3);
		ScaleViewUtils.scaleView(this);
		setBlackTitle(false);
		intent = this.getIntent();
		type = intent.getStringExtra(MineAccountChangeActivity.INTENT_KEY);
		initView();
		// 初始化倒计时构造函数
		time = new TimeCount(60000, 1000);
	}

	@Override
	protected void onResume() {
		super.onResume();
		setTitleData();
	}

	private void initView() {
		mLayoutLogin = (LinearLayout) findViewById(R.id.mine_accountmanager_login_layout);
		mLayoutRegister = (LinearLayout) findViewById(R.id.mine_accountmanager_register_layout);
		mLayoutQrcode = (LinearLayout) findViewById(R.id.mine_accountmanager_qrcode_layout);
		mLayoutKeyboard = (LinearLayout) findViewById(R.id.mine_accountmanager_keyboard);
		CurrentClickListener currentClickListener2 = new CurrentClickListener();
		CurrentFocusChangeListener currentFocusChangeListener = new CurrentFocusChangeListener();
		CurrentTouchListener currentTouchListener = new CurrentTouchListener();
		initKeyboard(currentClickListener2);
		setKeyboardNextFocus();
		radGrpTitle = (RadioGroup) findViewById(R.id.mine_accountmanager_title_radgroup);
		radGrpTitle.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				alog.info("[onCheckedChanged]");
				clearFocusView(focusViewFlag);
				if (checkedId == R.id.mine_accountmanager_login_title) {
					showLoginLayout();
					focusViewFlag = 0;
					setFocusView(focusViewFlag);
				} else if (checkedId == R.id.mine_accountmanager_register_title) {
					showRegisterLayout();
					focusViewFlag = 5;
					setFocusView(focusViewFlag);
				} else {
				}
			}
		});
		radBtnLoginTitle = (RadioButton) findViewById(R.id.mine_accountmanager_login_title);
		radBtnLoginTitle.setNextFocusDownId(R.id.btn_key_1);
		radBtnLoginTitle
				.setNextFocusRightId(R.id.mine_accountmanager_register_title);
		radBtnLoginTitle.setOnFocusChangeListener(currentFocusChangeListener);
		radBtnLoginTitle.setOnTouchListener(currentTouchListener);
		radBtnRegisterTitle = (RadioButton) findViewById(R.id.mine_accountmanager_register_title);
		radBtnRegisterTitle.setNextFocusDownId(R.id.btn_key_1);
		radBtnRegisterTitle
				.setOnFocusChangeListener(currentFocusChangeListener);
		radBtnRegisterTitle.setOnTouchListener(currentTouchListener);
		btnLogin = (Button) findViewById(R.id.mine_accountmanager_login_btn);
		setEvents(btnLogin);
		setNextFocusSelf(btnLogin);
		tvForgetPassword = (TextView) findViewById(R.id.mine_accountmanager_forgetpassword);
		// tvForgetPassword.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		setEvents(tvForgetPassword);
		setNextFocusSelf(tvForgetPassword);
		btnGetVerificationCode = (Button) findViewById(R.id.mine_accountmanager_getverificationcode);
		setEvents(btnGetVerificationCode);
		setNextFocusSelf(btnGetVerificationCode);
		btnSubmit = (Button) findViewById(R.id.mine_accountmanager_submit_btn);
		setEvents(btnSubmit);
		setNextFocusSelf(btnSubmit);
		tvRegisterPhoneNo = (TextView) findViewById(R.id.mine_accountmanager_register_phoneno_et);
		tvRegisterPhoneNo.setOnClickListener(currentClickListener2);
		tvVerificationCode = (TextView) findViewById(R.id.mine_accountmanager_verificationcode_et);
		tvVerificationCode.setOnClickListener(currentClickListener2);
		tvRegisterPassword = (TextView) findViewById(R.id.mine_accountmanager_register_password_et);
		tvRegisterPassword.setOnClickListener(currentClickListener2);
		tvLoginPhoneNo = (TextView) findViewById(R.id.mine_accountmanager_login_phoneno_et);
		tvLoginPhoneNo.setOnClickListener(currentClickListener2);
		tvLoginPassword = (TextView) findViewById(R.id.mine_accountmanager_login_password_et);
		tvLoginPassword.setOnClickListener(currentClickListener2);
		alog.info("type:" + type);
		if (AccountchangeAdapter.REGISTER.equals(type)) {
			radBtnRegisterTitle.requestFocus();
			radBtnRegisterTitle.requestFocusFromTouch();
		} else {
			radBtnLoginTitle.requestFocus();
			radBtnLoginTitle.requestFocusFromTouch();
		}

		loadingView = (ImageView) findViewById(R.id.iv_loading);
		mAnimationDrawable = (AnimationDrawable) loadingView.getDrawable();

		wv_wechat = (WebView) findViewById(R.id.user_login_wv_qrcode);
		initWeChat();
	}

	private void initWeChat() {
		wv_wechat.getSettings().setJavaScriptEnabled(true);
		wv_wechat.getSettings().setTextZoom(130);
		wv_wechat.loadUrl(WECHAT_LOGIN_URL);
		/*wv_wechat.getSettings().setLayoutAlgorithm(
				LayoutAlgorithm.NARROW_COLUMNS);
		wv_wechat.getSettings().setLayoutAlgorithm(
				LayoutAlgorithm.SINGLE_COLUMN);
		wv_wechat.getSettings().setLoadWithOverviewMode(true);*/
		wv_wechat.setWebViewClient(new WebViewClient() {

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
			}
			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				view.loadUrl("javascript:androidResponse();void(0)");
			}

		});
		wv_wechat.addJavascriptInterface(new JavaScriptUtils(this, loginListener),"cpjs");
	}
	OnWechatLoginListener loginListener = new OnWechatLoginListener() {
		
		@Override
		public void result(int code, String desc, WechatUserInfo userInfo) {
			if(code == LOGIN.SUCCESS){
				//userWeChatLogin(userInfo.getUsername(),userInfo.getUserId(),"");
				UserInfo usInfo = new UserInfo();
				usInfo.setBalance(userInfo.getBalance());
				usInfo.setCoupon(userInfo.getCoupon());
				usInfo.setIcon(userInfo.getIcon());
				usInfo.setIntegral(userInfo.getIntegral());
				usInfo.setUserId(userInfo.getUserId());
				usInfo.setUserName(userInfo.getUsername());
				BaseApplication.userInfo = usInfo;
				DataHelper.updateUserInfo(usInfo);
				UserTaskDaoHelper.saveWeChatFirstLogin(getApplicationContext(), userInfo.getUserId(), TimeHelper.getCurTime());
				mHandler.sendEmptyMessage(4);
			}
			else{
				NewToast.makeToast(MineAccountManagerActivity.this,desc,NewToast.LENGTH_LONG).show();
			}
		}
	};

	public class CurrentFocusChangeListener implements OnFocusChangeListener {

		private View view;

		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			alog.info("onFocusChange..." + hasFocus);
			if (hasFocus) {
				// 如果聚焦在登录标题上，则显示登录相关输入框，同时标题放大
				if (v.getId() == R.id.mine_accountmanager_login_title) {
					// 焦点聚焦在“登录”标题是，“登录”界面不可见，说明是从“注册”界面切换到登录界面的
					if (mLayoutLogin.getVisibility() == View.INVISIBLE) {
						showLoginLayout();
					}
					v.setScaleX(1.17f);
					v.setScaleY(1.17f);
					clearFocusView(focusViewFlag);
					setFocusView(0);
					radBtnLoginTitle.setChecked(true);
				} else if (v.getId() == R.id.mine_accountmanager_register_title) {
					if (mLayoutRegister.getVisibility() == View.INVISIBLE) {
						showRegisterLayout();
					}
					v.setScaleX(1.17f);
					v.setScaleY(1.17f);
					clearFocusView(focusViewFlag);
					setFocusView(5);
					radBtnRegisterTitle.setChecked(true);
				}
				alog.info("[onFocusChange]" + v.toString());

				if (v.isInTouchMode() && v == view) {
					v.performClick();
				} else {
					view = null;
				}

			} else {
				// 标题标签失去焦点时需要缩小为原来尺寸
				if (v.getId() == R.id.mine_accountmanager_login_title
						|| v.getId() == R.id.mine_accountmanager_register_title) {
					v.setScaleX(1.0f);
					v.setScaleY(1.0f);
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

	class CurrentClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			String tempString = getCurrentTextViewText(focusViewFlag);
			alog.info("onClick...");
			int id = v.getId();
			if (id == R.id.btn_key_1) {
				tempString += "1";
				showText(tempString, focusViewFlag);
			} else if (id == R.id.btn_key_2) {
				tempString += "2";
				showText(tempString, focusViewFlag);
			} else if (id == R.id.btn_key_3) {
				tempString += "3";
				showText(tempString, focusViewFlag);
			} else if (id == R.id.btn_key_4) {
				tempString += "4";
				showText(tempString, focusViewFlag);
			} else if (id == R.id.btn_key_5) {
				tempString += "5";
				showText(tempString, focusViewFlag);
			} else if (id == R.id.btn_key_6) {
				tempString += "6";
				showText(tempString, focusViewFlag);
			} else if (id == R.id.btn_key_7) {
				tempString += "7";
				showText(tempString, focusViewFlag);
			} else if (id == R.id.btn_key_8) {
				tempString += "8";
				showText(tempString, focusViewFlag);
			} else if (id == R.id.btn_key_9) {
				tempString += "9";
				showText(tempString, focusViewFlag);
			} else if (id == R.id.btn_key_0) {
				tempString += "0";
				showText(tempString, focusViewFlag);
			} else if (id == R.id.btn_key_delete) {
				if (tempString.length() > 0) {
					tempString = tempString.substring(0,
							tempString.length() - 1);
				}
				showText(tempString, focusViewFlag);
			} else if (id == R.id.btn_key_clear) {
				tempString = "";
				showText(tempString, focusViewFlag);
			} else if (id == R.id.btn_key_foward) {
				if ((focusViewFlag != 0 && focusViewFlag != 5)) {
					focusViewFlag -= 1;
				}
				setFocusView(focusViewFlag);
			} else if (id == R.id.btn_key_next) {
				alog.info("[onclick]next---> focusViewFlag=" + focusViewFlag);
				if ((focusViewFlag != 4 && focusViewFlag != 10)) {
					// if (focusViewFlag == 6) {
					// focusViewFlag += 2;
					// } else {
					focusViewFlag += 1;
					// }
				}
				if (focusViewFlag == 4 || focusViewFlag == 10) {
					// setFocusToBtnsure();
				}
				setFocusView(focusViewFlag);
			} else if (id == R.id.mine_accountmanager_login_phoneno_et) {
				clearFocusView(focusViewFlag);
				focusViewFlag = 1;
				showKeyboard();
				setFocusView(focusViewFlag);
				setFocusToBtn1();
			} else if (id == R.id.mine_accountmanager_login_password_et) {
				clearFocusView(focusViewFlag);
				focusViewFlag = 2;
				showKeyboard();
				setFocusView(focusViewFlag);
				setFocusToBtn1();
			} else if (id == R.id.mine_accountmanager_login_btn) {
				alog.info("login");
				clearFocusView(focusViewFlag);
				focusViewFlag = 3;
				setFocusView(focusViewFlag);
				sureAction(focusViewFlag);
			} else if (id == R.id.mine_accountmanager_forgetpassword) {
				clearFocusView(focusViewFlag);
				focusViewFlag = 4;
				setFocusView(focusViewFlag);
				sureAction(focusViewFlag);
			} else if (id == R.id.mine_accountmanager_register_phoneno_et) {
				clearFocusView(focusViewFlag);
				focusViewFlag = 6;
				setFocusView(focusViewFlag);
				setFocusToBtn1();
			} else if (id == R.id.mine_accountmanager_getverificationcode) {
				clearFocusView(focusViewFlag);
				focusViewFlag = 7;
				setFocusToNextBtn();
				setFocusView(focusViewFlag);
				sureAction(focusViewFlag);
			} else if (id == R.id.mine_accountmanager_verificationcode_et) {
				clearFocusView(focusViewFlag);
				focusViewFlag = 8;
				setFocusView(focusViewFlag);
				setFocusToBtn1();
			} else if (id == R.id.mine_accountmanager_register_password_et) {
				clearFocusView(focusViewFlag);
				focusViewFlag = 9;
				setFocusView(focusViewFlag);
				setFocusToBtn1();
			} else if (id == R.id.mine_accountmanager_submit_btn) {
				clearFocusView(focusViewFlag);
				focusViewFlag = 10;
				setFocusToForwardBtn();
				setFocusView(focusViewFlag);
				sureAction(focusViewFlag);
			} else {
			}
		}
	}

	public class CurrentTouchListener implements OnTouchListener {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			alog.info("onTouch...");
			if (event.getAction() == MotionEvent.ACTION_UP) {
				OnFocusChangeListener listener = v.getOnFocusChangeListener();
				if (listener != null
						&& listener instanceof CurrentFocusChangeListener) {
					((CurrentFocusChangeListener) listener).setView(v);
				}
			}
			return false;
		}
	}

	/**
	 * 
	 * @Title: showQrcode
	 * @Description: TODO(显示二维码)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	private void showQrcode() {
		mLayoutKeyboard.setVisibility(View.GONE);
		mLayoutQrcode.setVisibility(View.VISIBLE);
	}

	/**
	 * 
	 * @Title: showKeyboard
	 * @Description: TODO(显示数字键盘)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	private void showKeyboard() {
		mLayoutKeyboard.setVisibility(View.VISIBLE);
		mLayoutQrcode.setVisibility(View.GONE);
	}

	/**
	 * 
	 * @Title: showLoginLayout
	 * @Description: TODO(显示登录视图)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	private void showLoginLayout() {
		mLayoutLogin.setVisibility(View.VISIBLE);
		mLayoutRegister.setVisibility(View.GONE);
		showQrcode();
	}

	/**
	 * 
	 * @Title: showRegisterLayout
	 * @Description: TODO(显示注册界面视图)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	private void showRegisterLayout() {
		mLayoutLogin.setVisibility(View.GONE);
		mLayoutRegister.setVisibility(View.VISIBLE);
		showKeyboard();
	}

	/**
	 * 
	 * @Title: UserRegister
	 * @Description: TODO(用户注册)
	 * @param @param phoneNum
	 * @param @param password
	 * @param @param captcha 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	private void UserRegister(final String phoneNum, final String password, String captcha) {
		mHandler.sendEmptyMessage(0);
		// 构造请求参数
		UserLoginRegisterReq reqInfo = new UserLoginRegisterReq();
		reqInfo.setPhoneNum(phoneNum);
		reqInfo.setPassword(password);
		reqInfo.setCaptcha(captcha);

		ReqCallback<UserInfo> reqCallback = new ReqCallback<UserInfo>() {

			@Override
			public void onResult(TaskResult<UserInfo> taskResult) {
				mHandler.sendEmptyMessage(1);
				int code = taskResult.getCode();
				alog.info("taskResult code:" + code);
				if (code == TaskResult.OK) {
					// Umeng统计第一次登录
					UmengUtils.setOnEvent(MineAccountManagerActivity.this,
							UmengUtils.MINE_ACCOUNT_FIRST_LOGIN);
					UserInfo info = taskResult.getData();
					BaseApplication.userInfo = info;
					alog.info(info);
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

		DataFetcher.userRegister(this, reqCallback, reqInfo, false).request(
				this);
	}
	
	/**
	 * 
	 * @Title: UserLogin
	 * @Description: TODO(用户登录)
	 * @param @param phoneNum
	 * @param @param password 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	private void UserLogin(String phoneNum, String password) {
		mHandler.sendEmptyMessage(0);
		// 构造请求参数
		UserLoginRegisterReq reqInfo = new UserLoginRegisterReq();
		reqInfo.setPhoneNum(phoneNum);
		reqInfo.setPassword(password);

		ReqCallback<UserInfo> reqCallback = new ReqCallback<UserInfo>() {
			@Override
			public void onResult(TaskResult<UserInfo> taskResult) {
				mHandler.sendEmptyMessage(1);
				int code = taskResult.getCode();
				alog.info("taskResult code:" + code);
				if (code == TaskResult.OK) {
					UserInfo info = taskResult.getData();
					BaseApplication.userInfo = info;
					alog.info(info);
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
		DataFetcher.userLogin(this, reqCallback, reqInfo, false).request(this);

	}

	/**
	 * 
	 * @Title: getCaptcha
	 * @Description: TODO(获取验证码)
	 * @param @param phoneNum
	 * @param @param type 设定文件
	 * @return void 返回类型
	 * @throws
	 */
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
					mHandler.sendEmptyMessage(6);
				} else {
					// 显示失败原因
					alog.error(taskResult.getMsg());
					msg = Message.obtain();
					msg.obj = taskResult.getMsg();
					msg.what = 7;
					mHandler.sendMessage(msg);
				}

			}
		};
		DataFetcher.userGetCaptcha(this, reqCallback, reqInfo, false).request(
				this);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		if (requestCode == 9001) {
			radBtnLoginTitle.setChecked(true);
		}
	}

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			String errMsg = "";
			switch (msg.what) {
			case 0:
				loadingView.setVisibility(View.VISIBLE);
				mAnimationDrawable.start();
				break;
			case 1:
				mAnimationDrawable.stop();
				loadingView.setVisibility(View.INVISIBLE);
				break;
			case 2:// 注册成功
				NewToast.makeToast(MineAccountManagerActivity.this, "注册成功",
						Toast.LENGTH_SHORT).show();
				//radBtnLoginTitle.setChecked(true);
				setResult(8001);
				finish();
				break;
			case 3:// 注册失败
				errMsg = msg.obj.toString();
				if (errMsg == "") {
					errMsg = "系统异常";
				}
				NewToast.makeToast(MineAccountManagerActivity.this, errMsg,
						Toast.LENGTH_SHORT).show();
				break;
			case 4:// 登录成功
				NewToast.makeToast(MineAccountManagerActivity.this, "登录成功",
						Toast.LENGTH_SHORT).show();
				setResult(8001);
				finish();
				break;
			case 5:// 登录失败
				errMsg = msg.obj.toString();
				if (errMsg == "") {
					errMsg = "系统异常";
				}
				NewToast.makeToast(MineAccountManagerActivity.this, errMsg,
						Toast.LENGTH_SHORT).show();
				break;
			case 6:// 获取注册验证码成功
				NewToast.makeToast(MineAccountManagerActivity.this, "获取注册验证码成功",
						Toast.LENGTH_SHORT).show();
				time.start();
				break;
			case 7:// 获取注册验证码失败
				errMsg = msg.obj.toString();
				if (errMsg == "") {
					errMsg = "系统异常";
				}
				NewToast.makeToast(MineAccountManagerActivity.this, errMsg,
						Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
		}

	};

	/**
	 * 
	 * @Title: sureAction
	 * @Description: TODO(设置按钮事件)
	 * @param @param flag 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	private void sureAction(int flag) {
		if (flag == 3) {

			if (!NetUtil.isNetworkAvailable(this, true)) {
				NewToast.makeToast(this, "网络异常，请检查网络.", Toast.LENGTH_SHORT).show();
				return;
			}

			if (StringsVerifyUitls.verifyStringsFormat(
					StringsVerifyUitls.TYPE_PHONE, loginPhoneNoStr.trim()) == StringsVerifyUitls.STATUS_CODE_NULL) {
				NewToast.makeToast(
						this,
						getResources()
								.getString(R.string.account_phone_notnull),
						Toast.LENGTH_SHORT).show();
				return;
			}
			if (StringsVerifyUitls.verifyStringsFormat(
					StringsVerifyUitls.TYPE_PHONE, loginPhoneNoStr.trim()) == StringsVerifyUitls.STAUS_CODE_INVALIDATE) {
				NewToast.makeToast(this,
						getResources().getString(R.string.phone_format_error),
						Toast.LENGTH_SHORT).show();
				return;
			}

			if (StringsVerifyUitls.verifyStringsFormat(
					StringsVerifyUitls.TYPE_PWD, loginPasswordStr.trim()) == StringsVerifyUitls.STATUS_CODE_NULL) {
				NewToast.makeToast(
						this,
						getResources().getString(
								R.string.account_password_notnull),
						Toast.LENGTH_SHORT).show();
				return;
			}

			if (StringsVerifyUitls.verifyStringsFormat(
					StringsVerifyUitls.TYPE_PWD, loginPasswordStr.trim()) == StringsVerifyUitls.STAUS_CODE_INVALIDATE) {
				NewToast.makeToast(
						this,
						getResources()
								.getString(R.string.password_format_error),
						Toast.LENGTH_SHORT).show();
				return;
			}
			UserLogin(loginPhoneNoStr, loginPasswordStr);
		} else if (flag == 4) { // "忘记密码"按钮
			// ScreenShot.screenShotBmp = ScreenShot
			// .takeScreenShot(MineAccountManagerActivity.this);
			Intent intent = new Intent(MineAccountManagerActivity.this,
					MineFindPasswordActivity.class);
			startActivityForResult(intent, 9001);
			// startActivity(intent);
		} else if (flag == 7) {
			if (!NetUtil.isNetworkAvailable(this, true)) {
				NewToast.makeToast(this, "网络异常，请检查网络.", Toast.LENGTH_SHORT).show();
				return;
			}
			if (StringsVerifyUitls.verifyStringsFormat(
					StringsVerifyUitls.TYPE_PHONE, registerPhoneNoStr.trim()) == StringsVerifyUitls.STATUS_CODE_NULL) {
				NewToast.makeToast(
						this,
						getResources()
								.getString(R.string.account_phone_notnull),
						Toast.LENGTH_SHORT).show();
				return;
			}
			if (StringsVerifyUitls.verifyStringsFormat(
					StringsVerifyUitls.TYPE_PHONE, registerPhoneNoStr.trim()) == StringsVerifyUitls.STAUS_CODE_INVALIDATE) {
				NewToast.makeToast(this,
						getResources().getString(R.string.phone_format_error),
						Toast.LENGTH_SHORT).show();
				return;
			}
			getCaptcha(registerPhoneNoStr, GetCode.SIGNUP);
		} else if (flag == 10) {
			if (!NetUtil.isNetworkAvailable(this, true)) {
				NewToast.makeToast(this, "网络异常，请检查网络.", Toast.LENGTH_SHORT).show();
				return;
			}
			if (StringsVerifyUitls.verifyStringsFormat(
					StringsVerifyUitls.TYPE_PHONE, registerPhoneNoStr.trim()) == StringsVerifyUitls.STATUS_CODE_NULL) {
				NewToast.makeToast(
						this,
						getResources()
								.getString(R.string.account_phone_notnull),
						Toast.LENGTH_SHORT).show();
				return;
			}

			if (StringsVerifyUitls.verifyStringsFormat(
					StringsVerifyUitls.TYPE_PHONE, registerPhoneNoStr.trim()) == StringsVerifyUitls.STAUS_CODE_INVALIDATE) {
				NewToast.makeToast(this,
						getResources().getString(R.string.phone_format_error),
						Toast.LENGTH_SHORT).show();
				return;
			}

			if (StringsVerifyUitls.verifyStringsFormat(
					StringsVerifyUitls.TYPE_PWD, verificationCodeStr.trim()) == StringsVerifyUitls.STATUS_CODE_NULL) {
				NewToast.makeToast(
						this,
						getResources().getString(
								R.string.account_verificationcode_notnull),
						Toast.LENGTH_SHORT).show();
				return;
			}

			if (StringsVerifyUitls.verifyStringsFormat(
					StringsVerifyUitls.TYPE_PWD, verificationCodeStr.trim()) == StringsVerifyUitls.STAUS_CODE_INVALIDATE) {
				NewToast.makeToast(
						this,
						getResources().getString(
								R.string.verificationcode_format_error),
						Toast.LENGTH_SHORT).show();
				return;
			}

			if (StringsVerifyUitls.verifyStringsFormat(
					StringsVerifyUitls.TYPE_PWD, registerPasswordStr.trim()) == StringsVerifyUitls.STATUS_CODE_NULL) {
				NewToast.makeToast(
						this,
						getResources().getString(
								R.string.account_password_notnull),
						Toast.LENGTH_SHORT).show();
				return;
			}

			if (StringsVerifyUitls.verifyStringsFormat(
					StringsVerifyUitls.TYPE_PWD, registerPasswordStr.trim()) == StringsVerifyUitls.STAUS_CODE_INVALIDATE) {
				NewToast.makeToast(
						this,
						getResources()
								.getString(R.string.password_format_error),
						Toast.LENGTH_SHORT).show();
				return;
			}
			alog.info("registerPhoneNoStr=" + registerPhoneNoStr
					+ ";registerPasswordStr=" + registerPasswordStr
					+ "verificationCodeStr=" + verificationCodeStr);
			UserRegister(registerPhoneNoStr, registerPasswordStr,
					verificationCodeStr);
		}
	}

	/**
	 * 
	 * @Title: setFocusView
	 * @Description: TODO(设置当前编辑框为选中状态)
	 * @param @param flag 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	private void setFocusView(int flag) {
		if (flag == 0) {
			radBtnLoginTitle.setChecked(true);
			radBtnLoginTitle.requestFocus();
			deleteCursor(tvLoginPhoneNo);
			tvLoginPhoneNo.setBackgroundResource(R.drawable.account_input_bg);
			setAllUnUsable();
		} else if (flag == 1) {
			addCursor(tvLoginPhoneNo);
			deleteCursor(tvLoginPassword);
			tvLoginPhoneNo
					.setBackgroundResource(R.drawable.account_input_bg_focus);
			tvLoginPassword.setBackgroundResource(R.drawable.account_input_bg);
			btnLogin.setBackgroundResource(R.color.account_btn_green);
			tvForgetPassword.setTextColor(getResources().getColor(
					R.color.account_forgetpassword_tv_focus));
			setMiddleTextViewUI();
		} else if (flag == 2) {
			deleteCursor(tvLoginPhoneNo);
			addCursor(tvLoginPassword);
			tvLoginPhoneNo.setBackgroundResource(R.drawable.account_input_bg);
			tvLoginPassword
					.setBackgroundResource(R.drawable.account_input_bg_focus);
			btnLogin.setBackgroundResource(R.color.account_btn_green);
			tvForgetPassword.setTextColor(getResources().getColor(
					R.color.account_forgetpassword_tv_focus));
			setMiddleTextViewUI();
		} else if (flag == 3) {
			deleteCursor(tvLoginPassword);
			tvLoginPhoneNo.setBackgroundResource(R.drawable.account_input_bg);
			tvLoginPassword.setBackgroundResource(R.drawable.account_input_bg);
			btnLogin.setBackgroundResource(R.drawable.account_btn_focus);
			btnLogin.requestFocus();
			tvForgetPassword.setTextColor(getResources().getColor(
					R.color.account_forgetpassword_tv_focus));
			// setMiddleButtonUI();
			setAllUnUsable();
		} else if (flag == 4) {
			tvLoginPhoneNo.setBackgroundResource(R.drawable.account_input_bg);
			tvLoginPassword.setBackgroundResource(R.drawable.account_input_bg);
			btnLogin.setBackgroundResource(R.color.account_btn_green);
			tvForgetPassword.setTextColor(getResources()
					.getColor(R.color.white));
			tvForgetPassword.requestFocus();
			// setLastButtonUI();
			setAllUnUsable();
		} else if (flag == 5) {
			radBtnRegisterTitle.setChecked(true);
			radBtnRegisterTitle.requestFocus();
			radBtnRegisterTitle.requestFocusFromTouch();
			deleteCursor(tvRegisterPhoneNo);
			tvRegisterPhoneNo
					.setBackgroundResource(R.drawable.account_input_bg);
			// setFirstButtonUI();
			setAllUnUsable();
		} else if (flag == 6) {
			addCursor(tvRegisterPhoneNo);
			deleteCursor(tvVerificationCode);
			tvRegisterPhoneNo
					.setBackgroundResource(R.drawable.account_input_bg_focus);
			tvVerificationCode
					.setBackgroundResource(R.drawable.account_input_bg);
			btnGetVerificationCode
					.setBackgroundResource(R.drawable.getverificationcode_btn_normal);
			tvRegisterPassword
					.setBackgroundResource(R.drawable.account_input_bg);
			btnSubmit.setBackgroundResource(R.color.account_btn_green);
			setMiddleTextViewUI();
		} else if (flag == 7) {
			deleteCursor(tvRegisterPhoneNo);
			deleteCursor(tvVerificationCode);
			deleteCursor(tvRegisterPassword);
			tvRegisterPhoneNo
					.setBackgroundResource(R.drawable.account_input_bg);
			tvVerificationCode
					.setBackgroundResource(R.drawable.account_input_bg);
			btnGetVerificationCode
					.setBackgroundResource(R.drawable.getverificationcode_btn_focus);
			tvRegisterPassword
					.setBackgroundResource(R.drawable.account_input_bg);
			btnSubmit.setBackgroundResource(R.color.account_btn_green);
			btnGetVerificationCode.requestFocus();
			// setMiddleButtonUI();
			setAllUnUsable();
		} else if (flag == 8) {
			deleteCursor(tvRegisterPhoneNo);
			deleteCursor(tvRegisterPassword);
			addCursor(tvVerificationCode);
			tvRegisterPhoneNo
					.setBackgroundResource(R.drawable.account_input_bg);
			tvVerificationCode
					.setBackgroundResource(R.drawable.account_input_bg_focus);
			btnGetVerificationCode
					.setBackgroundResource(R.drawable.getverificationcode_btn_normal);
			tvRegisterPassword
					.setBackgroundResource(R.drawable.account_input_bg);
			btnSubmit.setBackgroundResource(R.color.account_btn_green);
			setMiddleTextViewUI();
		} else if (flag == 9) {
			deleteCursor(tvRegisterPhoneNo);
			deleteCursor(tvVerificationCode);
			addCursor(tvRegisterPassword);
			tvRegisterPhoneNo
					.setBackgroundResource(R.drawable.account_input_bg);
			tvVerificationCode
					.setBackgroundResource(R.drawable.account_input_bg);
			btnGetVerificationCode
					.setBackgroundResource(R.drawable.getverificationcode_btn_normal);
			tvRegisterPassword
					.setBackgroundResource(R.drawable.account_input_bg_focus);
			btnSubmit.setBackgroundResource(R.color.account_btn_green);
			setMiddleTextViewUI();
		} else if (flag == 10) {
			deleteCursor(tvRegisterPassword);
			tvRegisterPhoneNo
					.setBackgroundResource(R.drawable.account_input_bg);
			tvVerificationCode
					.setBackgroundResource(R.drawable.account_input_bg);
			btnGetVerificationCode
					.setBackgroundResource(R.drawable.getverificationcode_btn_normal);
			tvRegisterPassword
					.setBackgroundResource(R.drawable.account_input_bg);
			btnSubmit.setBackgroundResource(R.drawable.account_btn_focus);
			// setLastButtonUI();
			btnSubmit.requestFocus();
			setAllUnUsable();
		}

	}

	/**
	 * 
	 * @Title: clearFocusView
	 * @Description: TODO(取消当前编辑框或按钮相关的聚焦元素)
	 * @param @param flag 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	private void clearFocusView(int flag) {

		if (flag == 0) {

		} else if (flag == 1) {
			tvLoginPhoneNo.setBackgroundResource(R.drawable.account_input_bg);
			deleteCursor(tvLoginPhoneNo);
		} else if (flag == 2) {
			tvLoginPassword.setBackgroundResource(R.drawable.account_input_bg);
			deleteCursor(tvLoginPassword);
		} else if (flag == 3) {
			alog.info("[clearFocusView]flag=3");
			btnLogin.setBackgroundResource(R.color.account_btn_green);
		} else if (flag == 4) {
			tvForgetPassword.setTextColor(getResources().getColor(
					R.color.account_forgetpassword_tv_focus));
		} else if (flag == 5) {

		} else if (flag == 6) {
			deleteCursor(tvRegisterPhoneNo);
			tvRegisterPhoneNo
					.setBackgroundResource(R.drawable.account_input_bg);
		} else if (flag == 7) {
			btnGetVerificationCode
					.setBackgroundResource(R.drawable.getverificationcode_btn_normal);

		} else if (flag == 8) {
			deleteCursor(tvVerificationCode);
			tvVerificationCode
					.setBackgroundResource(R.drawable.account_input_bg);
		} else if (flag == 9) {
			deleteCursor(tvRegisterPassword);
			tvRegisterPassword
					.setBackgroundResource(R.drawable.account_input_bg);
		} else if (flag == 10) {
			btnSubmit.setBackgroundResource(R.color.account_btn_green);
		} else {

		}
	}

	/**
	 * 
	 * @Title: showText
	 * @Description: TODO(显示输入框中的内容)
	 * @param @param textString
	 * @param @param flag 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	private void showText(String textString, int flag) {
		int length = textString.length();
		if (flag == 1) {
			if (length > KeyboardUtils.PHONENUM_LENGTH) {
				textString = textString.substring(0,
						KeyboardUtils.PHONENUM_LENGTH);
			}
			tvLoginPhoneNo.setText(textString);
			loginPhoneNoStr = textString;
			addCursor(tvLoginPhoneNo);
		} else if (flag == 2) {
			String showString = "";
			if (length > KeyboardUtils.PASSWORD_LENGTH) {
				showString = KeyboardUtils.STARTS.substring(0,
						KeyboardUtils.PASSWORD_LENGTH);
				textString = textString.substring(0,
						KeyboardUtils.PASSWORD_LENGTH);
			} else {
				showString = KeyboardUtils.STARTS.substring(0, length);
			}
			tvLoginPassword.setText(showString);
			loginPasswordStr = textString;
			addCursor(tvLoginPassword);
		} else if (flag == 6) {
			if (length > KeyboardUtils.PHONENUM_LENGTH) {
				textString = textString.substring(0,
						KeyboardUtils.PHONENUM_LENGTH);
			}
			tvRegisterPhoneNo.setText(textString);
			registerPhoneNoStr = textString;
			addCursor(tvRegisterPhoneNo);
		} else if (flag == 8) {
			if (length > KeyboardUtils.VERIFICATIONCODE_LENGTH) {
				textString = textString.substring(0,
						KeyboardUtils.VERIFICATIONCODE_LENGTH);
			}
			tvVerificationCode.setText(textString);
			verificationCodeStr = textString;
			addCursor(tvVerificationCode);
		} else if (flag == 9) {
			String showString = "";
			if (length > KeyboardUtils.PASSWORD_LENGTH) {
				showString = KeyboardUtils.STARTS.substring(0,
						KeyboardUtils.PASSWORD_LENGTH);
				textString = textString.substring(0,
						KeyboardUtils.PASSWORD_LENGTH);
			} else {
				showString = KeyboardUtils.STARTS.substring(0, length);
			}
			tvRegisterPassword.setText(showString);
			registerPasswordStr = textString;
			addCursor(tvRegisterPassword);
		}
	}

	/**
	 * 
	 * @Title: getCurrentTextViewText
	 * @Description: TODO(获取当前输入框中已输入内容)
	 * @param @param flag
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	private String getCurrentTextViewText(int flag) {
		if (flag == 1) {
			return loginPhoneNoStr;
		} else if (flag == 2) {
			return loginPasswordStr;
		} else if (flag == 6) {
			return registerPhoneNoStr;
		} else if (flag == 8) {
			return verificationCodeStr;
		} else if (flag == 9) {
			return registerPasswordStr;
		}
		return "";
	}

	private void setEvents(View view) {
		view.setOnClickListener(new CurrentClickListener());
		view.setOnFocusChangeListener(new CurrentFocusChangeListener());
		view.setOnTouchListener(new CurrentTouchListener());
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		alog.info("keyCode=" + keyCode);
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
			if (getCurrentFocus().getId() == R.id.mine_accountmanager_login_title) {
				focusViewFlag = 1;
				setFocusView(focusViewFlag);
				showKeyboard();
				alog.info("login key down!!!");
			} else if (getCurrentFocus().getId() == R.id.mine_accountmanager_register_title) {
				focusViewFlag = 6;
				// setFocusToBtn1();
				setFocusView(focusViewFlag);
				alog.info("register key down!!!");
			}

			if (focusViewFlag == 3) {
				focusViewFlag = 4;
				setFocusView(focusViewFlag);
			} else if (focusViewFlag == 7) {
				focusViewFlag = 9;
				setFocusView(focusViewFlag);
				setFocusToBtn1();
			}
		}

		if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
			if (focusViewFlag == 4) {
				focusViewFlag = 3;
				setFocusView(focusViewFlag);
			} else if (focusViewFlag == 3) {
				focusViewFlag = 2;
				setFocusView(focusViewFlag);
				setFocusToBtn1();
			} else if (focusViewFlag == 7) {
				focusViewFlag = 6;
				setFocusView(focusViewFlag);
				setFocusToBtn1();
			} else if (focusViewFlag == 10) {
				focusViewFlag = 9;
				setFocusView(focusViewFlag);
				setFocusToBtn1();
			}
		}

		if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
			if (focusViewFlag == 7) {
				focusViewFlag = 8;
				setFocusView(focusViewFlag);
				setFocusToBtn1();
			}
		}

		if (keyCode == KeyEvent.KEYCODE_BUTTON_B
				|| keyCode == KeyEvent.KEYCODE_B
				|| keyCode == KeyEvent.KEYCODE_BACK) {
			if (focusViewFlag == 1 || focusViewFlag == 2 || focusViewFlag == 3
					|| focusViewFlag == 4) {
				clearFocusView(focusViewFlag);
				focusViewFlag = 0;
				setFocusView(0);
				if (focusViewFlag == 3) {

				} else if (focusViewFlag == 4) {

				}
			} else if (focusViewFlag == 6 || focusViewFlag == 7
					|| focusViewFlag == 8 || focusViewFlag == 9
					|| focusViewFlag == 10) {
				clearFocusView(focusViewFlag);
				focusViewFlag = 5;
				setFocusView(focusViewFlag);
			} else if (focusViewFlag == 0 || focusViewFlag == 5) {
				finish();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		// 停止倒计时
		time.cancel();
		btnGetVerificationCode.setClickable(true);
		btnGetVerificationCode
				.setText(this
						.getString(R.string.mine_accountsafe_bindphone_getverification));
	}

	/**
	 * 倒计时功能
	 */
	class TimeCount extends CountDownTimer {
		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
		}

		@Override
		public void onFinish() {// 计时完毕时触发
			btnGetVerificationCode.setText(MineAccountManagerActivity.this
					.getString(R.string.account_verificationcode_tip));
			btnGetVerificationCode.setClickable(true);
		}

		@Override
		public void onTick(long millisUntilFinished) {// 计时过程显示
			btnGetVerificationCode.setClickable(false);
			btnGetVerificationCode.setText(millisUntilFinished / 1000 + "s");
		}
	}
}
