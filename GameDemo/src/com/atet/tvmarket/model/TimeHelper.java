package com.atet.tvmarket.model;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

import com.atet.tvmarket.model.dataupdate.DataUpdateService;
import com.atet.tvmarket.receiver.BReceiver;

public class TimeHelper {
	private static long serverTimePoint = -1;
	private static long systemElapsedTime = -1;
	
	/**
	 * @description: 设置服务器时间
	 *
	 * @author: LiuQin
	 * @date: 2015年8月27日 下午1:00:57
	 */
	public static void setServerTimePoint(long serverTimePoint){
		synchronized (TimeHelper.class) {
			TimeHelper.serverTimePoint = serverTimePoint;
			TimeHelper.systemElapsedTime = SystemClock.elapsedRealtime();
		}
	}
	
	/**
	 * @description: 是否同步了服务器时间
	 *
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年8月27日 下午1:00:46
	 */
	public static boolean isServerTimeSyn(){
		return TimeHelper.serverTimePoint > 0;
	}
	
	/**
	 * @description: 获取当前时间
	 *
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年7月21日 下午4:41:57
	 */
	public static long getCurTime(){
		if(serverTimePoint > 0){
			return serverTimePoint + (SystemClock.elapsedRealtime() - systemElapsedTime);
		}
		return System.currentTimeMillis();
	}
	
	/**
	 * @description: 获取time（13位）当天过去的毫秒
	 *
	 * @param time
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年7月21日 下午4:24:36
	 */
	public static int getElapseMillisecond(long time){
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time);
		int hour=calendar.get(Calendar.HOUR_OF_DAY);
		int minute=calendar.get(Calendar.MINUTE);
		int second=calendar.get(Calendar.SECOND);
		int millisecond=calendar.get(Calendar.MILLISECOND);
		int elapseMillisecond = (hour*3600 + minute*60 + second) * 1000 +millisecond;
		
		return elapseMillisecond;
	}
	
	/**
	 * @description: 获取time这一天的开始时间，00:00:00,000
	 *
	 * @param time
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年7月21日 下午4:25:49
	 */
	public static long getTodayStartMills(long time){
		return time - getElapseMillisecond(time);
	}
	
	
	/**
	 * @description: 获取time这一天的结束时间，23:59:59,999
	 *
	 * @param time
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年7月21日 下午4:26:34
	 */
	public static long getTodayEndMills(long time){
		return time + (24*3600000) - 1;
	}
	
	/**
	 * @description: 转换可视时间
	 *
	 * @param time
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年7月21日 下午4:27:40
	 */
	public static String getHumanTime(long time){
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time);
		int year=calendar.get(Calendar.YEAR);
		int month=calendar.get(Calendar.MONTH)+1;
		int day=calendar.get(Calendar.DAY_OF_MONTH);
		int hour=calendar.get(Calendar.HOUR_OF_DAY);
		int minute=calendar.get(Calendar.MINUTE);
		int second=calendar.get(Calendar.SECOND);
		int millisecond=calendar.get(Calendar.MILLISECOND);
		
		String humanTime = year + "-"+month+"-"+day+" "+hour+":"+minute+":"+second+","+millisecond; 
		return humanTime;
	}
	
	/**
	 * @description: 设置数据更新服务时间
	 *
	 * @param context
	 * @param triggerAtMillis 
	 * @author: LiuQin
	 */
	public static void starDataUpdateService(Context context, long triggerAtMillis) {
//		String action = context.getPackageName() + DataConfig.ACTION_DATA_UPDATE;
		String action = DataConfig.ACTION_DATA_UPDATE;
		Intent newIntent = new Intent(context, BReceiver.class);
		newIntent.setAction(action);
		PendingIntent sender = PendingIntent.getBroadcast(context, 0, newIntent, 0);
		AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		am.cancel(sender);
		am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime()+triggerAtMillis,
				DataConfig.NomalSpaceTime, sender);
	}
	
	/**
	 * @description: 判断是否今天
	 *
	 * @param time
	 * @return 
	 * @author: LiuQin
	 * @date: 2015年9月21日 上午12:50:50
	 */
	public static boolean isToday(long time){
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(getCurTime());
		int year=calendar.get(Calendar.YEAR);
		int month=calendar.get(Calendar.MONTH);
		int day=calendar.get(Calendar.DAY_OF_MONTH);
		
		calendar.setTimeInMillis(time);
		
		return year == calendar.get(Calendar.YEAR)
				&& month == calendar.get(Calendar.MONTH)
				&& day == calendar.get(Calendar.DAY_OF_MONTH);
	}
}
