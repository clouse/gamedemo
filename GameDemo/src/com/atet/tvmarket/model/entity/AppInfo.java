package com.atet.tvmarket.model.entity;

import java.io.Serializable;

import com.atet.tvmarket.entity.AutoType;
import com.atet.tvmarket.model.database.BaseModel;
import com.atet.tvmarket.model.database.TableDescription;

import android.graphics.drawable.Drawable;

@TableDescription(name = "billInfo")
public class AppInfo extends BaseModel implements AutoType, Serializable {

	private static final long serialVersionUID = 1L;
	public String appName = "";
	public String packageName = "";
	public Drawable appIcon = null;
	public String versionName = "";
	public int versionCode = 0;

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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public int getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(int versionCode) {
		this.versionCode = versionCode;
	}

	@Override
	public String toString() {
		return "AppInfo ["
				+ (appName != null ? "appName=" + appName + ", " : "")
				+ (packageName != null ? "packageName=" + packageName + ", "
						: "")
				+ (appIcon != null ? "appIcon=" + appIcon + ", " : "")
				+ (versionName != null ? "versionName=" + versionName + ", "
						: "") + "versionCode=" + versionCode + "]";
	}
}
