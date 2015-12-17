package com.atet.statistics.model;

import com.atet.tvmarket.entity.AutoType;

public class GamePadInfo implements AutoType{
	
	private String mac;
	
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	@Override
	public String toString() {
		return "GamePadInfo [mac=" + mac + ", name=" + name + "]";
	}
	
	
}
