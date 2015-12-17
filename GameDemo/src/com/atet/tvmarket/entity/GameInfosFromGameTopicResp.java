package com.atet.tvmarket.entity;

import java.util.List;

import com.atet.tvmarket.entity.dao.GameInfo;
import com.atet.tvmarket.entity.dao.GameTypeInfo;

/**
 * @description: 
 *
 * @author: LiuQin
 * @date: 2015年7月14日 下午3:54:29 
 */
public class GameInfosFromGameTopicResp implements AutoType {
	private int code;
	private String name;
	private String remark;
	private List<GameInfo> games;
	private long currentTime;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<GameInfo> getGames() {
		return games;
	}

	public void setGames(List<GameInfo> games) {
		this.games = games;
	}

	public long getCurrentTime() {
		return currentTime;
	}

	public void setCurrentTime(long currentTime) {
		this.currentTime = currentTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Override
	public String toString() {
		return "GameInfosFromGameTopicResp [code=" + code + ", name=" + name
				+ ", remark=" + remark + ", games=" + games + ", currentTime="
				+ currentTime + "]";
	}
}