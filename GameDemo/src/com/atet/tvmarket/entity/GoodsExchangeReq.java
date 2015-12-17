package com.atet.tvmarket.entity;

public class GoodsExchangeReq implements AutoType {
	private int userId;
	private String goodsId;
	private int count;

	public String getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return "GoodsExchangeReq [userId=" + userId + ", goodsId=" + goodsId
				+ ", count=" + count + "]";
	}
}
