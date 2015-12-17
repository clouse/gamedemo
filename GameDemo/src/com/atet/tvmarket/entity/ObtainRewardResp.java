package com.atet.tvmarket.entity;


public class ObtainRewardResp implements AutoType {
	private int code;
	private String desc;

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
		return "ObtainRewardResp [code=" + code + ", desc=" + desc + "]";
	}
}