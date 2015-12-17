package com.atet.tvmarket.entity;

import java.util.List;

import com.atet.tvmarket.entity.dao.ActInfo;
import com.atet.tvmarket.entity.dao.GameGiftInfo;

public class GameGiftInfoResp implements AutoType {
	private int code;
	private long lastUpdateTime;
	private List<GameGiftInfo> data;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public List<GameGiftInfo> getData() {
		return data;
	}

	public void setData(List<GameGiftInfo> data) {
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
		return "GameGiftInfoResp [code=" + code + ", lastUpdateTime="
				+ lastUpdateTime + ", data=" + data + "]";
	}

	
}