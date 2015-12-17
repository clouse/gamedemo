package com.atet.tvmarket.entity;

public class Resp implements AutoType {
	private int code;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	@Override
	public String toString() {
		return "Resp [code=" + code + "]";
	}
}
