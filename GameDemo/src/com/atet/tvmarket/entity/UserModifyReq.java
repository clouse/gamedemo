package com.atet.tvmarket.entity;

public class UserModifyReq {
	private String newPhone;
	private String oldPassword;
	private String newPassword;
	private String captcha;
	private int userId;
	private int flat;

	public String getNewPhone() {
		return newPhone;
	}

	public void setNewPhone(String newPhone) {
		this.newPhone = newPhone;
	}

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getCaptcha() {
		return captcha;
	}

	public void setCaptcha(String captcha) {
		this.captcha = captcha;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getFlat() {
		return flat;
	}

	public void setFlat(int flat) {
		this.flat = flat;
	}

	@Override
	public String toString() {
		return "UserModifyReq [newPhone=" + newPhone + ", oldPassword="
				+ oldPassword + ", newPassword=" + newPassword + ", captcha="
				+ captcha + ", userId=" + userId + ", flat=" + flat + "]";
	}
}
