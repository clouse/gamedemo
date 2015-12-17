package com.atet.tvmarket.entity;

import java.util.List;

import com.atet.tvmarket.entity.dao.GameInfo;
import com.atet.tvmarket.entity.dao.ThirdGameInfo;

/**
 * @description:
 *
 * @author: LiuQin
 * @date: 2015年7月15日 下午3:54:57
 */
public class ThirdGameInfosFromGameTopicResp implements AutoType {
	private int code;
	private String name;
	private String remark;
	private List<ThirdGameInfo> games;
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public long getCurrentTime() {
		return currentTime;
	}

	public void setCurrentTime(long currentTime) {
		this.currentTime = currentTime;
	}

	public List<ThirdGameInfo> getGames() {
		return games;
	}

	public void setGames(List<ThirdGameInfo> games) {
		this.games = games;
	}

	@Override
	public String toString() {
		return "ThirdGameInfosFromGameTopicResp [code=" + code + ", name="
				+ name + ", remark=" + remark + ", games=" + games
				+ ", currentTime=" + currentTime + "]";
	}

}