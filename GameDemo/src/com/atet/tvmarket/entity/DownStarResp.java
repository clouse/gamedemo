package com.atet.tvmarket.entity;

import java.util.List;

public class DownStarResp implements AutoType {
	private int code;
	private int total;
	private List<DownStarCount> data;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public List<DownStarCount> getData() {
		return data;
	}

	public void setData(List<DownStarCount> data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "DownStarResp [code=" + code + ", total=" + total + ", data="
				+ data + "]";
	}

	public static class DownStarCount implements AutoType {
		private String gameId;
		private int downCount;
		private Double startLevel;

		public String getGameId() {
			return gameId;
		}

		public void setGameId(String gameId) {
			this.gameId = gameId;
		}

		public int getDownCount() {
			return downCount;
		}

		public void setDownCount(int downCount) {
			this.downCount = downCount;
		}

		public Double getStartLevel() {
			return startLevel;
		}

		public void setStartLevel(Double startLevel) {
			this.startLevel = startLevel;
		}

		@Override
		public String toString() {
			return "DownStarCount [gameId=" + gameId + ", downCount="
					+ downCount + ", startLevel=" + startLevel + "]";
		}

	}
}