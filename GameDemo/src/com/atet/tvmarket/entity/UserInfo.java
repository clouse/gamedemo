package com.atet.tvmarket.entity;

/**
 * @description: 注册用户信息
 *
 * @author: LiuQin
 * @date: 2015年8月2日 下午4:00:40 
 */
public class UserInfo implements AutoType{
	// 用户id
	private int userId;
	// 用户名称，atet用户为手机号，微信用户为其昵称
	private String userName;
	// 手机号,微信时可能为null
	private String phoneNum;
	// 用户头像,可能为null
	private String icon;
	// 积分
	private int integral;
	// 余额
	private int balance;
	// A币
	private int coupon;
	
	public UserInfo(){
		
	}
	
	public UserInfo(int userId, String userName, String phoneNum, String icon,
			Integer integral, Integer balance, Integer coupon) {
		super();
		
		if (userName == null) {
			userName = "";
		}
		if (phoneNum == null) {
			phoneNum = "";
		}
		if (integral == null) {
			integral = 0;
		}
		if (balance == null) {
			balance = 0;
		}
		if (coupon == null) {
			coupon = 0;
		}
		
		this.userId = userId;
		this.userName = userName;
		this.phoneNum = phoneNum;
		this.icon = icon;
		this.integral = integral;
		this.balance = balance;
		this.coupon = coupon;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public int getIntegral() {
		return integral;
	}

	public void setIntegral(int integral) {
		this.integral = integral;
	}

	public int getBalance() {
		return balance;
	}

	public void setBalance(int balance) {
		this.balance = balance;
	}

	public int getCoupon() {
		return coupon;
	}

	public void setCoupon(int coupon) {
		this.coupon = coupon;
	}

	@Override
	public String toString() {
		return "UserInfo [userId=" + userId + ", userName=" + userName
				+ ", phoneNum=" + phoneNum + ", icon=" + icon + ", integral="
				+ integral + ", balance=" + balance + ", coupon=" + coupon
				+ "]";
	}
}
