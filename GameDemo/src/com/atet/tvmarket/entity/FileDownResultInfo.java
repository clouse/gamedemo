package com.atet.tvmarket.entity;

public class FileDownResultInfo implements AutoType{
	//文件下载的路径
	private String path;

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@Override
	public String toString() {
		return "FileDownResultInfo [path=" + path + "]";
	}
}
