package com.atet.tvmarket.entity;

import java.util.List;

import com.atet.tvmarket.entity.dao.GameInfo;
import com.atet.tvmarket.entity.dao.GameTypeInfo;

public class GameInfosFromGameTopicRespBak implements AutoType {
	private int code;
	private Inner data;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public Inner getData() {
		return data;
	}

	public void setData(Inner data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "GameTypeInfoResp [code=" + code + ", data=" + data + "]";
	}

	public static class Inner {
		private String name;
		private String remark;
		private List<GameInfo> games;

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

		public List<GameInfo> getGames() {
			return games;
		}

		public void setGames(List<GameInfo> games) {
			this.games = games;
		}

		@Override
		public String toString() {
			return "Inner [name=" + name + ", remark=" + remark + ", games="
					+ games + "]";
		}
	}
}