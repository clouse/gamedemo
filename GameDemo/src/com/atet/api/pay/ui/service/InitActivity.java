package com.atet.api.pay.ui.service;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.atet.api.utils.DialogUtil;
import com.atet.api.utils.StringTool;
import com.atet.common.logging.ALog;
import com.atet.tvmarket.app.BaseApplication;
import com.atet.tvmarket.app.Configuration;
import com.atet.tvmarket.app.IPPort;
import com.atet.tvmarket.entity.DeviceInfo;
import com.atet.tvmarket.entity.UserInfo;
import com.atet.tvmarket.entity.pay.LoginResult;
import com.atet.tvmarket.entity.pay.PayResult;
import com.atet.tvmarket.model.DataFetcher;
import com.atet.tvmarket.model.DataHelper;
import com.atet.tvmarket.model.MyJsonPaser;
import com.atet.tvmarket.model.ReqCallback;
import com.atet.tvmarket.model.task.TaskResult;
import com.atet.tvmarket.view.NewToast;
import com.atetpay.LoginInfo;
import com.atetpay.PayInfo;
import com.atetpay.login.LoginActivity_;
import com.atetpay.login.data.LOGIN;
import com.atetpay.pay.PayFlatActivity_;
import com.atetpay.pay.data.PAY;

public class InitActivity extends Activity{
    ALog alog = ALog.getLogger(InitActivity.class);
	// 请求的页面标识，支付页面
	public static final int PAYACTIVITY = 0;
	// 请求的页面标识，登录页面
	public static final int LOGINACTIVITY = 1;
	
    public static final int PAY_SUCCESS = 1001;
    public static final int PAY_FAIL = 1002;
    public static final int PAY_CANCEL = 1003;
    public static final int PAY_HANDLING = 1004;
    public static final int PAY_NETWORK_ERROR = 1005;
    public static final int LOGIN_SUCCESS = 2001;
    public static final int LOGIN_FAIL = 2002;
    public static final int LOGIN_CANCEL = 2003;

	
    private com.atet.tvmarket.entity.PayInfo mPayInfo;
    private com.atet.tvmarket.entity.LoginInfo mLoginInfo=null;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);    	
    	alog.debug("");
    	initDeviceInfo();
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    }
    
    private void handleLoginPay(){
        if(!getTransferLoginInfo()){
//            mPayInfo=getTransferPayInfo();
        	mPayInfo=initTransferPayParams();
        }
        
        if(mLoginInfo!=null){
        	openLoginActivity();
        } else if(mPayInfo!=null){
        	openPayActivity();
        } else {
        	finish();
        }
    }
    
	/**
	 * @description: 获取server id
	 * 
	 * @author: LiuQin
	 * @date: 2015年7月4日 下午7:06:12
	 */
	public void initDeviceInfo() {
		DataHelper.initDeviceInfo(this);
		alog.debug("device info:" + DataHelper.getDeviceInfo().toString());
		if (TextUtils.isEmpty(DataHelper.getDeviceInfo().getServerId())
				|| TextUtils.isEmpty(DataHelper.getDeviceInfo().getChannelId())
				|| TextUtils.isEmpty(DataHelper.getDeviceInfo().getAtetId())
				|| "1".equals(DataHelper.getDeviceInfo().getAtetId())) {
			alog.debug("server id not exist, will request to server");
			ReqCallback<String> reqCallback = new ReqCallback<String>() {
				@Override
				public void onResult(TaskResult<String> taskResult) {
					DialogUtil.getInstanse().dismiss();

					int code = taskResult.getCode();
					alog.info("taskResult code:" + code);
					if (code == TaskResult.OK) {
						alog.info(taskResult.getData());
						handleLoginPay();
					} else {
						DialogUtil.getInstanse().showToast(InitActivity.this, "安全支付初始化失败...", false);
						if(!getTransferLoginInfo()){
							mPayInfo=initTransferPayParams();
						}
						if(mLoginInfo!=null){
							notifyLoginEnd(LOGIN_FAIL, "", 0);
						} else if(mPayInfo!=null){
							notifyPayEnd(PAY_FAIL);
						}
						finish();
					}
				}
			};
			
            DialogUtil.getInstanse().showProgressDialog(this, null, "正在初始化安全支付...", false, null);
			DataFetcher.getServerId(this, reqCallback, true).request(this);
		} else {
			alog.debug("server id exist:" + DataHelper.getDeviceInfo().getServerId());
			handleLoginPay();
		}
	}
    	
	
    /**
     * 获取传送过来的PayInfo支付信息
     * 
     * @return 
     * @throws
     * @author:LiuQin
     */
//    protected com.atet.tvmarket.entity.PayInfo getTransferPayInfo(){
//        return (com.atet.tvmarket.entity.PayInfo)getIntent().getSerializableExtra("PayInfo");
//    }
    
    private com.atet.tvmarket.entity.PayInfo initTransferPayParams(){
    	alog.debug("");
        Intent intent=getIntent();
        if(intent==null){
            return null;
        }
        Bundle bundle=intent.getExtras();
        if(bundle==null){
            return null;
        }
        String params=bundle.getString("PayParams", null);
        int payId=bundle.getInt(AtetPayService.KEY.PayId, 0);
        if(TextUtils.isEmpty(params) || payId<=0){
//            DebugTool.error(TAG, "[startPay] params error", null);
            return null;
        }
        
        String content=StringTool.getContent(params, "content=", "&sign=");
        String sign=StringTool.getContent(params, "&sign=", null);
//        DebugTool.info(TAG, "[initTransferPayParams] content:"+content);
//        DebugTool.info(TAG, "[initTransferPayParams] sign:"+sign);
        if(!Rsa.checkSign(content, sign, PayConfig.PUBLIC_KEY1)){
//            DebugTool.error(TAG, "[initTransferPayParams] sign error", null);
            return null;
        }
        
        com.atet.tvmarket.entity.PayInfo info=MyJsonPaser.fromJson(content, com.atet.tvmarket.entity.PayInfo.class);
        if(info==null){
            return null;
        }
        info.setPayId(payId);
        String appkey=info.getAppkey();
        if(appkey!=null){
            appkey=appkey.replaceAll("\\s", "");
        }
//        DebugTool.info(TAG, "[getTransferPayInfo] appKey:"+appkey);
//        info.setAppkey(EncryptUtils.mencrypt(appkey));
        info.setAppkey(appkey);
//        DebugTool.info(TAG, "[getTransferPayInfo] appKey:"+EncryptUtils.mencrypt(appkey));
        
        return info;
    }
    
    private boolean getTransferLoginInfo(){
        Intent intent=getIntent();
        if(intent==null){
            return false;
        }
        Bundle bundle=intent.getExtras();
        if(bundle==null){
            return false;
        }
        String params=bundle.getString(AtetPayService.KEY.LoginParams, null);
        int payId=bundle.getInt(AtetPayService.KEY.PayId, 0);
        if(TextUtils.isEmpty(params) || payId<=0){
            return false;
        }
        
        if(params!=null){
            mLoginInfo=MyJsonPaser.fromJson(params, com.atet.tvmarket.entity.LoginInfo.class);
        } else {
            mLoginInfo=new com.atet.tvmarket.entity.LoginInfo();
            mLoginInfo.setOrientation(android.content.res.Configuration.ORIENTATION_LANDSCAPE);
        }
        mLoginInfo.setPayId(payId);
        
//        Bundle b=new Bundle();
//        b.putSerializable(Atet.KEY.LoginInfo, loginInfo);
//        Intent in=new Intent(this,AccountLoginActivity.class);
//        in.putExtras(b);
//        startActivity(in);
        
        return true;
    }
    
	private PayInfo getPayInfo() {
		PayInfo info = new PayInfo();
		
		DataHelper.initDeviceInfo(this);
		DeviceInfo deviceInfo = DataHelper.getDeviceInfo();
		info.addParams(PayInfo.DEVICEID, deviceInfo.getServerId());
		info.addParams(PayInfo.ATETID, deviceInfo.getAtetId());
		info.addParams(PayInfo.PRODUCTID, deviceInfo.getDeviceUniqueId());
		info.addParams(PayInfo.DEVICECODE, deviceInfo.getDeviceModel());
		info.addParams(PayInfo.CHANNELID, deviceInfo.getChannelId());
		
		info.addParams(PayInfo.APPID, mPayInfo.getAppid());
		info.addParams(PayInfo.APPKEY, mPayInfo.getAppkey());
		info.addParams(PayInfo.CPPRIVATEINFO, mPayInfo.getCpprivateinfo());
		info.addParams(PayInfo.EXORDERNO, mPayInfo.getExorderno());// 该参数由Cp生成
		info.addParams(PayInfo.NOTIFYURL, mPayInfo.getNotifyurl());
		info.addParams(PayInfo.PRICE, mPayInfo.getPrice());
		info.addParams(PayInfo.QUANTITY, mPayInfo.getQuantity());
		info.addParams(PayInfo.VERSIONCODE, "0");
		info.addParams(PayInfo.WARESID, mPayInfo.getWaresid());
		info.addParams(PayInfo.WARESNAME, mPayInfo.getWaresname());
		info.addParams(PayInfo.GAMEICON, mPayInfo.getPackageName());
		if(IPPort.IS_PAY_RELEASE_IP){
			info.addParams(PayInfo.ENVIRONMENT, PAY.ENVIRONMENT.ONLINE);
		} else {
			info.addParams(PayInfo.ENVIRONMENT, PAY.ENVIRONMENT.IOS_TEST);
		}
		return info;
	}    	
	
	// 登录参数创建
	private LoginInfo getLoginInfo() {
		DataHelper.initDeviceInfo(this);
		DeviceInfo deviceInfo = DataHelper.getDeviceInfo();
		LoginInfo info = new LoginInfo();
		info.addParams(LoginInfo.ATETID, deviceInfo.getAtetId());
		info.addParams(LoginInfo.CHANNELID, deviceInfo.getChannelId());
		info.addParams(LoginInfo.DEVICECODE, deviceInfo.getDeviceModel());
		info.addParams(LoginInfo.DEVICEID, deviceInfo.getDeviceUniqueId());
		if(IPPort.IS_PAY_RELEASE_IP){
			info.addParams(LoginInfo.ENVIRONMENT, PAY.ENVIRONMENT.ONLINE);
		} else {
			info.addParams(LoginInfo.ENVIRONMENT, PAY.ENVIRONMENT.IOS_TEST);
		}
		return info;
	}
    
	public void openLoginActivity() {
		Intent intent = new Intent(this, LoginActivity_.class);
		Bundle args = new Bundle();
		args.putSerializable(LOGIN.LOGININFO, getLoginInfo());
		intent.putExtras(args);
		startActivityForResult(intent, LOGINACTIVITY);
	}
	
	/**
	 * TV端用户支付操作
	 */
	public void openPayActivity() {
		Intent intent = new Intent(this, PayFlatActivity_.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable(PAY.PAYINFO, getPayInfo());
		intent.putExtras(bundle);
		startActivityForResult(intent, PAYACTIVITY);
	}
	
	/**
	 * 接收返回消息支付页面的 requestCode有三种状态：1.PAY.CANCEL为用户取消
	 * 2.PAY.SUCCESS为操作成功3为PAY.FAILD 若支付成功，支付成功的订单号将在Intent中返回，具体获取代码参见下面
	 * 登录页面的resultCode也有三种状态
	 * :1.LOGIN.FAILD为登录失败2.LOGIN.CANCEL为登录取消3.LOGIN.SUCCESS为登录成功
	 * 若登录成功，登录成功的数据会在Intent中返回到当前页面
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		switch (requestCode) {
		case PAYACTIVITY:
			if (resultCode == PAY.CODE.CANCEL) {
//				AndroidHandle.showToast(activity, "用户取消支付");
				notifyPayEnd(PAY_CANCEL);
			} else if (resultCode == PAY.CODE.SUCCESS) {
				// 支付成功以后获取支付成功订单号码
//				AndroidHandle .showToast( activity, "支付成功，订单号为：" + intent.getStringExtra(PAY.ORDER.ORDERNO));
				notifyPayEnd(PAY_SUCCESS);
			} else if (resultCode == PAY.CODE.FAILED) {
//				AndroidHandle.showToast(activity, "支付失败");
				notifyPayEnd(PAY_FAIL);
			}
			break;
		case LOGINACTIVITY:
			if (resultCode == LOGIN.FAILD) {
//				AndroidHandle.showToast(activity, "登录失败");
				notifyLoginEnd(LOGIN_FAIL, "", 0);
			} else if (resultCode == LOGIN.CANCEL) {
				notifyLoginEnd(LOGIN_CANCEL, "", 0);
			} else if (resultCode == LOGIN.SUCCESS) {
				// 登录成功后获取登录成功的用户信息
				String username = intent
						.getStringExtra(LOGIN.USERINFO.USERNAME);
				int userId = intent.getIntExtra(LOGIN.USERINFO.USERID, 0);
//				String icon = intent.getStringExtra(LOGIN.USERINFO.ICON);
//				String coupon = intent.getStringExtra(LOGIN.USERINFO.COUPON);
//				int integral = intent.getIntExtra(LOGIN.USERINFO.INTEGRAL, 0);
//				AndroidHandle.showToast(activity, "登录成功!用户信息为：" + username
//						+ "," + icon + "," + coupon + "," + integral);
				notifyLoginEnd(LOGIN_SUCCESS, username, userId);
			}
			break;
		}
	}
	
    private void notifyLoginEnd(int statusCode, String username, int userId){
//    	initUserInfo();
    	
        LoginResult loginResult=new LoginResult();
        loginResult.setRetcode(statusCode);
        loginResult.setUsername(username);
        loginResult.setUid(userId);
        
        String resultInfo=MyJsonPaser.toJson(loginResult);
        
        Intent intent=new Intent(this,AtetPayService.class);
        if(mLoginInfo!=null){
            intent.putExtra(AtetPayService.KEY.PayId, mLoginInfo.getPayId());
        }
        intent.putExtra(AtetPayService.KEY_LOGIN_RESULT, resultInfo);
        startService(intent);
        finish();
    }
    
    protected void notifyPayEnd(int statusCode){
        if(mPayInfo != null && mPayInfo.getPayId()==0){
            //手机扫描支付
        	finish();
            return;
        }

        PayResult payResult=new PayResult();
        payResult.setResultCode(statusCode);
        
        Intent intent=new Intent(this,AtetPayService.class);
        if(mPayInfo!=null){
            payResult.setAppId(mPayInfo.getAppid());
            payResult.setWaresId(mPayInfo.getWaresid());
            payResult.setExorderno(mPayInfo.getExorderno());
            intent.putExtra(AtetPayService.KEY.PayId, mPayInfo.getPayId());
        }

        String resultInfo=MyJsonPaser.toJson(payResult);
        String result=resultInfo;
        if(!TextUtils.isEmpty(resultInfo)){
            String sign=Rsa.sign(resultInfo, PayConfig.PRIVATE_KEY2);
            result="content="+resultInfo+"&sign="+sign;
        }

        intent.putExtra(AtetPayService.KEY_PAY_RESULT, result);
        startService(intent);
        finish();
    }
    
    public static void showToast(Context context, String msg) {
        if (!TextUtils.isEmpty(msg)) {
            NewToast.makeToast(context, msg, Toast.LENGTH_SHORT).show();
        }
    }

    public static void showToast(Context context, int resId) {
        if (resId > 0) {
            String msg = context.getString(resId);
            showToast(context, msg);
        }
    }
    
	/**
	 * @description: 初始化用户信息
	 * 
	 * @author: LiuQin
	 * @date: 2015年10月9日 下午10:20:00
	 */
	private void initUserInfo() {
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
		DataFetcher.userGetLastLoginedUser(getApplicationContext(), reqCallback, false)
			.request(getApplicationContext());
	}
	
	@Override
	protected void onDestroy() {
    	initUserInfo();
		super.onDestroy();
	}
}
