package com.atet.tvmarket.entity;

public class DownloadFileReq implements AutoType {
	private String url;
	private String localPath;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getLocalPath() {
		return localPath;
	}

	public void setLocalPath(String localPath) {
		this.localPath = localPath;
	}

	@Override
	public String toString() {
		return "DownloadFileReq [url=" + url + ", localPath=" + localPath + "]";
	}

}
