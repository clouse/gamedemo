package com.atet.tvmarket.entity;

import java.util.List;

import com.atet.tvmarket.entity.dao.GameSearchPinyinInfo;

public class GameSearchPinyinResp implements AutoType {
	private int code;
	private List<GameSearchPinyinInfo> data;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public List<GameSearchPinyinInfo> getData() {
		return data;
	}

	public void setData(List<GameSearchPinyinInfo> data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "GameSearchPinyinResp [code=" + code + ", data=" + data + "]";
	}
}
