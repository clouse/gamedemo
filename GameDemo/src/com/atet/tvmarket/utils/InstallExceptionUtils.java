package com.atet.tvmarket.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

public class InstallExceptionUtils {
	public static final String TAG = "InstallExceptionUtils";
	
	public static void collectSelfInstallInfoAsync(Context ctx, final int exceptionCode) {
		final Context appContext = ctx.getApplicationContext();
		new Thread(new Runnable() {
			@Override
			public void run() {
				collectSelfInstallInfo(appContext, exceptionCode);;
			}
		}).start();
	}
	
	public static void collectGameInstallFailedInfoAsync(Context ctx, final String packageName,
			final String appName, final String versionName, final int exceptionCode) {
		final Context appContext = ctx.getApplicationContext();
		new Thread(new Runnable() {
			@Override
			public void run() {
				collectGameInstallFailedInfo(appContext, packageName, appName, versionName, exceptionCode);
			}
		}).start();
	}

	/**
	 * @description: 收集程序自身升级出错信息
	 *
	 * @param ctx
	 * @param exceptionCode 
	 * @author: LiuQin
	 * @date: 2015年10月9日 下午11:55:01
	 */
	public static void collectSelfInstallInfo(Context ctx, int exceptionCode) {
		String exceptionName = installFailureToString(exceptionCode);
		// 用来存储设备信息和异常信息
		Map<String, String> infos = new LinkedHashMap<String, String>();
		try {
			PackageManager pm = ctx.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(),
					PackageManager.GET_ACTIVITIES);
			if (pi != null) {
				String versionName = pi.versionName == null ? "null"
						: pi.versionName;
				String versionCode = pi.versionCode + "";
				infos.put("packageName", ctx.getPackageName());
				infos.put("appName", pi.applicationInfo.loadLabel(pm).toString());
				infos.put("versionName", versionName);
				infos.put("exceptionName", exceptionName);
				infos.put("versionCode", versionCode);
			}
		} catch (NameNotFoundException e) {
			Log.e(TAG, "an error occured when collect package info", e);
		}
		Field[] fields = Build.class.getDeclaredFields();
		for (Field field : fields) {
			try {
				field.setAccessible(true);
				infos.put(field.getName(), field.get(null).toString());
				// Log.d(TAG, field.getName() + " : " + field.get(null));
			} catch (Exception e) {
				Log.e(TAG, "an error occured when collect crash info", e);
			}
		}

		saveCrashInfo2File(ctx, infos);
	}

	/**
	 * @description: 收集游戏安装出错信息
	 *
	 * @param ctx
	 * @param packageName
	 * @param appName
	 * @param versionName
	 * @param exceptionCode
	 * @author: LiuQin
	 * @date: 2015年10月9日 下午11:54:32
	 */
	public static void collectGameInstallFailedInfo(Context ctx, String packageName,
			String appName, String versionName, int exceptionCode) {
		String exceptionName = installFailureToString(exceptionCode);
		// 用来存储设备信息和异常信息
		Map<String, String> infos = new LinkedHashMap<String, String>();
		infos.put("packageName", packageName);
		infos.put("appName", appName);
		infos.put("versionName", versionName);
		infos.put("exceptionName", exceptionName);
		Field[] fields = Build.class.getDeclaredFields();
		for (Field field : fields) {
			try {
				field.setAccessible(true);
				infos.put(field.getName(), field.get(null).toString());
				// Log.d(TAG, field.getName() + " : " + field.get(null));
			} catch (Exception e) {
				Log.e(TAG, "an error occured when collect crash info", e);
			}
		}

		saveCrashInfo2File(ctx, infos);
	}

	/**
	 * 保存错误信息到文件中
	 * 
	 * @param ex
	 * @return 返回文件名称,便于将文件传送到服务器
	 */
	private static String saveCrashInfo2File(Context context, Map<String, String> infos) {
		StringBuffer sb = new StringBuffer();
		for (Map.Entry<String, String> entry : infos.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			sb.append(key + "=" + value + "\n");
		}
		
		DateFormat formatter = new SimpleDateFormat("yyyyMMdd-HHmmss");  
		long timestamp = System.currentTimeMillis();  
		String time = formatter.format(new Date());  
       sb.append("time="+time+"\n");

		try {
			String fileName = "installFailed-" + time + ".log";
			String path = null;
			if (Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)) {
				// path =
				// Environment.getExternalStorageDirectory().getPath()+"/ATET/"+context.getPackageName()+"/crash/";
				path = Environment.getExternalStorageDirectory().getPath()
						+ "/ATET/tvmarket/crash/";
			} else {
				path = context.getFilesDir().getPath() + "/crash/";
			}

			File dir = new File(path);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			FileOutputStream fos = new FileOutputStream(path + fileName);
			fos.write(sb.toString().getBytes());
			fos.close();
			return fileName;
		} catch (Exception e) {
			Log.e(TAG, "an error occured while writing file...", e);
		}
		return null;
	}

	/**
	 * Converts a failure code into a string by using reflection to find a
	 * matching constant in PackageManager.
	 */
	private static String installFailureToString(int result) {
		Field[] fields = PackageManager.class.getFields();
		for (Field f : fields) {
			if (f.getType() == int.class) {
				int modifiers = f.getModifiers();
				// only look at public final static fields.
				if (((modifiers & Modifier.FINAL) != 0)
						&& ((modifiers & Modifier.PUBLIC) != 0)
						&& ((modifiers & Modifier.STATIC) != 0)) {
					String fieldName = f.getName();
					if (fieldName.startsWith("INSTALL_FAILED_")
							|| fieldName.startsWith("INSTALL_PARSE_FAILED_")) {
						// get the int value and compare it to result.
						try {
							if (result == f.getInt(null)) {
								return fieldName;
							}
						} catch (IllegalAccessException e) {
							// this shouldn't happen since we only look for
							// public static fields.
						}
					}
				}
			}
		}

		// couldn't find a matching constant? return the value
		return Integer.toString(result);
	}

}
