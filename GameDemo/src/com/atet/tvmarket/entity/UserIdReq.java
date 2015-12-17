package com.atet.tvmarket.entity;

public class UserIdReq implements AutoType{
	private String userId;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return "UserIdReq [userId=" + userId + "]";
	}
}
