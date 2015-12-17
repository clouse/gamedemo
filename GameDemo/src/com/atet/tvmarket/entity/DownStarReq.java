package com.atet.tvmarket.entity;

public class DownStarReq implements AutoType {
	private String deviceId;
	private int pageSize;
	private int currentPage;

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	@Override
	public String toString() {
		return "DownStarReq [deviceId=" + deviceId + ", pageSize=" + pageSize
				+ ", currentPage=" + currentPage + "]";
	}

}
