package com.atet.tvmarket.utils;

import java.net.URI;
import java.net.URL;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.widget.Toast;

import com.atet.common.logging.ALog;
import com.atet.tvmarket.R;
import com.atet.tvmarket.app.Constant;
import com.atet.tvmarket.view.NewToast;

public class NetUtil {
	static ALog alog = ALog.getLogger(NetUtil.class);
	private NetUtil() {
	}

	public static boolean isWifiOpen(Context context) {
		
		// 获取网络查看网络是否可用改成检测所有的网络网络是否可用，其中包括了拨号上网连接
		try {
			ConnectivityManager connec = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connec == null)
				return false;

			NetworkInfo[] allinfo = connec.getAllNetworkInfo();

			if (allinfo != null) {
				for (int i = 0; i < allinfo.length; i++) {
					if (allinfo[i].isAvailable() && allinfo[i].isConnected()) {
						return true;
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;

	}

	/**
	 * 
	 * @Title: isNetworkAvailable
	 * @Description: TODO
	 * @param @param context
	 * @param @param isAllowConnecting
	 * @param @return
	 * @return boolean true表示网络正常 false表示网络异常
	 * @throws
	 */
	public static boolean isNetworkAvailable(Context context,
			boolean isAllowConnecting) {
		// 获得网络系统连接服务
		try {
			ConnectivityManager connec = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connec == null)
				return false;

			NetworkInfo[] allinfo = connec.getAllNetworkInfo();

			if (allinfo != null) {
				for (int i = 0; i < allinfo.length; i++) {
					if (allinfo[i].isAvailable() && allinfo[i].isConnected()) {
						return true;
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * 
	 * @Title: isEnableDownload
	 * @Description: TODO 判断是否可以下载
	 * @param @param context
	 * @param @param isToast
	 * @param @return
	 * @return boolean
	 * @throws
	 */
	public static boolean isEnableDownload(Context context, boolean isToast) {
		boolean result = false;
//		if (NetUtil.isWifiOpen(context)) {
//			result = true;
//		} else 
		if (NetUtil.isNetworkAvailable(context, false)) {
			result = true;
		} else {
			if (isToast) {
				NewToast.makeToast(context, R.string.down_error_disconnect,
						Toast.LENGTH_SHORT).show();
			}
		}

		if (!Constant.IS_USE_CACHE_PATH_TO_SAVE) {
			// 如果不允许保存到内部存储，检测sd卡是否存在
			// if (result && !DeviceTool.isSDCardUsable()) {
			if (result && !DeviceTool.isSDCardReadWritable()) {
				result = false;
				if (isToast) {
					NewToast.makeToast(context, R.string.down_error_sd_not_mount,
							Toast.LENGTH_SHORT).show();
				}
			}
		}

		return result;
	}

	public static URI getValidUri(String urlStr) {
		try {
			URL url = new URL(Uri.decode(urlStr));
			alog.info("decode url:" + url.toString());
			URI uri = new URI(url.getProtocol(), url.getUserInfo(),
					url.getHost(), url.getPort(), url.getPath(),
					url.getQuery(), url.getRef());
			return uri;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * 
	 * @Title: checkNetWorkStatus
	 * @Description: TODO 检查当前的网络是否可用
	 * @param @param context
	 * @param @return
	 * @return boolean
	 * @throws
	 */
	public static boolean checkNetWorkStatus(Context context) {
		boolean state;
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netinfo = cm.getActiveNetworkInfo();
		if (netinfo != null && netinfo.isConnected()) {
			state = true;
			alog.info("The net was connected");
		} else {
			state = false;
			alog.info("The net was bad!");
		}
		return state;
	}
}
