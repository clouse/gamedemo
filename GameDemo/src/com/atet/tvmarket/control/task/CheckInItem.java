package com.atet.tvmarket.control.task;

public class CheckInItem{
	private String itemkey;
	private String itemValue;
	private int textSize;
	private boolean isReceived;
	
	public CheckInItem(String itemkey, String itemValue,int textSize,boolean isReceived) {
		super();
		this.itemkey = itemkey;
		this.itemValue = itemValue;
		this.textSize = textSize;
		this.isReceived = isReceived;
	}
	public String getItemkey() {
		return itemkey;
	}
	public void setItemkey(String itemkey) {
		this.itemkey = itemkey;
	}
	public String getItemValue() {
		return itemValue;
	}
	public void setItemValue(String itemValue) {
		this.itemValue = itemValue;
	}
	public boolean isReceived() {
		return isReceived;
	}
	public void setReceived(boolean isReceived) {
		this.isReceived = isReceived;
	}
	public int getTextSize() {
		return textSize;
	}
	public void setTextSize(int textSize) {
		this.textSize = textSize;
	}
	
	
}
