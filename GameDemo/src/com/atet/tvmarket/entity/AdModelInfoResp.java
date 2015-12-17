package com.atet.tvmarket.entity;

import java.util.List;

import com.atet.tvmarket.entity.dao.AdModelInfo;

public class AdModelInfoResp implements AutoType {
	private int code;
	private List<AdModelInfo> data;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public List<AdModelInfo> getData() {
		return data;
	}

	public void setData(List<AdModelInfo> data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "AdModelInfoResp [code=" + code + ", data=" + data + "]";
	}

}