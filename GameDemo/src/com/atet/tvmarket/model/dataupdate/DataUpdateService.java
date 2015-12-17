package com.atet.tvmarket.model.dataupdate;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.atet.common.logging.ALog;
import com.atet.tvmarket.utils.NetUtil;

/**
 * @description: 数据更新服务
 *
 * @author: LiuQin
 * @date: 2015年8月6日 下午2:57:39 
 */
public class DataUpdateService extends Service{
	private volatile boolean isExecuting = false;
	ALog alog = ALog.getLogger(DataUpdateService.class);
	private DataUpdateInstance mDataUpdateInstance = null;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	}
	
	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
		doWork();
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		exitProcess();
	}
	
	private void doWork(){
		if(newDataUpdateInstance()){
			if (mDataUpdateInstance != null) {
				mDataUpdateInstance.doUpdate();
			}
		} else {
			alog.warn("update service is running");
		}
	}
	
	private boolean newDataUpdateInstance(){
		synchronized (DataUpdateService.class) {
			boolean result = false;
			if(mDataUpdateInstance == null){
				mDataUpdateInstance = new DataUpdateInstance(this, mOnCompleteCallback);
				result =true;
			}
			return result;
		}
	}
	
	private void resetState(){
		alog.debug("");
		synchronized (DataUpdateService.class) {
			mDataUpdateInstance = null;
		}
		stopSelf();
	}
	
	private OnCompleteCallback mOnCompleteCallback = new OnCompleteCallback() {
		@Override
		public void onComplete() {
			resetState();
		}
	};
	
	public static interface OnCompleteCallback{
		public void onComplete();
	}
	
	/**
	 * @description: 启动数据更新服务
	 *
	 * @param context 
	 * @author: LiuQin
	 * @date: 2015年9月21日 上午3:55:47
	 */
	public static boolean start(Context context){
		if(NetUtil.isNetworkAvailable(context, true)){
			Intent startIntent = new Intent(context, DataUpdateService.class);
			startIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startService(startIntent);
			return true;
		}
		return false;
	}
	
	public static void exitProcess(){
		android.os.Process.killProcess(android.os.Process.myPid());
	}
}
