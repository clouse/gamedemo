package com.atet.tvmarket.entity;


/**
 * @Description: 查询新版本
 * @author: Liuqin
 * @date 2013-1-7 上午9:29:32
 * 
 */
public class NewVersionInfoReq implements AutoType{
    private String deviceId;
    private String appId;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    @Override
    public String toString() {
        return "ReqGetVersionInfo [deviceId=" + deviceId + ", appId=" + appId
                + "]";
    }

}
