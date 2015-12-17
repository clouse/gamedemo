package com.atet.tvmarket.utils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Date;

import com.atet.tvmarket.app.Configuration;

import android.content.Context;


public class RecodUtil {
	public static String FILE_NAME = "SynchronousRecord.txt";
	
	public static boolean ACCOUNT_DEBUG = true;
	
	
	public static void appendRecord(final Context context,final String content)
	{
//		if(Configuration.IS_DEBUG_ENABLE){
//			write(context,content);
//		}
	}
	
	public synchronized static void write(Context context,String content)
	{
		try {
			FileOutputStream fos = context.openFileOutput(FILE_NAME, Context.MODE_APPEND);
			
			PrintStream ps = new PrintStream(fos);
			ps.println(new Date(System.currentTimeMillis())+":"+content + "\n\r");
			ps.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public synchronized static void newLog(Context context,String serviceName,String msg)
	{
//		StringBuilder recordString = new StringBuilder();	
//		recordString.append(DeviceStatisticsUtils.getDateToString(DeviceStatisticsUtils.getTime()));
//		recordString.append("[ " + serviceName + " ] " + msg);
//	
//		appendRecord(context, recordString.toString());
//		
//		recordString = null;
	}
	
	public synchronized static void noInternetLog(Context context,String serviceName)
	{
//		if (StatisticsRecordTestUtils.ACCOUNT_DEBUG == true)
//		{
//			StringBuilder sb = new StringBuilder();
//			sb.append("上传设备信息失败！无有效的网络连接。\r\n");
//			String msg = sb.toString();
//			sb = null;
//			StatisticsRecordTestUtils.newLog(context,serviceName, msg);
//		}
	}
	
	public synchronized static void databaseOverwriteLog(Context context,String serviceName)
	{
//		if (StatisticsRecordTestUtils.ACCOUNT_DEBUG == true)
//		{
//			StringBuilder sb = new StringBuilder();
//			sb.append("本地数据库中记录超过100条，将删除最早的1条记录。。\r\n");
//			String msg = sb.toString();
//			sb = null;
//			StatisticsRecordTestUtils.newLog(context,serviceName, msg);
//		}
	}
}
