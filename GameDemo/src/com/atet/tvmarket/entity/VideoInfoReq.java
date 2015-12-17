package com.atet.tvmarket.entity;

public class VideoInfoReq implements AutoType {
	private String deviceId;
	private String videoType;
	private String currentPage;
	private String pageSize;

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getVideoType() {
		return videoType;
	}

	public void setVideoType(String videoType) {
		this.videoType = videoType;
	}

	public String getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(String currentPage) {
		this.currentPage = currentPage;
	}

	public String getPageSize() {
		return pageSize;
	}

	public void setPageSize(String pageSize) {
		this.pageSize = pageSize;
	}

	@Override
	public String toString() {
		return "VideoInfoReq [deviceId=" + deviceId + ", videoType="
				+ videoType + ", currentPage=" + currentPage + ", pageSize="
				+ pageSize + "]";
	}
}
