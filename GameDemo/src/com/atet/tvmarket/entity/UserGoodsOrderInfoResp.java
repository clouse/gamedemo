package com.atet.tvmarket.entity;

import java.util.List;

import com.atet.tvmarket.entity.dao.ActInfo;

public class UserGoodsOrderInfoResp implements AutoType {
	private int code;
	private String userId;
	private List<UserGoodsOrderInfo> data;
	private long elapsedRealTime;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public List<UserGoodsOrderInfo> getData() {
		return data;
	}

	public void setData(List<UserGoodsOrderInfo> data) {
		this.data = data;
	}

	public long getElapsedRealTime() {
		return elapsedRealTime;
	}

	public void setElapsedRealTime(long elapsedRealTime) {
		this.elapsedRealTime = elapsedRealTime;
	}

	@Override
	public String toString() {
		return "UserGoodsOrderInfoResp [code=" + code + ", userId=" + userId
				+ ", data=" + data + "]";
	}

}