package com.atet.tvmarket.entity;

public class ThirdGameInfoFromGameIdReq implements AutoType {
	private String deviceId;
	private String ids;

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	@Override
	public String toString() {
		return "ThirdGameInfoFromGameIdReq [deviceId=" + deviceId + ", ids="
				+ ids + "]";
	}
	
}
