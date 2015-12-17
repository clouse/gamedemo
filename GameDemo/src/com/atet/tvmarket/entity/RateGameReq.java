package com.atet.tvmarket.entity;

public class RateGameReq implements AutoType {
	private String userId;
	private String deviceId;
	private String gameId;
	private int score;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	@Override
	public String toString() {
		return "RateGameReq [userId=" + userId + ", deviceId=" + deviceId
				+ ", gameId=" + gameId + ", score=" + score + "]";
	}

}
