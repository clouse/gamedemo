package com.atet.tvmarket.entity;

public class UpdateInterfaceReq implements AutoType {
	private String deviceId;
	private long lastTime;

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public long getLastTime() {
		return lastTime;
	}

	public void setLastTime(long lastTime) {
		this.lastTime = lastTime;
	}

	@Override
	public String toString() {
		return "UpdateInterfaceReq [deviceId=" + deviceId + ", lastTime="
				+ lastTime + "]";
	}
}
