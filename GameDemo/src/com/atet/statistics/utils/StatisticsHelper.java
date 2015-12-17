package com.atet.statistics.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.atet.statistics.bases.StatisticsConstant;
import com.atet.statistics.services.GameCollectService;
import com.atet.statistics.services.PlatFormOnlineService;
import com.atet.statistics.services.StatisticsGameOnlineService;
import com.atet.statistics.services.UpdateHardwareService;
import com.atet.statistics.services.UploadGameOnlineService;
import com.atet.statistics.services.UploadPlatFormTimeInfoService;
import com.atet.tvmarket.control.mygame.task.TaskManager;
import com.atet.tvmarket.utils.DebugTool;

public class StatisticsHelper {
	
	private static StatisticsHelper mStatisticsHelper;
	private Context mContext;
	
	private String TAG = "StatisticsHelper";
	private static final String IS_UPDATED = "isUpdated";//是否已经上传过信息
	protected TaskManager taskManager;
	
	public static boolean IS_RUNNING;
	
	//统计服务的时间间隔
	public static long STATISTICS_INTERVAL = 30 * 1000;
	
	public static boolean is_debug = false;
	
	
		
		//　测试上传时间间隔
//		public static long STATISTICS_UPLOAD_INTERVAL = 10 * 60 * 1000;
	
		//上传服务的时间间隔
		public static long STATISTICS_UPLOAD_INTERVAL = 4 * 60 * 60 * 1000;
		
	
	
	public static long STATISTICS_UPLOAD_HARDWARE_INTERVAL = 5 * 60 * 1000;
	
	private AlarmManager am;
	
	PendingIntent pi_uploadPlatFormInfo;
	PendingIntent pi_updatePlatFormTime;
	PendingIntent pi_gameonline;
	PendingIntent pi_updategameonline;
	PendingIntent pi_gamecollect;
	PendingIntent pi_updateHardware;
	PendingIntent pi_uploadGameOnline;

	private int REQUEST_CODE_INIT = 0;//初始化服务的代号
	private int REQUEST_CODE_INIT_UPTIME = 1;//初始化更新时间服务的代号
	private int REQUEST_CODE_UPLOAD_GAMETIME = 2;//上传游戏在线时长的服务代号
	private int REQUEST_CODE_UPDATE_GAMETIME = 3;//游戏在线时长定时更新时间的服务代号
	private int REQUEST_CODE_GAMECOLLECT = 4;//游戏采集服务
	private int REQUEST_CODE_UPDATE_HARDWARE = 5;//装机量上传服务
		
	private long SERVICE_INIT_TRIGGER_TIME = 0;//初始化接口服务第一次运行的时间，从开机启动后算
	private long SERVICE_UPDATE_TIME_TRIGGER_TIME = 0;//初始化接口服务中定时更新设备时间节点的服务第一次运行的时间，从开机启动后算
	private long SERVICE_GAMETIME_TRIGGER_TIME = 4 * 6000;//游戏在线时长统计接口服务第一次运行的时间，从开机启动后算
	private long SERVICE_COLLECT_TRIGGER_TIME = 0;//游戏采集第一次运行的时间，从开机启动后算
	
	
	private StatisticsHelper(Context context)
	{
		this.mContext = context;
	}
	
	public static StatisticsHelper getInstance(Context context)
	{
		if (mStatisticsHelper == null)
			mStatisticsHelper = new StatisticsHelper(context);
		
		
		return mStatisticsHelper;
	}
	

	/**
	 * 启动各种统计服务
	 * 
	 * 
	 * 
	 * */
	public void startServices(){
		
		if(!StatisticsConstant.OPEN_STATISTICS){
			return;
		}
		
		IS_RUNNING = true;
		
		if (am == null)
			am = (AlarmManager) mContext.getSystemService(Service.ALARM_SERVICE);
		
		//游戏平台运行时长信息上传服务
		Intent intent_uploadPlatForm = new Intent(mContext,UploadPlatFormTimeInfoService.class);
		pi_uploadPlatFormInfo = PendingIntent.getService(mContext, REQUEST_CODE_INIT, intent_uploadPlatForm, 0);
		
		/**
		 * 游戏平台时间信息检测服务，每30秒检测一次
		 */
		Intent intent_updatePlatFormTime = new Intent(mContext,PlatFormOnlineService.class);
	    pi_updatePlatFormTime = PendingIntent.getService(mContext, REQUEST_CODE_INIT_UPTIME, intent_updatePlatFormTime, 0);
	    
	    /**
	     * 向服务器上传游戏在线时长
	     * */
	    Intent intent_uploadGameOnline = new Intent(mContext,UploadGameOnlineService.class);
	    pi_uploadGameOnline = PendingIntent.getService(mContext, REQUEST_CODE_UPLOAD_GAMETIME, intent_uploadGameOnline, 0);
		
		/**
		 * 游戏信息采集服务
		 * */
		Intent intent_gamecollect = new Intent(mContext,GameCollectService.class);
		pi_gamecollect = PendingIntent.getService(mContext, REQUEST_CODE_GAMECOLLECT, intent_gamecollect, 0);
		
		/**
		 * 统计游戏的在线时长服务，每30秒运行一次
		 * */
		Intent intent_updategameonline = new Intent(mContext,StatisticsGameOnlineService.class);
		pi_updategameonline = PendingIntent.getService(mContext, REQUEST_CODE_UPDATE_GAMETIME, intent_updategameonline, 0);
		
		if (!DeviceStatisticsUtils.isHadUpdateHardWareInfo(mContext))
		{
			
			Intent intent_hardware = new Intent(mContext,UpdateHardwareService.class);
			pi_updateHardware = PendingIntent.getService(mContext, REQUEST_CODE_UPDATE_HARDWARE, intent_hardware, 0);
			
			am.setRepeating(AlarmManager.RTC,System.currentTimeMillis()+STATISTICS_INTERVAL, STATISTICS_UPLOAD_HARDWARE_INTERVAL, pi_updateHardware);
		}
		
		//向AlarmManager注册游戏平台在线时长上传服务。
		am.setRepeating(AlarmManager.RTC,System.currentTimeMillis()+STATISTICS_INTERVAL, STATISTICS_UPLOAD_INTERVAL, pi_uploadPlatFormInfo);	
		//向AlarmManager注册游戏平台在线时长统计服务。
		am.setRepeating(AlarmManager.RTC,System.currentTimeMillis()+STATISTICS_INTERVAL, STATISTICS_INTERVAL, pi_updatePlatFormTime);
		
		//向AlarmManager注册游戏在线时长统计服务
		am.setRepeating(AlarmManager.RTC,System.currentTimeMillis()+STATISTICS_INTERVAL, STATISTICS_INTERVAL, pi_updategameonline);
		//向AlarmManager注册游戏在线时长上传服务。
		am.setRepeating(AlarmManager.RTC,System.currentTimeMillis()+STATISTICS_INTERVAL, STATISTICS_UPLOAD_INTERVAL, pi_uploadGameOnline);
		
		//向AlarmManger注册上传游戏采集接口服务
		am.setRepeating(AlarmManager.RTC,System.currentTimeMillis()+STATISTICS_INTERVAL, STATISTICS_UPLOAD_INTERVAL, pi_gamecollect);
		
	}
	
	
	public void stopServices()
	{	
		IS_RUNNING = false;
		
		if (am != null)
		{
//			am.cancel(pi_uploadPlatFormInfo);
			//am.cancel(pi_gameonline);
			am.cancel(pi_updatePlatFormTime);
			//am.cancel(pi_gamecollect);
			//am.cancel(pi_updategameonline);
			DebugTool.info(TAG,"取消一些定时服务。");
		}
	}
	
	/**
	 * 启动开机运行的服务
	 * 
	 * */
	public void startBootServices()
	{
		bootInstalledService();
		
		bootGameStatisticsServices();
		
	}
	
	/**
	 * 启动需要开机就运行的服务。
	 * 
	 * 1、游戏在线时长统计
	 * 2、游戏数据采集接口
	 * 
	 * 游戏平台时长统计不需要开机就启动
	 * 
	 * */
	public void bootGameStatisticsServices()
	{
		if (am == null){
			am = (AlarmManager) mContext.getSystemService(Service.ALARM_SERVICE);
		}
		
		//add by zml 198~201 2015-1-29 15:13 添加了开机上传平台运行时长
		//游戏平台运行时长信息上传服务
		Intent intent_uploadPlatForm = new Intent(mContext,UploadPlatFormTimeInfoService.class);
		pi_uploadPlatFormInfo = PendingIntent.getService(mContext, REQUEST_CODE_INIT, intent_uploadPlatForm, 0);
		
		 /**
	     * 向服务器上传游戏在线时长
	     * */
	    Intent intent_uploadGameOnline = new Intent(mContext,UploadGameOnlineService.class);
	    pi_uploadGameOnline = PendingIntent.getService(mContext, REQUEST_CODE_UPLOAD_GAMETIME, intent_uploadGameOnline, 0);
		
		/**
		 * 游戏信息采集服务
		 * */
		Intent intent_gamecollect = new Intent(mContext,GameCollectService.class);
		pi_gamecollect = PendingIntent.getService(mContext, REQUEST_CODE_GAMECOLLECT, intent_gamecollect, 0);
		
		/**
		 * 统计游戏的在线时长服务，每30秒运行一次
		 * */
		Intent intent_updategameonline = new Intent(mContext,StatisticsGameOnlineService.class);
		pi_updategameonline = PendingIntent.getService(mContext, REQUEST_CODE_UPDATE_GAMETIME, intent_updategameonline, 0);
		
		/*if (!DeviceStatisticsUtils.isHadUpdateHardWareInfo(mContext))
		{
			
			Intent intent_hardware = new Intent(mContext,UpdateHardwareService.class);
			pi_updateHardware = PendingIntent.getService(mContext, REQUEST_CODE_UPDATE_HARDWARE, intent_hardware, 0);
			
			am.setRepeating(AlarmManager.ELAPSED_REALTIME, SERVICE_INIT_TRIGGER_TIME, SERVICE_UPDATE_HARDWARE_INTERVAL, pi_updateHardware);
		}*/
		
		//向AlarmManager注册游戏平台在线时长上传服务。
		am.setRepeating(AlarmManager.RTC,System.currentTimeMillis()+STATISTICS_INTERVAL, STATISTICS_UPLOAD_INTERVAL, pi_uploadPlatFormInfo);
		
		//向AlarmManager注册游戏在线时长统计服务
		am.setRepeating(AlarmManager.ELAPSED_REALTIME, SERVICE_GAMETIME_TRIGGER_TIME, STATISTICS_INTERVAL, pi_updategameonline);
		//向AlarmManager注册游戏在线时长上传服务。
		am.setRepeating(AlarmManager.ELAPSED_REALTIME, SERVICE_INIT_TRIGGER_TIME, STATISTICS_UPLOAD_INTERVAL, pi_uploadGameOnline);
		
		//向AlarmManger注册上传游戏采集接口服务
		am.setRepeating(AlarmManager.ELAPSED_REALTIME, SERVICE_COLLECT_TRIGGER_TIME, STATISTICS_UPLOAD_INTERVAL, pi_gamecollect);
			
	}
	
	
	/**
	 * 启动统计装机量服务,也包含获取atetid
	 * */
	public void bootInstalledService()
	{
		SharedPreferences bootSP = mContext.getSharedPreferences("BOOT_INFO", mContext.MODE_PRIVATE);
		boolean isUpdate = bootSP.getBoolean(IS_UPDATED, false);
		boolean hasAtetId = bootSP.getBoolean("hasAtetId", false);
		
		
		//如果已经上传过，就不需要再上传了。
		if (isUpdate && hasAtetId)
			return;
		
//		Intent startIntent = new Intent(mContext, UpdateHardwareService.class);
//		mContext.startService(startIntent);
		Intent intent_hardware = new Intent(mContext,UpdateHardwareService.class);
		pi_updateHardware = PendingIntent.getService(mContext, REQUEST_CODE_UPDATE_HARDWARE, intent_hardware, 0);
		
		if (am == null){
			am = (AlarmManager) mContext.getSystemService(Service.ALARM_SERVICE);
		}
		am.setRepeating(AlarmManager.ELAPSED_REALTIME, SERVICE_INIT_TRIGGER_TIME, STATISTICS_UPLOAD_HARDWARE_INTERVAL, pi_updateHardware);
		
	}

}
