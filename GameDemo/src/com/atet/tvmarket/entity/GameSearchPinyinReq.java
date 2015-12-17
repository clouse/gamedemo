package com.atet.tvmarket.entity;

public class GameSearchPinyinReq implements AutoType {
	private String deviceId;
	private int type;

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "GameSearchReq [deviceId=" + deviceId + ", type=" + type + "]";
	}

}
