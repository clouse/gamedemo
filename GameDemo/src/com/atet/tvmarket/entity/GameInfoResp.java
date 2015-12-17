package com.atet.tvmarket.entity;

import java.util.List;

import com.atet.tvmarket.entity.dao.GameInfo;

public class GameInfoResp implements AutoType {
	private int code;
	private int total;
	private List<GameInfo> data;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public List<GameInfo> getData() {
		return data;
	}

	public void setData(List<GameInfo> data) {
		this.data = data;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	@Override
	public String toString() {
		return "GameInfoResp [code=" + code + ", total=" + total + ", data="
				+ data + "]";
	}

}