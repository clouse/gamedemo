package com.atet.tvmarket.entity;

import java.util.List;

import com.atet.tvmarket.entity.dao.NoticeInfo;

public class NoticeInfoResp implements AutoType {
	private int code;
	private List<NoticeInfo> data;
	private long lastUpdateTime;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public List<NoticeInfo> getData() {
		return data;
	}

	public void setData(List<NoticeInfo> data) {
		this.data = data;
	}

	public long getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(long lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	@Override
	public String toString() {
		return "NoticeInfoResp [code=" + code + ", data=" + data
				+ ", lastUpdateTime=" + lastUpdateTime + "]";
	}

}