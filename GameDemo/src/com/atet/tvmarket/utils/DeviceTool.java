package com.atet.tvmarket.utils;

import java.io.File;

import com.atet.common.logging.ALog;
import com.atet.tvmarket.app.BaseApplication;
import com.atet.tvmarket.app.Constant;

import android.app.Service;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings;
import android.telephony.TelephonyManager;

public class DeviceTool {
	
	ALog alog = ALog.getLogger(DeviceTool.class);
	
	public static boolean isSDCardReadWritable() {
		File sdDir = Environment.getExternalStorageDirectory();
		if (sdDir != null && sdDir.canRead() && sdDir.canWrite()) {
			return true;
		}
		return false;
	}
	
	
	public static String getSDPath() {
		File sdDir = null;
		boolean sdCardExist = Environment.getExternalStorageState()
				.equalsIgnoreCase(Environment.MEDIA_MOUNTED);
		if (sdCardExist) {
			sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
		} else {
//			try {
//				sdDir = Environment.getExternalStorageDirectory();
//				if (sdDir != null && sdDir.canRead() && sdDir.canWrite()) {
//					DebugTool.debug("sdCard", "sdDir.getAbsolutePath " + sdDir.getAbsolutePath());
//					return sdDir.getAbsolutePath();
//				}
//			} catch (Exception e) {
//				// TODO: handle exception
//			}
//			sdDir = new File("/mnt/sdcard/");
//			if (!sdDir.exists() || !sdDir.canRead() || !sdDir.canWrite()) {
//				DebugTool.error("SD_PATH", "sd card not usable", null);
//				return "";
//			}
			return "";
		}
		return sdDir.getAbsolutePath();
	}
	
	public static long getSdAvailableSpace() {
		long availableSpace = -1;
		;
		String sdcard = null;
		boolean sdCardExist = Environment.getExternalStorageState()
				.equalsIgnoreCase(Environment.MEDIA_MOUNTED);

		if (sdCardExist) {
			sdcard = Environment.getExternalStorageDirectory().getPath();
		} else {
//			File sdDir = Environment.getExternalStorageDirectory();
//			if (sdDir != null && sdDir.canRead() && sdDir.canWrite()) {
//				sdcard = sdDir.getAbsolutePath();
//				sdCardExist = true;
//			}
			sdcard = BaseApplication.getContext().getFilesDir().getParentFile()
			.getAbsolutePath()
			+ "/";
		}
//		if (sdCardExist) {
			File file = new File(sdcard);
			StatFs statFs = new StatFs(file.getPath());
			availableSpace = (long) (statFs.getBlockSize() * ((long) statFs
					.getAvailableBlocks()));
//		}
		return availableSpace;
	}
	
	public static int getCallState(Context context) {
		try {
			TelephonyManager tm = (TelephonyManager) context
					.getSystemService(Service.TELEPHONY_SERVICE);
			return tm.getCallState();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return TelephonyManager.CALL_STATE_IDLE;
	}
	
	/**
	 * 获取指定目录下的空间
	 * 
	 * @param dir
	 * @return
	 * @throws
	 */
	public static long getAvailableSpaceByPath(String dir) {
		long availableSpace = -1;
		;
		if (dir == null || dir.length() <= 0) {
			return availableSpace;
		}
		try {
			File file = new File(dir);
			if (file.exists()) {
				StatFs statFs = new StatFs(file.getPath());
				availableSpace = (long) (statFs.getBlockSize() * ((long) statFs
						.getAvailableBlocks()));
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return availableSpace;
	}

	/**
	 * 获取指定目录下的总空间
	 * 
	 * @param dir
	 * @return
	 * @throws
	 * */
	public static long getAllSpaceByPath(String dir) {
		long allSpace = -1;
		;
		if (dir == null || dir.length() <= 0) {
			return allSpace;
		}
		try {
			File file = new File(dir);
			if (file.exists()) {
				StatFs statFs = new StatFs(file.getPath());
				allSpace = (long) (statFs.getBlockSize() * ((long) statFs
						.getBlockCount()));
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return allSpace;

	}
	
	/**
	 * 检查网络接连类型.
	 * 
	 * @param context
	 * @return SysConstants.NETWORK_TYPE_NONE: 无网络连接;
	 *         SysConstants.NETWORK_TYPE_WIFI: 通过WIFI连接网络;
	 *         SysConstants.NETWORK_TYPE_WAP: 通过GPRS连接网络.
	 */
	public static int checkNetWorkType(Context context) {
		if (isAirplaneModeOn(context)) {
			return Constant.NETWORK_TYPE_NONE;
		}

		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
				.getState() == NetworkInfo.State.CONNECTED) {
			return Constant.NETWORK_TYPE_NET;
		}

		if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
				.getState() == NetworkInfo.State.CONNECTED) {
			String type = connectivityManager.getActiveNetworkInfo()
					.getExtraInfo();
			if ("wap".equalsIgnoreCase(type.substring(type.length() - 3))) {
				return Constant.NETWORK_TYPE_WAP;
			} else {
				return Constant.NETWORK_TYPE_NET;
			}
		}

		return Constant.NETWORK_TYPE_NONE;
	}

	/**
	 * 判断手机是否处于飞行模式.
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isAirplaneModeOn(Context context) {
		return Settings.System.getInt(context.getContentResolver(),
				Settings.System.AIRPLANE_MODE_ON, 0) != 0;
	}
}
