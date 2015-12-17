package com.atet.tvmarket.utils;



import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Handler;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.view.View;

import com.atet.tvmarket.app.BaseApplication;
import com.atet.tvmarket.view.BaseImageView;
import com.atet.tvmarket.view.CloseAcceTextView;
import com.atet.tvmarket.view.NewToast;



/**
 * @description: 工具類
 *
 * @author: LiJie
 * @date: 2015年5月28日 上午10:50:21 
 */
public class UIUtils
{
	/**
	 * 上下文的获取
	 * 
	 * @return
	 */
	public static Context getContext()
	{
		return BaseApplication.getContext();
	}

	/**
	 * 获取资源
	 * 
	 * @return
	 */
	public static Resources getResources()
	{
		return getContext().getResources();
	}
	
	public static Handler getMainHandler(){
		return BaseApplication.getMainHandler();
	}
	/**
	 * 
	 * @param dip
	 * @return
	 */
	public static int dip2px(int dip)
	{
		// 公式 1: px = dp * (dpi / 160)
		// 公式 2: dp = px / denistity;
		DisplayMetrics metrics = getResources().getDisplayMetrics();
		float density = metrics.density;
		// metrics.densityDpi
		return (int) (dip * density + 0.5f);
	}

	public static int px2dip(int px)
	{
		// 公式 1: px = dp * (dpi / 160)
		// 公式 2: dp = px / denistity;
		DisplayMetrics metrics = getResources().getDisplayMetrics();
		float density = metrics.density;
		// metrics.densityDpi
		return (int) (px / density + 0.5f);
	}
	
	public static long getMainThreadId()
	{
		return BaseApplication.getMainThreasdId();
	}
	
	public static void runOnUiThread(Runnable task)
	{
		long currentThreadId = android.os.Process.myTid();
		long mainThreadId = getMainThreadId();

		if (currentThreadId == mainThreadId)
		{
			// 如果在主线程中执行
			task.run();
		}
		else
		{
			// 需要转的主线程执行
			getMainHandler().post(task);
		}
	}

	public static String getString(int resId)
	{
		return getResources().getString(resId);
	}
	
	public static String getString(int resId,Object... formatArgs)
	{
		return getResources().getString(resId,formatArgs);
	}

	public static String getPackageName()
	{
		return getContext().getPackageName();
	}

	public static String[] getStringArray(int resId)
	{
		return getResources().getStringArray(resId);
	}

	public static int getColor(int resId)
	{
		return getResources().getColor(resId);
	}
	
	public static int getDimens(int size){
		return (int) ScaleViewUtils.resetTextSize(UIUtils.getResources().getDimensionPixelSize(size));
	}
	
	public static void startActivity(Intent intent){
		getContext().startActivity(intent);
	}
	
	public static void postDelayed(Runnable task, long delay){
		getMainHandler().postDelayed(task, delay);
	}
	
	public static void removeAllCallBack(Runnable task){
		getMainHandler().removeCallbacks(task);
	}
	
	/*public static void setTextType(TextView tv){
		tv.setTypeface(BaseApplication.getTypeFace());
	}*/
	
	public static String changeTimeStyle(long time){
		String sysTimeStr = (String) DateFormat.format("yyyy-MM-dd", time);
		return sysTimeStr;
	}
	
	public static void setOnline(CloseAcceTextView tv , Integer online){
		tv.setText(online + "人同时在线");
	}
	
	public static void isReleased(BaseImageView baseImageView, Integer tag){
		if(tag == 0){
			baseImageView.setVisibility(View.INVISIBLE);
		}else{
			baseImageView.setVisibility(View.VISIBLE);
		}
	}
	
	public static void makeText(final Context context, final String msg){
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				NewToast.makeToast(context,msg, 0).show();
			}
		});

	}
}
