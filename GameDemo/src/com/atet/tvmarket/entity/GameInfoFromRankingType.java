package com.atet.tvmarket.entity;

public class GameInfoFromRankingType implements AutoType {
	private String deviceId;
	private Integer type;

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "GameInfoFromRankingType [deviceId=" + deviceId + ", type="
				+ type + "]";
	}

}
