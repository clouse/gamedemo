package com.atet.tvmarket.entity;

import java.io.Serializable;

public class PayInfo implements AutoType,Serializable,Cloneable{
    private static final long serialVersionUID = 1L;
    
    private int payId;
    
    //App key,cp传入
    private String appkey;
    //App ID,cp传入
    private String appid;
    //商户id，cp传入
    private String partnerid;
    //cp ID,cp传入
    private String cpid;
    //回调地址,cp传入
    private String notifyurl;
    //包名，cp传入
    private String packageName;
    
    //外部订单号,cp传入
    private String exorderno;
    //支付点编码,cp传入
    private String waresid;
    //--支付点名称,cp传入
    private String waresname;
    //单价,cp传入
    private int price;
    //数量,cp传入
    private int quantity;
    //交易币种,cp传入
    private int amountType;
    //cp传入的字串，支付完成后原封传回
    private String cpprivateinfo;
    //cp传入,兼容爱贝，用户角色相关
    private String appuserid;
    
    //用户昵称,获取 
    private String userName;
    //用户id,获取
    private int userId;
    //渠道id,获取
    private String channelId;
    //device id,获取
    private String deviceId;
    //device code,获取
    private String deviceCode;
    //设备类型,获取
    private int deviceType;
    //支付渠道:快钱、支付宝等，获取
    private int payType;
    //服务器根据device id 和device code返回的用于请求的id,相当于设备型号的作用
    private String serverId;
    //服务器生成的订单号
    private String serverOrderno;
    //atet id,服务器生成的设备唯一id
    private String atetId;
    
    private boolean login;
    
    //是否显示银行卡支付
    private boolean isBankcard;
    //是否显示联通支付
    private boolean isUnicom;
    //是否显示支付宝支付
    private boolean isAlipay;
    
    private int orientation;
    
    private String gameName;
    
    
    
    public boolean isBankcard() {
        return isBankcard;
    }
    public void setBankcard(boolean isBankcard) {
        this.isBankcard = isBankcard;
    }
    public boolean isUnicom() {
        return isUnicom;
    }
    public void setUnicom(boolean isUnicom) {
        this.isUnicom = isUnicom;
    }
    
    public String getAppkey() {
        return appkey;
    }
    public void setAppkey(String appkey) {
        this.appkey = appkey;
    }
    public String getAppid() {
        return appid;
    }
    public void setAppid(String appid) {
        this.appid = appid;
    }
    public String getNotifyurl() {
        return notifyurl;
    }
    public void setNotifyurl(String notifyurl) {
        this.notifyurl = notifyurl;
    }
    public String getPartnerid() {
        return partnerid;
    }
    public void setPartnerid(String partnerid) {
        this.partnerid = partnerid;
    }
    public String getCpid() {
        return cpid;
    }
    public void setCpid(String cpid) {
        this.cpid = cpid;
    }
    public String getPackageName() {
        return packageName;
    }
    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
    public String getExorderno() {
        return exorderno;
    }
    public void setExorderno(String exorderno) {
        this.exorderno = exorderno;
    }
    public String getWaresid() {
        return waresid;
    }
    public void setWaresid(String waresid) {
        this.waresid = waresid;
    }
    public String getWaresname() {
        return waresname;
    }
    public void setWaresName(String waresname) {
        this.waresname = waresname;
    }
    public int getPrice() {
        return price;
    }
    public void setPrice(int price) {
        this.price = price;
    }
    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public int getAmountType() {
        return amountType;
    }
    public void setAmountType(int amountType) {
        this.amountType = amountType;
    }
    public String getCpprivateinfo() {
        return cpprivateinfo;
    }
    public void setCpprivateinfo(String cpprivateinfo) {
        this.cpprivateinfo = cpprivateinfo;
    }
    public String getAppuserid() {
        return appuserid;
    }
    public void setAppuserid(String appuserid) {
        this.appuserid = appuserid;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
    public String getChannelId() {
        return channelId;
    }
    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }
    public String getDeviceId() {
        return deviceId;
    }
    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
    public String getDeviceCode() {
        return deviceCode;
    }
    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
    }
    public int getDeviceType() {
        return deviceType;
    }
    public void setDeviceType(int deviceType) {
        this.deviceType = deviceType;
    }
    public int getPayType() {
        return payType;
    }
    public void setPayType(int payType) {
        this.payType = payType;
    }
    
    @Override
	public Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
        PayInfo obj = null;  
        try {  
            obj = (PayInfo) super.clone();  
        } catch (CloneNotSupportedException e) {  
            e.printStackTrace();  
        }  
        return obj;  
    }
    public String getServerId() {
        return serverId;
    }
    public void setServerId(String serverId) {
        this.serverId = serverId;
    }
    public String getServerOrderno() {
        return serverOrderno;
    }
    public void setServerOrderno(String serverOrderno) {
        this.serverOrderno = serverOrderno;
    }

    public boolean isLogin() {
        return login;
    }
    public void setLogin(boolean login) {
        this.login = login;
    }
    public boolean isAlipay() {
        return isAlipay;
    }
    public void setAlipay(boolean isAlipay) {
        this.isAlipay = isAlipay;
    }
    public int getOrientation() {
        return orientation;
    }
    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }
    public String getGameName() {
        return gameName;
    }
    public void setGameName(String gameName) {
        this.gameName = gameName;
    }
    public int getPayId() {
        return payId;
    }
    public void setPayId(int payId) {
        this.payId = payId;
    }
    public String getAtetId() {
        return atetId;
    }
    public void setAtetId(String atetId) {
        this.atetId = atetId;
    }
    @Override
    public String toString() {
        return "PayInfo [payId=" + payId + ", appkey=" + appkey + ", appid="
                + appid + ", partnerid=" + partnerid + ", cpid=" + cpid
                + ", notifyurl=" + notifyurl + ", packageName=" + packageName
                + ", exorderno=" + exorderno + ", waresid=" + waresid
                + ", waresname=" + waresname + ", price=" + price
                + ", quantity=" + quantity + ", amountType=" + amountType
                + ", cpprivateinfo=" + cpprivateinfo + ", appuserid="
                + appuserid + ", userName=" + userName + ", userId=" + userId
                + ", channelId=" + channelId + ", deviceId=" + deviceId
                + ", deviceCode=" + deviceCode + ", deviceType=" + deviceType
                + ", payType=" + payType + ", serverId=" + serverId
                + ", serverOrderno=" + serverOrderno + ", atetId=" + atetId
                + ", login=" + login + ", isBankcard=" + isBankcard
                + ", isUnicom=" + isUnicom + ", isAlipay=" + isAlipay
                + ", orientation=" + orientation + ", gameName=" + gameName
                + "]";
    }
}
