package com.atet.tvmarket.entity;

public class RateGameResp implements AutoType {
	private int code;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	@Override
	public String toString() {
		return "RateGameResp [code=" + code + "]";
	}
}