package com.atet.tvmarket.entity;

public class GameInfoFromGameIdReq implements AutoType {
	private String deviceId;
	private String gameId;

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

	@Override
	public String toString() {
		return "GameInfoFromGameIdReq [deviceId=" + deviceId + ", gameId="
				+ gameId + "]";
	}
}
