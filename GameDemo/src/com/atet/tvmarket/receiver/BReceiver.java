package com.atet.tvmarket.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;

import com.atet.common.logging.ALog;
import com.atet.statistics.bases.StatisticsConstant;
import com.atet.statistics.utils.StatisticsHelper;
import com.atet.tvmarket.app.BaseApplication;
import com.atet.tvmarket.entity.AppConfig;
import com.atet.tvmarket.model.DataConfig;
import com.atet.tvmarket.model.DataConfig.DeviceCapability;
import com.atet.tvmarket.model.DataFetcher;
import com.atet.tvmarket.model.DataHelper;
import com.atet.tvmarket.model.dataupdate.DataUpdateService;

public class BReceiver extends BroadcastReceiver{
	ALog alog = ALog.getLogger(BReceiver.class);
	
	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
//		String targetAction = context.getPackageName() + DataConfig.ACTION_DATA_UPDATE;
		String targetAction = DataConfig.ACTION_DATA_UPDATE;
		alog.debug("action:"+action);
		if(targetAction.equals(action)){
			if(!DataUpdateService.start(context)){
				DataUpdateService.exitProcess();
			}
		} else if (action.equals("android.net.conn.CONNECTIVITY_CHANGE")) {
			if(isNetworkAvailable(context)){
				BaseApplication.getMainHandler().postDelayed(new Runnable() {
					@Override
					public void run() {
						Context context = BaseApplication.getContext();
						if(!DataUpdateService.start(context)){
							DataUpdateService.exitProcess();
						}
					}
				}, 10000);
			} else {
				DataUpdateService.exitProcess();
			}
		} else if (action.equals(Intent.ACTION_BOOT_COMPLETED)) {
			if(!TextUtils.isEmpty(DataHelper.getDeviceInfo().getServerId())){
				AppConfig appConfig = DataFetcher.getAppConfig();
				if(appConfig.isStatisticsOnBoot()){
					//启动需要开机就运行的服务
					if(StatisticsConstant.OPEN_STATISTICS){
						alog.debug("start statistics service");
						StatisticsHelper.getInstance(context).startBootServices();
					}
				} else {
					alog.debug("low mem device,not start statistics service");
				}
			}
			
			if(isNetworkAvailable(context)){
				BaseApplication.getMainHandler().postDelayed(new Runnable() {
					@Override
					public void run() {
						Context context = BaseApplication.getContext();
						if(!DataUpdateService.start(context)){
							DataUpdateService.exitProcess();
						}
					}
				}, 10000);
			} else {
				DataUpdateService.exitProcess();
			}
		}
	}
	
	public static boolean isNetworkAvailable(Context context) {
		try {
			ConnectivityManager connec = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connec == null)
				return false;

			NetworkInfo[] allinfo = connec.getAllNetworkInfo();

			if (allinfo != null) {
				for (int i = 0; i < allinfo.length; i++) {
					if (allinfo[i].isAvailable() && allinfo[i].isConnected()) {
						return true;
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

}
