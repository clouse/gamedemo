package com.atet.tvmarket.entity;

import java.util.List;

import com.atet.tvmarket.entity.dao.UpdateInterfaceInfo;

public class UpdateInterfaceResp implements AutoType {
	private int code;
	private List<UpdateInterfaceInfo> data;
	private long lastTime;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public List<UpdateInterfaceInfo> getData() {
		return data;
	}

	public void setData(List<UpdateInterfaceInfo> data) {
		this.data = data;
	}

	public long getLastTime() {
		return lastTime;
	}

	public void setLastTime(long lastTime) {
		this.lastTime = lastTime;
	}

	@Override
	public String toString() {
		return "UpdateInterfaceResp [code=" + code + ", data=" + data
				+ ", lastTime=" + lastTime + "]";
	}
}
