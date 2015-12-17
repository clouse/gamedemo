package com.atet.tvmarket.utils;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.atet.tvmarket.app.Constant;
import com.atet.tvmarket.entity.Group;
import com.atet.tvmarket.model.database.PersistentSynUtils;
import com.atet.tvmarket.model.entity.AppInfo;
import com.atet.tvmarket.model.entity.MyGameInfo;

public class AppUtil {
	/**
	 * @Title: getPkgInfoByName
	 * @Description: 由应用包名得到应用信息
	 * @param context
	 * @param pkgName
	 * @return
	 * @throws
	 */
	public static PackageInfo getPkgInfoByName(Context context, String pkgName) {
		PackageInfo pkgInfo = null;
		PackageManager pm = context.getPackageManager();
		try {
			// 0代表是获取版本信息
			pkgInfo = pm.getPackageInfo(pkgName, 0);

		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pkgInfo;
	}

	/**
	 * @Title: startAppByPkgName
	 * @Description: 由package name启动应用
	 * @param context
	 * @param pkgName
	 * @return
	 * @throws
	 */
	public static boolean startAppByPkgName(Context context, String pkgName) {
		PackageManager packageManager = context.getPackageManager();
		Intent intent = new Intent();
		intent = packageManager.getLaunchIntentForPackage(pkgName);
		if (intent == null) {
			System.out.println("APP not found!");
			return false;
		}
		context.startActivity(intent);
		return true;
	}

	/**
	 * @Title: isAlreadyInstall
	 * @Description: 由package name判断程序是否已经安装应用
	 * @param context
	 * @param pkgName
	 * @return
	 * @throws
	 */
	public static boolean isAlreadyInstall(Context context, String pkgName) {
		final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
		List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
		List<String> pName = new ArrayList<String>();// 用于存储所有已安装程序的包名
		// 从pinfo中将包名字逐一取出，压入pName list中
		if (pinfo != null) {
			for (int i = 0; i < pinfo.size(); i++) {
				String pn = pinfo.get(i).packageName;
				pName.add(pn);
			}
		}
		return pName.contains(pkgName);
	}

	public static List<ResolveInfo> queryAppInfo(Context context,
			String packageName) {
		PackageManager pm = context.getPackageManager(); // 获得PackageManager对象
		Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
		mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		mainIntent.setPackage(packageName);
		// 通过查询，获得所有ResolveInfo对象.
		List<ResolveInfo> resolveInfos = pm.queryIntentActivities(mainIntent,
				PackageManager.GET_INTENT_FILTERS);
		return resolveInfos;
	}

	public static void setProDialogFontSize(Dialog dialog, int size) {
		if (dialog == null) {
			return;
		}
		Window window = dialog.getWindow();
		View view = window.getDecorView();
		TextView messageTv = (TextView) view.findViewById(android.R.id.message);
		if (messageTv != null) {
			messageTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
		}
	}

	public static void setDialogAlpha(Dialog dialog, int alpha) {
		if (dialog == null) {
			return;
		}
		try {
			Window window = dialog.getWindow();
			View view = window.getDecorView();
			setViewBackgroundAlpha(view, alpha);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private static void setViewBackgroundAlpha(View view, int alpha) {
		if (view instanceof ViewGroup) {
			Drawable drawable = view.getBackground();
			if (drawable != null) {
				drawable.setAlpha(alpha);
				view.setBackgroundDrawable(drawable);
			}
			ViewGroup parent = (ViewGroup) view;
			int count = parent.getChildCount();
			for (int i = 0; i < count; i++) {
				setViewBackgroundAlpha(parent.getChildAt(i), alpha);
			}
		}
	}

	/**
	 * 
	 * 方法概述：
	 * 
	 * @description：已经安装的应用的版本号，
	 * @param context
	 * @param pkgName
	 *            应用包名
	 * @return
	 * @return int
	 * @throws
	 * @author: songwei
	 * @date 2014年10月8日 上午11:52:11 修改记录： 修改者: 修改时间： 修改内容：
	 */
	public static int getInstalledAppVersionCode(Context context, String pkgName) {
		int versionCode = -1;
		PackageInfo pkgInfo = AppUtil.getPkgInfoByName(context, pkgName);
		if (pkgInfo != null) {
			versionCode = pkgInfo.versionCode;
		}
		return versionCode;
	}

	/**
	 * 
	 * @Title: getInstalledAppList
	 * @Description: TODO 获取预装游戏信息列表
	 * @param @param context
	 * @param @param preGamePkgs 从网络获取得到的包名数组
	 * @param @return
	 * @return Group<AppInfo> 返回的是所需要的预装游戏信息列表
	 * @throws
	 */
	public static Group<AppInfo> getPreInstalledGameList(Context context,
			String[] preGamePkgsFromNet) {
		Group<AppInfo> preGameList = new Group<AppInfo>();
		for (int i = 0; i < preGamePkgsFromNet.length; i++) {
			PackageInfo packageInfo = getPkgInfoByName(context,
					preGamePkgsFromNet[i]);
			// 系统应用
			if (packageInfo != null) {
				AppInfo appInfo = new AppInfo();
				appInfo.appName = packageInfo.applicationInfo.loadLabel(
						context.getPackageManager()).toString();
				appInfo.packageName = packageInfo.packageName;
				appInfo.appIcon = packageInfo.applicationInfo.loadIcon(context
						.getPackageManager());
				// 只需要返回前4个对象
				// if (preGameList.size() < 4) {
				preGameList.add(appInfo);
				// }
			}
		}
		return preGameList;
	}

	/**
	 * 
	 * @Title: savePreInstalledGameToMyGameInfo
	 * @Description: TODO 将预装游戏保存到我的游戏中
	 * @param @param context
	 * @param @param preGamePkgsFromNet 从网络中获取的预装游戏信息
	 * @return void
	 * @throws
	 */
	public static void savePreInstalledGameToMyGameInfo(Context context,
			Group<AppInfo> mPreGames) {

		Group<AppInfo> preGameList = new Group<AppInfo>();
		for (int i = 0; i < mPreGames.size(); i++) {
			PackageInfo packageInfo = getPkgInfoByName(context, mPreGames
					.get(i).getPackageName());

			if (packageInfo != null) {
				MyGameInfo myGameInfo = new MyGameInfo();
				myGameInfo.setName(packageInfo.applicationInfo.loadLabel(
						context.getPackageManager()).toString());
				myGameInfo.setPackageName(packageInfo.packageName);
				myGameInfo.setLaunchAct(getActivitie(context, mPreGames.get(i)
						.getPackageName()));
				// 判断是否为系统应用，并修改状态值(sw)
				int flag = packageInfo.applicationInfo.flags;
				if ((flag & ApplicationInfo.FLAG_SYSTEM) != 0
						|| (flag & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
					// DebugTool.info(TAG, "sytem app");
					myGameInfo.setState(Constant.GAME_STATE_INSTALLED_SYSTEM);
				} else {
					// DebugTool.info(TAG, "user app");
					myGameInfo.setState(Constant.GAME_STATE_INSTALLED_USER);
				}
				// =============================================================
				// myGameInfo.setState(Constant.GAME_STATE_INSTALLED_SYSTEM);
				myGameInfo.setVersionCode(packageInfo.versionCode);
				// 如果数据库表中已经有了记录，则不再添加
				if (isInMygameInfoDB(packageInfo.packageName)) {
					// DebugTool.info(TAG, "" + packageInfo.packageName
					// + "已经存在...");
					continue;
				}
				// DebugTool.info(TAG, "" + packageInfo.packageName
				// + "存入到了数据库中...");
				long id = PersistentSynUtils.addModel(myGameInfo);
				if (id != -1) {
					myGameInfo.setAutoIncrementId(id + "");
					myGameInfo.setGameId((-id) + "");
					myGameInfo.setDownToken(Constant.DOWN_FROM_LOCAL);
					PersistentSynUtils.update(myGameInfo);
				}
			}
		}
	}

	public static String getActivitie(Context activity, String packageName) {
		Intent localIntent = new Intent("android.intent.action.MAIN", null);
		localIntent.addCategory("android.intent.category.LAUNCHER");
		List<ResolveInfo> appList = activity.getPackageManager()
				.queryIntentActivities(localIntent, 0);
		for (int i = 0; i < appList.size(); i++) {
			ResolveInfo resolveInfo = appList.get(i);
			String packageStr = resolveInfo.activityInfo.packageName;
			if (packageStr.equals(packageName)) {
				// 这个就是你想要的那个Activity
				return resolveInfo.activityInfo.name;
			}
		}
		return "";
	}

	private static boolean isInMygameInfoDB(String packageName) {
		Group<MyGameInfo> myGamesInDB = PersistentSynUtils.getModelList(
				MyGameInfo.class, " packageName=" + "'" + packageName + "'");
		// 当数据库中有数据的时候
		if (myGamesInDB != null && myGamesInDB.size() > 0) {
			return true;
		}
		return false;
	}

	public static String getVersionName(Context context) throws Exception {
		// 获取packagemanager的实例
		PackageManager packageManager = context.getPackageManager();
		// getPackageName()是你当前类的包名，0代表是获取版本信息
		PackageInfo packInfo = packageManager.getPackageInfo(
				context.getPackageName(), 0);
		String version = packInfo.versionName;
		return version;
	}
}
