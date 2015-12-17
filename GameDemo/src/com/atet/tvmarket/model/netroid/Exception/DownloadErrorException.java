package com.atet.tvmarket.model.netroid.Exception;

public class DownloadErrorException extends RuntimeException{
	private static final long serialVersionUID= 6526592819041935721L;
	public DownloadErrorException(){
		super();
	}
	
	public DownloadErrorException(String detailMessage,Throwable throwable){
		super(detailMessage,throwable);
	}
	
	public DownloadErrorException(String detailMessage){
		super(detailMessage);
	}
	
	public DownloadErrorException(Throwable throwable) {
		super(throwable);
	}
}
