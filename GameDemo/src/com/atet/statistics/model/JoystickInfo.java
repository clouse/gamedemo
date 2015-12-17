package com.atet.statistics.model;

import com.atet.tvmarket.entity.AutoType;

public class JoystickInfo implements AutoType{
	
	public final static int TYPE_GAMEPAD = 0;
	public final static int TYPE_JOYSTICK = 1;
	private String mac;
	private String productId;
	private String manufactorId;
	private String handlerName;
	
	private int type;
	
	public String getManufactorId() {
		return manufactorId;
	}
	public void setManufactorId(String manufactorId) {
		this.manufactorId = manufactorId;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}
	
	
	@Override
	public boolean equals(Object o) {
		
		if (!(o instanceof JoystickInfo)){
			return false;
		}
		JoystickInfo stick = (JoystickInfo) o;
		return stick.getMac().equals(mac)&&stick.getProductId().equals(productId)&&stick.equals(manufactorId);
	}
	
	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return mac.hashCode()+productId.hashCode()+manufactorId.hashCode();
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getHandlerName() {
		return handlerName;
	}
	public void setHandlerName(String handlerName) {
		this.handlerName = handlerName;
	}
	@Override
	public String toString() {
		return "JoystickInfo [mac=" + mac + ", productId=" + productId
				+ ", manufactorId=" + manufactorId + ", handlerName="
				+ handlerName + ", type=" + type + "]";
	}
	
}
