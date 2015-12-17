package com.atet.tvmarket.entity;

/**
 * @description:
 *
 * @author: LiuQin
 * @date: 2015年7月29日 下午7:13:30
 */
public class UserGoodsOrderInfo implements AutoType {
	private String orderId;
	private String goodsId;
	private String goodsName;
	private String photo;
	private int integral;
	private int price;
	private String name;
	private String phone;
	private String wechat;
	private String address;
	private long tradeTime;
	private int tradeModel;
	private int isDiscount;
	private double discount;
	private String express;
	private String expressNo;
	private int count;
	private int actualIntegral;
	private int actualPrice;
	private long createTime;
	private int state;

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public int getIntegral() {
		return integral;
	}

	public void setIntegral(int integral) {
		this.integral = integral;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getWechat() {
		return wechat;
	}

	public void setWechat(String wechat) {
		this.wechat = wechat;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public long getTradeTime() {
		return tradeTime;
	}

	public void setTradeTime(long tradeTime) {
		this.tradeTime = tradeTime;
	}

	public int getTradeModel() {
		return tradeModel;
	}

	public void setTradeModel(int tradeModel) {
		this.tradeModel = tradeModel;
	}

	public int getIsDiscount() {
		return isDiscount;
	}

	public void setIsDiscount(int isDiscount) {
		this.isDiscount = isDiscount;
	}

	public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}

	public String getExpress() {
		return express;
	}

	public void setExpress(String express) {
		this.express = express;
	}

	public String getExpressNo() {
		return expressNo;
	}

	public void setExpressNo(String expressNo) {
		this.expressNo = expressNo;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getActualIntegral() {
		return actualIntegral;
	}

	public void setActualIntegral(int actualIntegral) {
		this.actualIntegral = actualIntegral;
	}

	public int getActualPrice() {
		return actualPrice;
	}

	public void setActualPrice(int actualPrice) {
		this.actualPrice = actualPrice;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	@Override
	public String toString() {
		return "UserGoodsOrderInfo [orderId=" + orderId + ", goodsId="
				+ goodsId + ", goodsName=" + goodsName + ", photo=" + photo
				+ ", integral=" + integral + ", price=" + price + ", name="
				+ name + ", phone=" + phone + ", wechat=" + wechat
				+ ", address=" + address + ", tradeTime=" + tradeTime
				+ ", tradeModel=" + tradeModel + ", isDiscount=" + isDiscount
				+ ", discount=" + discount + ", express=" + express
				+ ", expressNo=" + expressNo + ", count=" + count
				+ ", actualIntegral=" + actualIntegral + ", actualPrice="
				+ actualPrice + ", createTime=" + createTime + ", state="
				+ state + "]";
	}
}