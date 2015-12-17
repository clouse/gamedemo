package com.atet.tvmarket.entity;

import java.util.List;

import com.atet.tvmarket.entity.dao.ActInfo;

public class ActivityInfoResp implements AutoType {
	private int code;
	private int total;
	private long lastUpdateTime;
	private List<ActInfo> data;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public List<ActInfo> getData() {
		return data;
	}

	public void setData(List<ActInfo> data) {
		this.data = data;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public long getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(long lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	@Override
	public String toString() {
		return "ActivityInfoResp [code=" + code + ", total=" + total
				+ ", lastUpdateTime=" + lastUpdateTime + ", data=" + data + "]";
	}

}