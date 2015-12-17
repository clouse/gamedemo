package com.atet.tvmarket.model.dataupdate;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.atet.common.logging.ALog;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

/** 
 * @ClassName: AlarmUtil 
 * @Description: 
 * @author: Liuqin
 * @date 2013-1-15 上午11:40:33 
 *  
 */  
public class AlarmUtil {
	static ALog alog = ALog.getLogger(AlarmUtil.class);

//	/**
//	 * @Title: sleepTime
//	 * @Description: 服务启动定时
//	 * @param context
//	 * @param ms
//	 * @param isRepeat
//	 * @throws
//	 */
//	public static void sleepTime(Context context, long ms,boolean isRepeat) {
//		Intent inent = new Intent(context, FPReceiver.class);
//		inent.setAction(FPReceiver.ACTION_RESTART_SERVICE);
//		PendingIntent sender = PendingIntent.getBroadcast(context, 0, inent, 0);
//		AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//		alarm.cancel(sender);
//		if(!isRepeat){
//			alarm.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + ms, sender);
//		} else {
//			alarm.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+ms, ms, sender);
//		}
//		alog.debug("sleep: " + ms / 60000 + " minutes later will restart the service");
//
//		dspTimeString(ms, "next time to restart service");
//	}
//
//	private static void dspTimeString(long ms, String txt) {
//		Calendar calendar = Calendar.getInstance();
//		calendar.setTimeInMillis(System.currentTimeMillis() + ms);
//		SimpleDateFormat dateformat = new SimpleDateFormat(
//				"[yyyy-MM-dd HH:mm:ss]");
//		String restartTime = dateformat.format(calendar.getTime());
//		alog.debug(txt + ": " + restartTime);
//	}
//	
//	public static void cancelServiceAlarm(Context context){
//		Intent inent = new Intent(context, FPReceiver.class);
//		inent.setAction(FPReceiver.ACTION_RESTART_SERVICE);
//		PendingIntent sender = PendingIntent.getBroadcast(context, 0, inent, 0);
//		AlarmManager alarm = (AlarmManager) context .getSystemService(Context.ALARM_SERVICE);
//		alarm.cancel(sender);
//		alog.debug("cancel service alarm");
//	}
}
