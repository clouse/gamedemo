package com.atet.tvmarket.entity;

public class GoodsIdReq implements AutoType {
	private String goodsId;

	public String getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}

	@Override
	public String toString() {
		return "GoodsIdReq [goodsId=" + goodsId + "]";
	}

}
