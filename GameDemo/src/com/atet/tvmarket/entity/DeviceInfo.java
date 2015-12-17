package com.atet.tvmarket.entity;

public class DeviceInfo {
	private String serverId;
	private String channelId;
	private String atetId;
	private String deviceModel;
	private String deviceUniqueId;

	public String getServerId() {
		return serverId;
	}

	public void setServerId(String serverId) {
		this.serverId = serverId;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getAtetId() {
		return atetId;
	}

	public void setAtetId(String atetId) {
		this.atetId = atetId;
	}

	public String getDeviceModel() {
		return deviceModel;
	}

	public void setDeviceModel(String deviceModel) {
		this.deviceModel = deviceModel;
	}

	public String getDeviceUniqueId() {
		return deviceUniqueId;
	}

	public void setDeviceUniqueId(String deviceUniqueId) {
		this.deviceUniqueId = deviceUniqueId;
	}

	@Override
	public String toString() {
		return "DeviceInfo [serverId=" + serverId + ", channelId=" + channelId
				+ ", atetId=" + atetId + ", deviceModel=" + deviceModel
				+ ", deviceUniqueId=" + deviceUniqueId + "]";
	}

}
