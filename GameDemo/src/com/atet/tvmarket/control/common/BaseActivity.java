package com.atet.tvmarket.control.common;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.widget.Toast;

import com.atet.common.logging.ALog;
import com.atet.tvmarket.R;
import com.atet.tvmarket.app.BaseApplication;
import com.atet.tvmarket.common.cache.ImageFetcher;
import com.atet.tvmarket.utils.GamepadTool;
import com.atet.tvmarket.utils.IdUtils;
import com.atet.tvmarket.utils.KeyConversionUtils;
import com.atet.tvmarket.utils.ScreenShot;
import com.atet.tvmarket.utils.UIUtils;
import com.atet.tvmarket.view.BaseImageView;
import com.atet.tvmarket.view.CloseAcceTextView;
import com.atet.tvmarket.view.blur.BlurringView;

/**
 * @description: 所有Activity的基类
 * 
 * @author: LiuQin
 * @date: 2015年5月27日 上午10:00:12
 */
public class BaseActivity extends FragmentActivity {
	ALog alog = ALog.getLogger(BaseActivity.class);
	public static final String CONNECTIVITY_CHANGE_ACTION ="android.net.conn.CONNECTIVITY_CHANGE";
	
	public ImageFetcher mImageFetcher;
	private IntentFilter wifiIntentFilter;
	private IntentFilter timeIntentFilter;
	private int wifi_level;
	private BaseImageView home_wifi;
	protected BaseImageView home_tag;
	protected CloseAcceTextView currentTime;
	private CloseAcceTextView home_user_name;
	private CloseAcceTextView home_integral;
	protected BaseImageView atet_icon,user_icon;
	private boolean isBlack;
	private ConnectivityManager connManager;
	//private BaseImageView home_line;
	protected TouchListener touchListener = new TouchListener();
	protected FocusChangeListener focusChangeListener = new FocusChangeListener();
	private SimpleDateFormat formatters;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		connManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		mImageFetcher = new ImageFetcher();
		wifiIntentFilter = new IntentFilter();
		wifiIntentFilter.addAction(WifiManager.RSSI_CHANGED_ACTION);
		wifiIntentFilter.addAction(CONNECTIVITY_CHANGE_ACTION);
		timeIntentFilter = new IntentFilter();
		timeIntentFilter.addAction(Intent.ACTION_TIME_TICK);
		formatters = new SimpleDateFormat("HH:mm");
	}

	public ImageFetcher getmImageFetcher() {
		return mImageFetcher;
	}


	/**
	 * 监听wifi强度的变化
	 */
	private BroadcastReceiver wifiReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			alog.info("网络发生了变化");
			wifi_level = Math.abs(((WifiManager) getSystemService(WIFI_SERVICE)).getConnectionInfo().getRssi());
			setBgColor();
		}

	};
	
	/**
	 * 监听事件时间的变化
	 */
	private BroadcastReceiver timeReceiver = new BroadcastReceiver(){

		@Override
		public void onReceive(Context context, Intent intent) {
			if (currentTime != null) {
				currentTime.setText(formatters.format(new Date(System.currentTimeMillis())));
			}
		}
	};

	private void setBgColor() {
		// 检查是否有有线连接，如果有有线连接，显示有线图标，如果没有，显示wifi图标
		NetworkInfo[] networkInfo = connManager.getAllNetworkInfo();
		if (networkInfo != null && home_wifi != null) {
			for (int i = 0; i < networkInfo.length; i++) {
				if (networkInfo[i].isAvailable()&& networkInfo[i].isConnected()) {
					
					
					if (networkInfo[i].getType() != ConnectivityManager.TYPE_WIFI) {
						if (isBlack) {
							home_wifi.setImageResource(R.drawable.wired_network_icon);
						} else {
							home_wifi.setImageResource(R.drawable.wired_network_icon);
						}
						return;
					}
					
				} else {
					home_wifi.setImageResource(R.drawable.wifi_white0);
				}
			}
		}

		if (home_wifi != null) {
			if (isBlack) {
				if (wifi_level <= 25) {
					home_wifi.setImageResource(R.drawable.wifi_white4);
				} else if (wifi_level > 25 && wifi_level <= 50) {
					home_wifi.setImageResource(R.drawable.wifi_white3);
				} else if (wifi_level > 50 && wifi_level <= 75) {
					home_wifi.setImageResource(R.drawable.wifi_white2);
				} else if (wifi_level > 75 && wifi_level <= 100) {
					home_wifi.setImageResource(R.drawable.wifi_white1);
				} else {
					home_wifi.setImageResource(R.drawable.wifi_white0);
				}
			} else {
				if (wifi_level <= 25) {
					home_wifi.setImageResource(R.drawable.wifi_white4);
				} else if (wifi_level > 25 && wifi_level <= 50) {
					home_wifi.setImageResource(R.drawable.wifi_white3);
				} else if (wifi_level > 50 && wifi_level <= 75) {
					home_wifi.setImageResource(R.drawable.wifi_white2);
				} else if (wifi_level > 75 && wifi_level <= 100) {
					home_wifi.setImageResource(R.drawable.wifi_white1);
				} else {
					home_wifi.setImageResource(R.drawable.wifi_white0);
				}
			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		registerReceiver(wifiReceiver, wifiIntentFilter);
		registerReceiver(timeReceiver, timeIntentFilter);
		//currentTime.setText(formatters.format(new Date(System.currentTimeMillis())));
	}

	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(wifiReceiver);
		unregisterReceiver(timeReceiver);
	}
	
	
	@Override
	protected void onRestart() {
		super.onRestart();
		if(currentTime != null){
			currentTime.setText(formatters.format(new Date(System.currentTimeMillis())));
		}
	}

	/**
	 * 
	 * @description: 设置低端设备的title
	 * 
	 * @throws: 
	 * @author: LiJie
	 * @date: 2015年8月21日 下午2:54:15
	 */
	public void setLowDevicesTitle(){
		home_user_name.setVisibility(View.GONE);
		home_integral.setVisibility(View.GONE);
		user_icon.setVisibility(View.GONE);
		
	}

	public void setBlackTitle(boolean isBlack) {
		this.isBlack = isBlack;
		home_wifi = (BaseImageView) findViewById(R.id.home_wifi);
		currentTime = (CloseAcceTextView) findViewById(R.id.home_current_time);
		home_user_name = (CloseAcceTextView) findViewById(R.id.home_user_name);
		home_integral = (CloseAcceTextView) findViewById(R.id.home_integral);
		home_tag = (BaseImageView) findViewById(R.id.home_atet_tag);
		user_icon = (BaseImageView)findViewById(R.id.home_user_icon);
		//home_line = (BaseImageView) findViewById(R.id.home_line);

		if (isBlack) {
			currentTime.setTextColor(UIUtils.getColor(R.color.home_title_gray));
			home_user_name.setTextColor(UIUtils.getColor(R.color.home_title_gray));
			home_integral.setTextColor(UIUtils.getColor(R.color.home_title_gray));
			//home_line.setBackgroundResource(R.drawable.black_line);
			mImageFetcher.loadLocalImage(R.drawable.atet, home_tag, R.drawable.atet);
		}
		else {
			currentTime.setTextColor(UIUtils.getColor(R.color.home_title_gray));
			home_user_name.setTextColor(UIUtils.getColor(R.color.home_title_gray));
			home_integral.setTextColor(UIUtils.getColor(R.color.home_title_gray));
			//home_line.setBackgroundResource(R.drawable.white_line);
			mImageFetcher.loadLocalImage(R.drawable.atet_white, home_tag, R.drawable.atet_white);
		}
		currentTime.setText(formatters.format(new Date(System.currentTimeMillis())));
		setBgColor();
	}

	public void setTitleData(){
		if(BaseApplication.userInfo!=null){
			home_user_name.setText(BaseApplication.userInfo.getUserName()+"");
			home_integral.setText("积分 : "+BaseApplication.userInfo.getIntegral());
			if(BaseApplication.userInfo.getIcon()==null)
				mImageFetcher.loadImage("", user_icon, R.drawable.user_icon);
			else
				mImageFetcher.loadImage(BaseApplication.userInfo.getIcon(), user_icon, R.drawable.user_icon);
		}
		else{
			home_user_name.setText("未登录");
			home_integral.setText("积分 : "+0);
		}
	}
	
	/**
	 * 
	 * @description: 设置活动详情的title
	 *
	 * @param itemView
	 * @param isBlack 
	 * @throws: 
	 * @author: LiJie
	 * @date: 2015年8月21日 下午2:53:39
	 */
	public void setBlackTitle2(View itemView, boolean isBlack) {
		this.isBlack = isBlack;
		home_wifi = (BaseImageView) itemView.findViewById(R.id.home_wifi);
		currentTime= (CloseAcceTextView) itemView
				.findViewById(R.id.home_current_time);
		home_user_name = (CloseAcceTextView) itemView
				.findViewById(R.id.home_user_name);
		home_integral = (CloseAcceTextView) itemView
				.findViewById(R.id.home_integral);
		home_tag = (BaseImageView) itemView.findViewById(R.id.home_atet_tag);
		user_icon = (BaseImageView)itemView.findViewById(R.id.home_user_icon);
		//home_line = (BaseImageView) itemView.findViewById(R.id.home_line);

		if (isBlack) {
			currentTime
					.setTextColor(UIUtils.getColor(R.color.home_title_gray));
			home_user_name.setTextColor(UIUtils
					.getColor(R.color.home_title_gray));
			home_integral.setTextColor(UIUtils
					.getColor(R.color.home_title_gray));
			//home_line.setBackgroundResource(R.drawable.black_line);
			mImageFetcher.loadLocalImage(R.drawable.atet, home_tag, R.drawable.atet);
		}
		currentTime.setText(formatters.format(new Date(System.currentTimeMillis())));
		setBgColor();
	}

	public class TouchListener implements OnTouchListener {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			System.out.println("onTouch...");
			if (event.getAction() == MotionEvent.ACTION_UP) {
				OnFocusChangeListener listener = v.getOnFocusChangeListener();
				if (listener != null && listener instanceof FocusChangeListener) {
					((FocusChangeListener) listener).setView(v);
				}
			}
			return false;
		}
	}

	public class FocusChangeListener implements OnFocusChangeListener {

		private View view;

		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			if (hasFocus) {
				if (IdUtils.isIdHandlelinkChoice(v)) {

				} else if (IdUtils.isIdChildlock(v)) {

				} else if (IdUtils.isIdChildlockChoice(v)) {

				} else {
                     
					v.setScaleX(1.0f);
					v.setScaleY(1.0f);
					v.bringToFront();
				}
				System.out.println("[onFocusChange]" + v.toString());
				if (v.isInTouchMode() && v == view) {
					v.performClick();
				} else {
					view = null;
				}
			} else {
				if (IdUtils.isIdHandlelinkChoice(v)) {

				} else if (IdUtils.isIdChildlock(v)) {

				} else if (IdUtils.isIdChildlockChoice(v)) {

				} else {
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


	@Override
	public boolean dispatchKeyEvent(KeyEvent event){

		KeyEvent keyEvent;

		int keyCode = event.getKeyCode();

		if (KeyConversionUtils.isConversionEnter(keyCode)) {
			// 确定键的转换
			keyEvent = KeyConversionUtils.conversionEnter(event);
		} else if (KeyConversionUtils.isConversionBack(keyCode)) {
			// 返回键的转换
			keyEvent = KeyConversionUtils.conversionBack(event);
		} else if(GamepadTool.handleButtonCloud(getApplicationContext(), event)) {
			// 处理了手柄云键
			return true;
		} else if(GamepadTool.handleButtonGame(getApplicationContext(), event)) {
			// 处理了手柄Game键
			return true;
		} else {
			keyEvent = event;
		}

		return KeyConversionUtils.filterAppendKeyCode(keyCode, super.dispatchKeyEvent(keyEvent));
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_A
				|| keyCode == KeyEvent.KEYCODE_BUTTON_A
				|| keyCode == KeyEvent.KEYCODE_ENTER) {
			MotionEvent e = MotionEvent.obtain(0, 0, MotionEvent.ACTION_DOWN,
					0, 0, 0);
			View view = getCurrentFocus();
			if (view != null) {
				view.onTouchEvent(e);
			}
		}
		return super.onKeyDown(keyCode, event);
	}
}
