package com.atet.tvmarket.model;

import com.atet.tvmarket.model.netroid.Listener;
import com.atet.tvmarket.model.netroid.toolbox.FileDownloader;

public class DownloadTaskRequest {
	private Listener listener;
	private String localFilePath;
	private String fileUrl;
	private FileDownloader.DownloadController downloadController;

	public String getLocalFilePath() {
		return localFilePath;
	}

	public void setLocalFilePath(String localFilePath) {
		this.localFilePath = localFilePath;
	}

	public String getFileUrl() {
		return fileUrl;
	}

	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}

	public Listener getListener() {
		return listener;
	}

	public void setListener(Listener listener) {
		this.listener = listener;
	}

	public FileDownloader.DownloadController getDownloadController() {
		return downloadController;
	}

	public void setDownloadController(
			FileDownloader.DownloadController downloadController) {
		this.downloadController = downloadController;
	}

	@Override
	public String toString() {
		return "DownloadTaskRequest [listener=" + listener + ", localFilePath="
				+ localFilePath + ", fileUrl=" + fileUrl
				+ ", downloadController=" + downloadController + "]";
	}
}
