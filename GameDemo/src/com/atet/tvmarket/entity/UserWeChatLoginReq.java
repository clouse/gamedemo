package com.atet.tvmarket.entity;

public class UserWeChatLoginReq {
	private int userId;
	private String username;
	private String token;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	@Override
	public String toString() {
		return "WeChatLoginReq [userId=" + userId + ", username=" + username
				+ ", token=" + token + "]";
	}
}
