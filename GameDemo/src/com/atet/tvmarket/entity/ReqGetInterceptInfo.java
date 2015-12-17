package com.atet.tvmarket.entity;

public class ReqGetInterceptInfo {
    private String deviceCode;
    private String deviceId;
    private long minTime;
    
    public ReqGetInterceptInfo() {
        // TODO Auto-generated constructor stub
    }

    public String getDeviceCode() {
        return deviceCode;
    }

    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
    }

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
		return "ReqGetInterceptInfo [deviceCode=" + deviceCode + ", deviceId="
				+ deviceId + ", minTime=" + minTime + "]";
	}

    
    
}
