package com.atet.tvmarket.entity;

public class SyncTimeResp implements AutoType {
	private int code;
	private long time;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	@Override
	public String toString() {
		return "SyncTimeResp [code=" + code + ", time=" + time + "]";
	}

}
