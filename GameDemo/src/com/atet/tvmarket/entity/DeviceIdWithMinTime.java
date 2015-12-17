package com.atet.tvmarket.entity;

public class DeviceIdWithMinTime implements AutoType {
	private String deviceId;
	private long minTime;

	public long getMinTime() {
		return minTime;
	}

	public void setMinTime(long minTime) {
		this.minTime = minTime;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	@Override
	public String toString() {
		return "GameInfoFromDeviceIdWithTime [deviceId=" + deviceId
				+ ", minTime=" + minTime + "]";
	}
}
