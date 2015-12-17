package com.atet.tvmarket.control.mygame.receiver;

import java.io.File;
import java.util.List;
import com.atet.tvmarket.app.Constant;
import com.atet.tvmarket.control.mygame.activity.MyGameActivity;
import com.atet.tvmarket.control.mygame.utils.DownloadingUtil;
import com.atet.tvmarket.entity.Group;
import com.atet.tvmarket.model.database.PersistentSynUtils;
import com.atet.tvmarket.model.entity.MyGameInfo;
import com.atet.tvmarket.model.net.http.download.DownloadTask;
import com.atet.tvmarket.utils.AppUtil;
import com.atet.tvmarket.utils.DeviceTool;
import com.atet.tvmarket.utils.StringTool;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.ResolveInfo;
import android.os.Bundle;



public class BReceiver extends BroadcastReceiver {
	public static final String TAG = "RECEIVER";
	// 该值用来判断调用系统安装或者卸载是否成功
    // 安装或者卸载成功则返回快速开始更新游戏信息
	public static String needInstalledGameInfo = null;
	public static String updatePackageName = null;
	@Override
	public void onReceive(Context context, Intent intent) {
		try {
			String action = intent.getAction();
			boolean replacing = intent.getBooleanExtra(Intent.EXTRA_REPLACING, false);
			// if(action.equals(Intent.ACTION_PACKAGE_ADDED) ||
//			 action.equals(Intent.ACTION_PACKAGE_REPLACED)){
			if (action.equals(Intent.ACTION_PACKAGE_ADDED)) { 
				final String packageName = intent.getData()
						.getSchemeSpecificPart();
				appHandle(context, packageName, DownloadTask.OP_INSTALL, replacing);
			} else if (action.equals(Intent.ACTION_PACKAGE_REMOVED)) { // 游戏安装或者卸载
				final String packageName = intent.getData()
						.getSchemeSpecificPart();
				appHandle(context, packageName, DownloadTask.OP_UNINSTALL, replacing);
			} else if (action.equals(Intent.ACTION_MEDIA_MOUNTED)) {  // 初始化下载目录
				initSdMounted();
			} else if (action.equals(Intent.ACTION_BOOT_COMPLETED)) {
				SharedPreferences sp = context.getSharedPreferences("config",
						Context.MODE_PRIVATE);
				sp.edit().putBoolean("isRun", false).commit();
//				Intent giftUpdateIntent = new Intent(context,
//						GiftUpdateService.class);
//				context.startService(giftUpdateIntent);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @description 根据对应广播，作出相应的处理
	 * @param context
	 * @param packageName 游戏包名
	 * @param opType 类型：安装或者卸载
	 * @throws
	 * @author：陈庆文
	 * @date 2015-8-28上午10:51:27
	 *
	 */
	private void appHandle(final Context context, final String packageName,
			final int opType, final boolean replacing) {
		if (StringTool.isEmpty(packageName)) {
			return;
		}

		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					boolean isGame = true;
					Group<MyGameInfo> myGameInfos = PersistentSynUtils
							.getModelList(MyGameInfo.class, " packageName='"
									+ packageName + "'");
					if (myGameInfos != null && myGameInfos.size() > 0) {
						MyGameInfo myGameInfo = myGameInfos.get(0);

						if (opType == DownloadTask.OP_INSTALL) {  // 游戏安装成功广播
							needInstalledGameInfo = myGameInfo
									.getPackageName();
							myGameInfo.setState(Constant.GAME_STATE_INSTALLED);
							List<ResolveInfo> resolveInfos = AppUtil
									.queryAppInfo(context, packageName);
							if (resolveInfos != null && resolveInfos.size() > 0) {
								myGameInfo.setLaunchAct(resolveInfos.get(0).activityInfo.name);
							}
							if (isGame) {
								PersistentSynUtils.update(myGameInfo);
							} else {
								PersistentSynUtils.delete(myGameInfo);
							}

							Intent in = new Intent(
									DownloadTask.ACTION_ON_APP_INSTALLED);
							Bundle bundle = new Bundle();
							bundle.putInt("opType", DownloadTask.OP_INSTALL);
							// bundle.putString("packageName", packageName);
							// bundle.putString("gameId",
							// myGameInfo.getGameId());
							bundle.putSerializable("myGameInfo", myGameInfo);
							if (!isGame) {
								bundle.putInt(DownloadTask.FILE_TYPE_KEY,
										DownloadTask.FILE_TYPE_APP);
							}
							in.putExtra("data", bundle);
							context.sendBroadcast(in);
							DownloadingUtil.setmInstallingPackageName(null);
						} else if (opType == DownloadTask.OP_UNINSTALL) {  // 游戏卸载成功广播
							// 特别添加，解决更新应用时图标被删除的问题
							if (replacing && isGame) {
								return;
							}
							needInstalledGameInfo = myGameInfo
									.getPackageName();
							PersistentSynUtils.delete(myGameInfo);
							Intent in = new Intent(
									DownloadTask.ACTION_ON_APP_INSTALLED);
							Bundle bundle = new Bundle();
							bundle.putInt("opType", DownloadTask.OP_UNINSTALL);
							// bundle.putString("packageName", packageName);
							// bundle.putString("gameId",
							// myGameInfo.getGameId());
							bundle.putSerializable("myGameInfo", myGameInfo);
							if (!isGame) {
								bundle.putInt(DownloadTask.FILE_TYPE_KEY,
										DownloadTask.FILE_TYPE_APP);
							}
							in.putExtra("data", bundle);
							context.sendBroadcast(in);
						} else {
							return;
						}

						if (myGameInfo.getLocalFilename().toLowerCase()
								.endsWith(MyGameActivity.ZIP_EXT)) {
							// 删除压缩包
							File file = new File(MyGameActivity
									.getLocalUnApkPath(myGameInfo));
							if (file != null && file.exists()) {
								file.delete();
							}
						}

						boolean isDelZip = true;
						// if (opType == DownloadTask.OP_INSTALL) {
						// SharedPreferences preferences = context
						// .getSharedPreferences("marketApp",
						// Context.MODE_PRIVATE);
						// isDelZip = preferences
						// .getBoolean("isDelZip", false);
						// }
						if (isDelZip) {
							// 删除apk 文件
							File apkFile = new File(myGameInfo.getLocalDir(),
									myGameInfo.getLocalFilename());
							if (apkFile != null && apkFile.exists()) {
								apkFile.delete();
							}
						}
					} else {
//						DebugTool.info(TAG, "[run] app");
						if (opType == DownloadTask.OP_INSTALL) {
							List<ResolveInfo> resolveInfos = AppUtil
									.queryAppInfo(context, packageName);
							ResolveInfo appInfo = null;
							if (resolveInfos != null && resolveInfos.size() > 0) {
								appInfo = resolveInfos.get(0);
							}
							if (appInfo == null) {
								return;
							}
							MyGameInfo myGameInfo = new MyGameInfo();
							myGameInfo.setPackageName(packageName);
							myGameInfo.setLaunchAct(appInfo.activityInfo.name);
							myGameInfo.setName(appInfo.activityInfo
									.loadLabel(context.getPackageManager())
									+ "");
							int flag = appInfo.activityInfo.applicationInfo.flags;
							if ((flag & ApplicationInfo.FLAG_SYSTEM) != 0
									|| (flag & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
								myGameInfo
										.setState(Constant.GAME_STATE_INSTALLED_SYSTEM);
							} else {
								myGameInfo
										.setState(Constant.GAME_STATE_INSTALLED_USER);
							}
							// 不需要把其他第三方应用添加到数据库
//							PersistentSynUtils.addModel(myGameInfo);

							Intent in = new Intent(
									DownloadTask.ACTION_ON_APP_INSTALLED);
							Bundle bundle = new Bundle();
							bundle.putInt("opType", DownloadTask.OP_INSTALL);
							bundle.putSerializable("myGameInfo", myGameInfo);
							bundle.putInt(DownloadTask.FILE_TYPE_KEY,
									DownloadTask.FILE_TYPE_APP);
							in.putExtra("data", bundle);
							context.sendBroadcast(in);
						} else if (opType == DownloadTask.OP_UNINSTALL) {
//							DebugTool.info(TAG, "receiver:OP_UNINSTALL");
							MyGameInfo myGameInfo = new MyGameInfo();
							myGameInfo.setPackageName(packageName);
							Intent in = new Intent(
									DownloadTask.ACTION_ON_APP_INSTALLED);
							Bundle bundle = new Bundle();
							bundle.putInt("opType", DownloadTask.OP_UNINSTALL);
							bundle.putSerializable("myGameInfo", myGameInfo);
							bundle.putInt(DownloadTask.FILE_TYPE_KEY,
									DownloadTask.FILE_TYPE_APP);
							in.putExtra("data", bundle);
							context.sendBroadcast(in);
						}

					}
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
		}).start();
	}

	/**
	 * 
	 * @description 初始化游戏下载存储路径
	 * @throws
	 * @author：陈庆文
	 * @date 2015-8-28上午10:52:26
	 *
	 */
	public static void initSdMounted() {
		String sdRoot = DeviceTool.getSDPath();
		if (StringTool.isEmpty(sdRoot)) {
			return;
		}
		if (Constant.SDCARD_ROOT.equals(sdRoot)) {
			return;
		}
		Constant.SDCARD_ROOT = sdRoot;
		// 游戏下载保存目录
		Constant.GAME_DOWNLOAD_LOCAL_DIR = Constant.SDCARD_ROOT
				+ Constant.GAME_RELATIVE_DIR;
		// 游戏数据包存放目录
		Constant.GAME_ZIP_DATA_LOCAL_DIR = Constant.SDCARD_ROOT + "/";
	}
}