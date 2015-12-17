package com.atet.tvmarket.entity;


public class ObtainUserGiftResp implements AutoType {
	private int code;
	private String giftCode;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getGiftCode() {
		return giftCode;
	}

	public void setGiftCode(String giftCode) {
		this.giftCode = giftCode;
	}

	@Override
	public String toString() {
		return "GetUserGiftResp [code=" + code + ", giftCode=" + giftCode + "]";
	}

}