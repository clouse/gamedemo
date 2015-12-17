package com.atet.tvmarket.entity;

import java.util.List;

import com.atet.tvmarket.entity.dao.GameTypeInfo;


public class GameTypeInfoResp implements AutoType{
	private int code;
	private List<GameTypeInfo> data;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public List<GameTypeInfo> getData() {
		return data;
	}

	public void setData(List<GameTypeInfo> data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "GameTypeInfoResp [code=" + code + ", data=" + data + "]";
	}
}