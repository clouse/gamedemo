package com.atet.tvmarket.control.mygame.update;

public class NetWorkNotFoundException extends Exception{
	private static final long serialVersionUID = -8714305957835301255L;
	
	public NetWorkNotFoundException() {
		super();
	}
	
	public NetWorkNotFoundException(String detailMessage,Throwable throwable) {
		super(detailMessage,throwable);
	}
	
	public NetWorkNotFoundException(String detailMessage){
		this(detailMessage,null);
	}
	
	public NetWorkNotFoundException(Throwable throwable){
		this(null,throwable);
	}

}
