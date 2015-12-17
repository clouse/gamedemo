package com.atet.tvmarket.entity;

import java.util.List;

public class RespGetConfigInfo implements AutoType{
    private int code;
    private List<AddrInfo> data;

    public RespGetConfigInfo() {
        // TODO Auto-generated constructor stub
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
    
    public List<AddrInfo> getData() {
        return data;
    }

    public void setData(List<AddrInfo> data) {
        this.data = data;
    }
    
    @Override
    public String toString() {
        return "RespGetConfigInfo [code=" + code + ", data=" + data + "]";
    }

    public static class AddrInfo{
        private String downloadURL;
        private int handleType;
        
        public AddrInfo() {
            // TODO Auto-generated constructor stub
        }
        
        public String getDownloadURL() {
            return downloadURL;
        }
        public void setDownloadURL(String downloadURL) {
            this.downloadURL = downloadURL;
        }
        public int getHandleType() {
            return handleType;
        }
        public void setHandleType(int handleType) {
            this.handleType = handleType;
        }

        @Override
        public String toString() {
            return "AddrInfo [downloadURL=" + downloadURL + ", handleType="
                    + handleType + "]";
        }
    }

}
