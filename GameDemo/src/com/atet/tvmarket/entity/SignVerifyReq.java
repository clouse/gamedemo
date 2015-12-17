package com.atet.tvmarket.entity;

public class SignVerifyReq implements AutoType{
	private String data;
	private String sign;

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	@Override
	public String toString() {
		return "VerifyReq [data=" + data + ", sign=" + sign + "]";
	}

}
