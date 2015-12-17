package com.atet.tvmarket.entity;

public class ObtainUserGiftReq implements AutoType {
	private String userId;
	private String deviceId;
	private String giftPackageid;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getGiftPackageid() {
		return giftPackageid;
	}

	public void setGiftPackageid(String giftPackageid) {
		this.giftPackageid = giftPackageid;
	}

	@Override
	public String toString() {
		return "GetUserGiftReq [userId=" + userId + ", deviceId=" + deviceId
				+ ", giftPackageid=" + giftPackageid + "]";
	}

}
