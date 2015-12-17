package com.atet.tvmarket.entity;

public class GameInfoFromDeviceId implements AutoType {
	private String deviceId;

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	@Override
	public String toString() {
		return "GameInfoFromDeviceId [deviceId=" + deviceId + "]";
	}
}
