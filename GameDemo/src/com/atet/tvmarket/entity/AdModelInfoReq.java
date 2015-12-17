package com.atet.tvmarket.entity;

public class AdModelInfoReq implements AutoType {
	private String deviceId;
	private String modelId;
	private Long minTime;

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getModelId() {
		return modelId;
	}

	public void setModelId(String modelId) {
		this.modelId = modelId;
	}

	public Long getMinTime() {
		return minTime;
	}

	public void setMinTime(Long minTime) {
		this.minTime = minTime;
	}

	@Override
	public String toString() {
		return "AdsInfoReq [deviceId=" + deviceId + ", modelId=" + modelId
				+ ", minTime=" + minTime + "]";
	}

}
