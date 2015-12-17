package com.atet.tvmarket.entity;

import java.io.Serializable;

import com.atet.tvmarket.view.recyclerview.BaseBean;


public class DownloadAddrInfo extends BaseBean implements AutoType,Serializable{
    private static final long serialVersionUID = 1L;
    private String downloadAddress;
    private int type;
    
    
    public DownloadAddrInfo() {
        // TODO Auto-generated constructor stub
    }

    public DownloadAddrInfo(String downloadAddress, int type) {
        super();
        this.downloadAddress = downloadAddress;
        this.type = type;
    }

    public String getDownloadAddress() {
        return downloadAddress;
    }

    public void setDownloadAddress(String downloadAddress) {
        this.downloadAddress = downloadAddress;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "DownloadAddrInfo [downloadAddress=" + downloadAddress
                + ", type=" + type + "]";
    }
}
