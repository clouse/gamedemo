package com.atet.tvmarket.entity;

import java.util.List;

import com.atet.tvmarket.entity.dao.ActInfo;
import com.atet.tvmarket.entity.dao.GoodsInfo;

public class GoodsInfoResp implements AutoType {
	private int code;
	private int total;
	private long lastUpdateTime;
	private List<GoodsInfo> data;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public List<GoodsInfo> getData() {
		return data;
	}

	public void setData(List<GoodsInfo> data) {
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
		return "GoodsInfoResp [code=" + code + ", total=" + total
				+ ", lastUpdateTime=" + lastUpdateTime + ", data=" + data + "]";
	}

}