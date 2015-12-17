package com.atet.tvmarket.control.mygame;

import com.atet.tvmarket.app.BaseApplication;
import com.atet.tvmarket.control.mygame.utils.ImplUtils;
import com.atet.tvmarket.model.net.http.download.InterceptInputApp;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

public class UpdateGameService extends Service{
	private Handler mHandler;
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		// 屏蔽输入法
		InterceptInputApp.downloadInterceptInputApp(getApplicationContext());
		mHandler = new Handler() {
			public void handleMessage(Message msg) {
				if (msg.what == 0x12345) {
					new Thread(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							ImplUtils.updateGame();
						}
					}).start();
					Intent intent = new Intent(BaseApplication.getContext(),
							UpdateGameService.class);
					stopService(intent);
				}
			}
		};
		
		// 处理预装游戏
		ImplUtils.handlePreInstallgames(BaseApplication.getContext(),
				mHandler);
        new Thread(new Runnable() {
			
			@Override
			public void run() {
				// 处理检查本地目录Apk
				ImplUtils.checkApks();
			}
		}).start();
		return 0;
	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
