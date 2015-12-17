package com.atet.tvmarket.model.entity;

import java.io.Serializable;

import com.atet.tvmarket.entity.AutoType;
import com.atet.tvmarket.model.database.BaseModel;
import com.atet.tvmarket.model.database.TableDescription;

import android.graphics.drawable.Drawable;


@TableDescription(name = "ApkInfo")
public class ApkInfo extends BaseModel implements AutoType, Serializable {

	private static final long serialVersionUID = 1L;
	public String appName = ""; // 应用名
	public String packageName = "";// 包名
	public Drawable appIcon = null;// 图标
	public String versionName = "";// 版本名
	public String gameId = ""; // gameId
	public String launchAct = ""; // 启动act
	public int versionCode = 0; // versionCode
	public String apkName = ""; // apk的文件名
	public String apkDir = ""; // apk的目录名

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public Drawable getAppIcon() {
		return appIcon;
	}

	public void setAppIcon(Drawable appIcon) {
		this.appIcon = appIcon;
	}

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

	public String getLaunchAct() {
		return launchAct;
	}

	public void setLaunchAct(String launchAct) {
		this.launchAct = launchAct;
	}

	public int getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(int versionCode) {
		this.versionCode = versionCode;
	}

	public String getApkName() {
		return apkName;
	}

	public void setApkName(String apkName) {
		this.apkName = apkName;
	}

	public String getApkDir() {
		return apkDir;
	}

	public void setApkDir(String apkDir) {
		this.apkDir = apkDir;
	}

	@Override
	public String toString() {
		return "ApkInfo [appName=" + appName + ", packageName=" + packageName
				+ ", appIcon=" + appIcon + ", versionName=" + versionName
				+ ", gameId=" + gameId + ", launchAct=" + launchAct
				+ ", versionCode=" + versionCode + ", apkName=" + apkName
				+ ", apkDir=" + apkDir + "]";
	}

}
