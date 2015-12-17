package com.atet.tvmarket.entity;

import java.util.List;

public class AppConfigResp implements AutoType {
	private int code;
	private int startUp;
	private List<String> modules;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public int getStartUp() {
		return startUp;
	}

	public void setStartUp(int startUp) {
		this.startUp = startUp;
	}

	public List<String> getModules() {
		return modules;
	}

	public void setModules(List<String> modules) {
		this.modules = modules;
	}

	@Override
	public String toString() {
		return "AppConfigResp [code=" + code + ", startUp=" + startUp
				+ ", modules=" + modules + "]";
	}

}