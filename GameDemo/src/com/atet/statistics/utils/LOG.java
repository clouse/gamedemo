package com.atet.statistics.utils;

import android.util.Log;

/**
 * 日志打印辅助类，自定义了开关。
 * 
 * @author zhaominglai
 * @date 2014-12-25
 * 
 * */
public class LOG {
	public static boolean IS_DEBUG = false;
	
	public static void E(String tag,String msg){
		
		if (IS_DEBUG){
			Log.e(tag,msg);
		}
	}

}
