package com.atet.tvmarket.entity;

public class NoticeInfoReq implements AutoType {
	private String deviceId;
	private int noticeType;
	private long minTime;

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public int getNoticeType() {
		return noticeType;
	}

	public void setNoticeType(int noticeType) {
		this.noticeType = noticeType;
	}

	public long getMinTime() {
		return minTime;
	}

	public void setMinTime(long minTime) {
		this.minTime = minTime;
	}

	@Override
	public String toString() {
		return "NoticeInfoReq [deviceId=" + deviceId + ", noticeType="
				+ noticeType + ", minTime=" + minTime + "]";
	}

}
