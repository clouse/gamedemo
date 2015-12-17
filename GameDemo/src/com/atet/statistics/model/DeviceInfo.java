package com.atet.statistics.model;

import java.io.Serializable;

import com.atet.tvmarket.entity.AutoType;
import com.atet.tvmarket.model.database.BaseModel;
import com.atet.tvmarket.model.database.TableDescription;
@TableDescription(name="deviceInfo")
public class DeviceInfo extends BaseModel implements AutoType, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String deviceId;// 设备在服务器数据库中对应的ID
	private String channelId;// 渠道ID
	private Integer type;// 平台类型,1为TV,2为手机
    private int capability;

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public int getCapability() {
		return capability;
	}

	public void setCapability(int capability) {
		this.capability = capability;
	}

	@Override
	public String toString() {
		return "DeviceInfo [deviceId=" + deviceId + ", channelId=" + channelId
				+ ", type=" + type + ", capability=" + capability + "]";
	}
}
