package com.atet.tvmarket.entity;

/**
 * @description:
 *
 * @author: LiuQin
 * @date: 2015年7月30日 上午11:33:51
 */
public class GoodsExchangeResp implements AutoType {
	private int userId;
	private String orderId;
	private int integral;
	private String coupon;
	private int code;
	private String desc;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public int getIntegral() {
		return integral;
	}

	public void setIntegral(int integral) {
		this.integral = integral;
	}

	public String getCoupon() {
		return coupon;
	}

	public void setCoupon(String coupon) {
		this.coupon = coupon;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	@Override
	public String toString() {
		return "GoodsExchangeResp [userId=" + userId + ", orderId=" + orderId
				+ ", integral=" + integral + ", coupon=" + coupon + ", code="
				+ code + ", desc=" + desc + "]";
	}
}
