package com.atet.tvmarket.entity;

import java.util.List;

import com.atet.tvmarket.entity.dao.NoticeInfo;
import com.atet.tvmarket.entity.dao.VideoInfo;

public class VideoInfoResp implements AutoType {
	private int code;
	private List<VideoInfo> data;
	private long lastUpdateTime;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public List<VideoInfo> getData() {
		return data;
	}

	public void setData(List<VideoInfo> data) {
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
		return "VideoInfoResp [code=" + code + ", data=" + data
				+ ", lastUpdateTime=" + lastUpdateTime + "]";
	}

}