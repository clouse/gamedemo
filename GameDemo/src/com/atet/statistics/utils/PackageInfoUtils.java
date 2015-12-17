package com.atet.statistics.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;


/**
 * 
 * 用于获取程序包信息的辅助类
 * 
 * 
 * @author zhaominglai
 * 
 * */
public class PackageInfoUtils{
	
	
	
	/**
	 * 获取程序的名称，而非包名。例如浏览器的包名为com.android.browser，而名称为浏览器
	 * 
	 * */
	public static String getApplicationLable(Context context,String packageName)
	{
		PackageManager mPM = context.getPackageManager();
		try {
			return mPM.getApplicationLabel(mPM.getApplicationInfo(packageName, PackageManager.GET_META_DATA)).toString();
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "noname";
	}
	
	/**
	 * 获取当前正在运行的Activity的程序包名。
	 * 
	 * */
	@SuppressWarnings("finally")
	public static String getRunningPackageName(Context context)
	{
		ActivityManager mAM = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		
		String pkgName = "noname";
		if (Build.VERSION.SDK_INT < 21){
			
			try {
				pkgName = mAM.getRunningTasks(1).get(0).topActivity.getPackageName();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return pkgName;
		}
		try {
			pkgName = mAM.getRunningAppProcesses().get(0).pkgList[0];
		
		} catch (Exception e) {
			
		}finally{
			return pkgName;
		}

	}

}
