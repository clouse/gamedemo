package com.atet.tvmarket.entity;

public class UpdateInterfaceInner implements AutoType {
	private String key;
	private long time;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	@Override
	public String toString() {
		return "UpdateInterfaceInner [key=" + key + ", time=" + time + "]";
	}

}
