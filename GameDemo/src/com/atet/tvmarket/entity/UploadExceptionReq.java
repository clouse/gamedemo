package com.atet.tvmarket.entity;

import java.io.File;

public class UploadExceptionReq implements AutoType {
	private String deviceId;
	private String productId;
	private String packageName;
	private String appName;
	private String versionName;
	private String exceptionName;
	private File file;

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public String getExceptionName() {
		return exceptionName;
	}

	public void setExceptionName(String exceptionName) {
		this.exceptionName = exceptionName;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	@Override
	public String toString() {
		return "UploadExceptionReq [deviceId=" + deviceId + ", productId="
				+ productId + ", packageName=" + packageName + ", appName="
				+ appName + ", versionName=" + versionName + ", exceptionName="
				+ exceptionName + ", file=" + file + "]";
	}
}
