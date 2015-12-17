package com.atet.tvmarket.entity;

/**
 * @description: 用户注册请求信息
 *
 * @author: LiuQin
 * @date: 2015年8月2日 下午4:00:40
 */
public class UserLoginRegisterReq {
	private String phoneNum;
	private String password;
	private String captcha;

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getCaptcha() {
		return captcha;
	}

	public void setCaptcha(String captcha) {
		this.captcha = captcha;
	}

	@Override
	public String toString() {
		return "UserRegisterReq [phoneNum=" + phoneNum + ", password="
				+ password + ", captcha=" + captcha + "]";
	}

}
