package com.atet.tvmarket.entity;

/**
 * @description: server id请求实体类
 *
 * @author: LiuQin
 * @date: 2015年7月4日 下午2:17:00 
 */
public class ServerIdInfoReq implements AutoType {
    private String deviceCode;
    private String productId;
    private String channelId;
    private int type;

    public String getDeviceCode() {
        return deviceCode;
    }

    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
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

    @Override
    public String toString() {
        return "GetDeviceIdReq [deviceCode=" + deviceCode + ", productId="
                + productId + ", channelId=" + channelId + ", type=" + type
                + "]";
    }
}
