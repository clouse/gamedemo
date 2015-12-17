package com.atet.tvmarket.entity;

public class GameInfosFromGameTypeReq implements AutoType {
	private String userId;
	private String deviceId;
	private String typeId;
	private int pageSize;
	private int currentPage;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
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
		return "GameInfosFromGameTypeReq [userId=" + userId + ", deviceId="
				+ deviceId + ", typeId=" + typeId + ", pageSize=" + pageSize
				+ ", currentPage=" + currentPage + "]";
	}

}
