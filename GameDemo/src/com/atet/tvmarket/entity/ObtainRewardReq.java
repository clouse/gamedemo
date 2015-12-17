package com.atet.tvmarket.entity;

public class ObtainRewardReq implements AutoType {
	private int userId;
	private String rewardId;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getRewardId() {
		return rewardId;
	}

	public void setRewardId(String rewardId) {
		this.rewardId = rewardId;
	}

	@Override
	public String toString() {
		return "ObtainRewardReq [userId=" + userId + ", rewardId=" + rewardId
				+ "]";
	}
}