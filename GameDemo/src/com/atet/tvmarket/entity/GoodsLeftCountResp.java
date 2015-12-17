package com.atet.tvmarket.entity;

import java.util.List;

public class GoodsLeftCountResp implements AutoType{
	private int code;
	private List<GoodsLeftCountInfo> data;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public List<GoodsLeftCountInfo> getData() {
		return data;
	}

	public void setData(List<GoodsLeftCountInfo> data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "GoodsLeftCountResp [code=" + code + ", data=" + data + "]";
	}

	public static class GoodsLeftCountInfo implements AutoType{
		private String goodsId;
		private int surplus;
		private int code;

		public String getGoodsId() {
			return goodsId;
		}

		public void setGoodsId(String goodsId) {
			this.goodsId = goodsId;
		}

		public int getSurplus() {
			return surplus;
		}

		public void setSurplus(int surplus) {
			this.surplus = surplus;
		}

		public int getCode() {
			return code;
		}

		public void setCode(int code) {
			this.code = code;
		}

		@Override
		public String toString() {
			return "GoodsLeftCountInfo [goodsId=" + goodsId + ", surplus=" + surplus + ", code=" + code + "]";
		}
	}

}