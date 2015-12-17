package com.atet.tvmarket.control.mygame.update;

public class IllegalUrlException extends Exception{
	private static final long serialVersionUID=8248979961640522923L;
	
	public IllegalUrlException(){
		super();
	}
	
	public IllegalUrlException(String detailMessage,Throwable throwable){
		super(detailMessage,throwable);
	}
	
	public IllegalUrlException(String detailMessage){
		this(detailMessage,null);
	}
	
	public IllegalUrlException(Throwable throwable){
		this(null,throwable);
	}

}
