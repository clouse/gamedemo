package com.atet.tvmarket.entity;


/**
 * @description: server id返回实体类 
 *
 * @author: LiuQin
 * @date: 2015年7月4日 下午2:17:28 
 */
/**
 * @description: 
 *
 * @author: LiuQin
 * @date: 2015年8月21日 下午3:40:01 
 */
public class ServerIdInfo implements AutoType {
    private int code;
    private String deviceId;
    private String channelId;
    private int type;
    private int capability;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

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

    public int getType() {
        return type;
    }

    public void setType(int type) {
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
		return "ServerIdInfo [code=" + code + ", deviceId=" + deviceId
				+ ", channelId=" + channelId + ", type=" + type
				+ ", capability=" + capability + "]";
	}
}
