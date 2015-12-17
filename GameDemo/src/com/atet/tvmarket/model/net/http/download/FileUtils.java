package com.atet.tvmarket.model.net.http.download;

import java.io.File;

import com.atet.tvmarket.app.BaseApplication;
import com.atet.tvmarket.app.Constant;
import com.atet.tvmarket.control.mygame.activity.MyGameActivity;
import com.atet.tvmarket.entity.Group;
import com.atet.tvmarket.model.database.PersistentSynUtils;
import com.atet.tvmarket.model.entity.ApkInfo;
import com.atet.tvmarket.model.entity.MyGameInfo;
import com.atet.tvmarket.utils.AppUtil;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;


public class FileUtils {
	private static final String TAG = "FileUtils";

	/**
	 * 
	 * @Title: getFile
	 * @Description: TODO 获取指定目录下的所有Apk文件信息,并且将apk信息存储到数据库中
	 * @param @param path
	 * @return void
	 * @throws
	 */
	public static Group<ApkInfo> getApkFileAndSavetoDB(String path) {
		Group<ApkInfo> apkFiles = new Group<ApkInfo>();
		File file = new File(path);
		File[] allFiles = file.listFiles();
		if (allFiles == null || allFiles.length == 0) {
			return null;
		}
		ApkInfo apkInfo = new ApkInfo();
		for (int i = 0; i < allFiles.length; i++) {
			File mFile = allFiles[i];
			// 如果是文件，并且是APK文件
			if (mFile.isFile() && isApkFile(mFile.getName())) {
				apkInfo = getApkInfo(mFile.getAbsolutePath(),
						BaseApplication.getContext());
				// 如果此包名的apk不存在于数据库表中，则存储于DB
				if (!isInAppInfoTable(apkInfo.getPackageName())) {
					apkFiles.add(apkInfo);
					saveApkInfoToMyGameInfo(BaseApplication.getContext(), apkInfo);
				}
			}
		}
		return apkFiles;
	}

	/**
	 * 
	 * @Title: isApkFile
	 * @Description: TODO 判断某个文件是不是apk文件
	 * @param @param fileName
	 * @param @return
	 * @return boolean
	 * @throws
	 */
	public static boolean isApkFile(String fileName) {
		int fileNameLen = fileName.length();
		if (fileNameLen < 4) {
			return false;
		}
		String fileSuffix = fileName.substring(fileNameLen - 4, fileNameLen);
		if (".apk".equals(fileSuffix)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 获取apk包的信息：版本号，名称，图标等
	 * 
	 * @param absPath
	 *            apk包的绝对路径
	 * @param context
	 */
	public static ApkInfo getApkInfo(String absPath, Context context) {
		ApkInfo apkInfo = new ApkInfo();
		PackageManager pm = context.getPackageManager();
		PackageInfo pkgInfo = pm.getPackageArchiveInfo(absPath,
				PackageManager.GET_ACTIVITIES);
		if (pkgInfo != null) {
			ApplicationInfo appInfo = pkgInfo.applicationInfo;
			/* 必须加这两句，不然下面icon获取是default icon而不是应用包的icon */
			appInfo.sourceDir = absPath;
			appInfo.publicSourceDir = absPath;

			String appName = pm.getApplicationLabel(appInfo).toString();// 得到应用名
			String packageName = appInfo.packageName; // 得到包名
			String versionName = pkgInfo.versionName; // 得到版本信息
			int versionCode = pkgInfo.versionCode;
			Drawable icon = pm.getApplicationIcon(appInfo);// 得到图标信息
			apkInfo.setAppIcon(icon);
			apkInfo.setAppName(appName);
			apkInfo.setLaunchAct(AppUtil.getActivitie(BaseApplication.getContext(),
					packageName));
			apkInfo.setPackageName(packageName);
			apkInfo.setVersionCode(versionCode);
			apkInfo.setApkDir(getFileDir(absPath));
			apkInfo.setApkName(getFileName(absPath));
		}
		return apkInfo;
	}

	/**
	 * 
	 * @Title: saveApkInfoToMyGameInfo
	 * @Description: TODO 将APK信息保存到mygameinfo中
	 * @param @param context
	 * @param @param mApkInfos
	 * @return void
	 * @throws
	 */
	public static void saveApkInfoToMyGameInfo(Context context, ApkInfo apkInfo) {
		if (apkInfo == null) {
			return;
		}
		MyGameInfo myGameInfo = new MyGameInfo();
		myGameInfo.setName(apkInfo.getAppName());
		myGameInfo.setPackageName(apkInfo.getPackageName());
		myGameInfo.setLaunchAct(AppUtil.getActivitie(context,
				apkInfo.getPackageName()));
		myGameInfo.setState(Constant.GAME_STATE_NOT_INSTALLED_APK);
		myGameInfo.setVersionCode(apkInfo.getVersionCode());
		myGameInfo.setLocalDir(apkInfo.apkDir);
		myGameInfo.setLocalFilename(apkInfo.apkName);
		long id = PersistentSynUtils.addModel(myGameInfo);
		if (id != -1) {
			myGameInfo.setAutoIncrementId(id + "");
			myGameInfo.setGameId((-id) + "");
			PersistentSynUtils.update(myGameInfo);
		}
	}

	/**
	 * 
	 * @Title: getApkIconForPkgname
	 * @Description: TODO根据包名获取apkicon
	 * @param @param pkgName
	 * @param @return
	 * @return Drawable
	 * @throws
	 */
	public static Drawable getApkIconForPkgname(String pkgName) {
		Group<ApkInfo> apkInfos = PersistentSynUtils.getModelList(
				ApkInfo.class, " packageName='" + pkgName + "';");
		if (apkInfos == null || apkInfos.size() == 0) {
			return null;
		}
		ApkInfo apkInfo = apkInfos.get(0);
		Drawable apkIcon = apkInfo.getAppIcon();
		return apkIcon;
	}

	/**
	 * 
	 * @Title: isInDB
	 * @Description: TODO根据包名判断某个游戏是否在appinfo表中
	 * @param @param pkgName
	 * @param @return
	 * @return boolean
	 * @throws
	 */
	public static boolean isInAppInfoTable(String pkgName) {
		Group<MyGameInfo> myGameInfos = PersistentSynUtils.getModelList(
				MyGameInfo.class, " packageName='" + pkgName + "';");
		if (myGameInfos == null || myGameInfos.size() == 0) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 
	 * @Title: getApkConfig
	 * @Description: TODO 获取apk的配置文件
	 * @param @param pkgName
	 * @return 是否成功获取
	 * @throws
	 */
	public static boolean getApkConfig(String pkgName) {
		if (pkgName == null || "".equals(pkgName)) {
			return false;
		}
		boolean isOk = FileDownloader.getConfigFile(BaseApplication.getContext(),
				pkgName);
		return isOk;
	}

	/**
	 * 
	 * @Title: getFileName
	 * @Description: TODO 获取文件名
	 * @param @param path
	 * @param @return
	 * @return String
	 * @throws
	 */
	public static String getFileName(String path) {
		if (path == null || path.length() == 0) {
			return "";
		}
		int position = path.lastIndexOf("/");
		String filiName = path.substring(position + 1, path.length());
		return filiName;
	}

	/**
	 * 
	 * @Title: getFileDir
	 * @Description: TODO 获取文件目录
	 * @param @param path
	 * @param @return
	 * @return String
	 * @throws
	 */
	public static String getFileDir(String path) {
		if (path == null || path.length() == 0) {
			return "";
		}
		int position = path.lastIndexOf("/");
		String filiDir = path.substring(0, position + 1);
		return filiDir;
	}

	/**
	 * 
	 * @Title: getApkIcon
	 * @Description: TODO 获取apk图标
	 * @param @param absPath
	 * @param @param context
	 * @param @return
	 * @return Drawable
	 * @throws
	 */
	public static Drawable getApkIcon(String absPath, Context context) {
		Drawable apkIcon = null;
		PackageManager pm = context.getPackageManager();
		PackageInfo pkgInfo = pm.getPackageArchiveInfo(absPath,
				PackageManager.GET_ACTIVITIES);
		if (pkgInfo != null) {
			ApplicationInfo appInfo = pkgInfo.applicationInfo;
			/* 必须加这两句，不然下面icon获取是default icon而不是应用包的icon */
			appInfo.sourceDir = absPath;
			appInfo.publicSourceDir = absPath;
			Drawable icon = pm.getApplicationIcon(appInfo);// 得到图标信息
			apkIcon = icon;
		}
		return apkIcon;
	}

	/**
	 * 
	 * @Title: removeApkInfoFromDB
	 * @Description: TODO 将apk信息从数据库中删除
	 * @param
	 * @return void
	 * @throws
	 */
	public static void removeApkInfoFromDB() {
		Group<MyGameInfo> apkInfos = PersistentSynUtils.getModelList(
				MyGameInfo.class, " state ="
						+ Constant.GAME_STATE_NOT_INSTALLED_APK + ";");
		if (apkInfos == null || apkInfos.size() == 0) {
			return;
		}
		MyGameInfo apkInfo = new MyGameInfo();
		for (int i = 0; i < apkInfos.size(); i++) {
			apkInfo = apkInfos.get(i);
			// 如果是被手动删除的，那么将信息从数据库中删除
			if (isRemovedByManual(apkInfo)) {
				PersistentSynUtils.delete(apkInfo);
			}
		}
	}

	/**
	 * 
	 * @Title: isRemovedByManual
	 * @Description: TODO 判断apk文件是否被手动删除了
	 * @param @param myGameInfo
	 * @param @return
	 * @return boolean
	 * @throws
	 */
	public static boolean isRemovedByManual(MyGameInfo myGameInfo) {
		String file = myGameInfo.getLocalDir() + myGameInfo.getLocalFilename();
		File apkFile = new File(file);
		if (apkFile.exists()) {
			return false;
		} else {
			return true;
		}
	}

}
