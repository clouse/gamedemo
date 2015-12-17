package com.atet.tvmarket.entity;

public class RespGetInterceptInfo {
    private int code;
    private String data;
    
    public RespGetInterceptInfo() {
        // TODO Auto-generated constructor stub
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "RespGetInterceptInfo [code=" + code + ", data=" + data + "]";
    }
}
