package com.atet.tvmarket.entity;

public class UserTaskReq implements AutoType{
	private int userId;
	private int flag;
	private String deviceId;
	private String taskId;
	private String ruleId;
	private String gameId;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
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

	@Override
	public String toString() {
		return "UserTaskReq [userId=" + userId + ", flag=" + flag
				+ ", deviceId=" + deviceId + ", taskId=" + taskId + ", ruleId="
				+ ruleId + ", gameId=" + gameId + "]";
	}
}
