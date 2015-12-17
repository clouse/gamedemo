package com.atet.tvmarket.entity;

import java.util.List;

import com.atet.tvmarket.entity.dao.ThirdGameInfo;

public class ThirdGameInfoResp implements AutoType {
	private int code;
	private List<ThirdGameInfo> data;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public List<ThirdGameInfo> getData() {
		return data;
	}

	public void setData(List<ThirdGameInfo> data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "ThirdGameInfoResp [code=" + code + ", data=" + data + "]";
	}
}