package com.atet.tvmarket.entity;

public class UserGetCaptchaReq {
	private String phoneNum;
	private int type;

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "UserGetCaptchaReq [phoneNum=" + phoneNum + ", type=" + type
				+ "]";
	}

}
