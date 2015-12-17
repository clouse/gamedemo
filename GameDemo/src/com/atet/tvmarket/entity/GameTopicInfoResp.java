package com.atet.tvmarket.entity;

import java.util.List;

import com.atet.tvmarket.entity.dao.GameTopicInfo;
import com.atet.tvmarket.entity.dao.GameTypeInfo;


public class GameTopicInfoResp implements AutoType{
	private int code;
	private List<GameTopicInfo> data;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public List<GameTopicInfo> getData() {
		return data;
	}

	public void setData(List<GameTopicInfo> data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "GameTopicInfoResp [code=" + code + ", data=" + data + "]";
	}
}