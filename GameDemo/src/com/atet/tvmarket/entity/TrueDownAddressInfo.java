package com.atet.tvmarket.entity;

import java.io.Serializable;

import com.atet.tvmarket.view.recyclerview.BaseBean;


public class TrueDownAddressInfo extends BaseBean implements AutoType,Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String url; //真实的下载地址

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String toString() {
		return "TrueDownAddressInfo [url=" + url + "]";
	}
	
	

}
