package com.atet.tvmarket.entity;

import java.util.List;

import com.atet.tvmarket.entity.dao.UserGiftInfo;


public class UserGiftInfoResp implements AutoType {
	private int code;
	private List<UserGiftInfo> data;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public List<UserGiftInfo> getData() {
		return data;
	}

	public void setData(List<UserGiftInfo> data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "UserGiftInfoResp [code=" + code + ", data=" + data + "]";
	}

}