package com.atet.tvmarket.entity;

public class DeviceIdWithUserIdReq implements AutoType {
	private String userId;

	private String deviceId;

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

	@Override
	public String toString() {
		return "GameTypeInfoReq [userId=" + userId + ", deviceId=" + deviceId
				+ "]";
	}

}
