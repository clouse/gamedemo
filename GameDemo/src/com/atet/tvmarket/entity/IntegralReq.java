package com.atet.tvmarket.entity;

public class IntegralReq implements AutoType {
	private String deviceId;
	private int userId;
	private String taskId;
	private String ruleId;
	private String gameId;
	private int flag;

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getRuleId() {
		return ruleId;
	}

	public void setRuleId(String ruleId) {
		this.ruleId = ruleId;
	}

	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	@Override
	public String toString() {
		return "IntegralReq [deviceId=" + deviceId + ", userId=" + userId
				+ ", taskId=" + taskId + ", ruleId=" + ruleId + ", gameId="
				+ gameId + ", flag=" + flag + "]";
	}

}
