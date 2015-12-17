package com.atet.tvmarket.entity;

public class ActivityInfoReq implements AutoType {
	private String deviceId;
	private int type;
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

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "ActivityInfoReq [deviceId=" + deviceId + ", type=" + type
				+ ", minTime=" + minTime + "]";
	}

}
