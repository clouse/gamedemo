package com.atet.tvmarket.entity;

public class ReqGetConfiigInfo implements AutoType{
	private String deviceId;
    private String packageName;
    private int versionCode;
    private int height;
    private int wide;
    private int density;

    public ReqGetConfiigInfo() {
        // TODO Auto-generated constructor stub
    }

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public int getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(int versionCode) {
		this.versionCode = versionCode;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWide() {
		return wide;
	}

	public void setWide(int wide) {
		this.wide = wide;
	}

	public int getDensity() {
		return density;
	}

	public void setDensity(int density) {
		this.density = density;
	}

	@Override
	public String toString() {
		return "ReqGetConfiigInfo [deviceId=" + deviceId + ", packageName="
				+ packageName + ", versionCode=" + versionCode + ", height="
				+ height + ", wide=" + wide + ", density=" + density + "]";
	}


   
}
