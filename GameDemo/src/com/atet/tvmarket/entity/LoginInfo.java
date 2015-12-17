package com.atet.tvmarket.entity;

import java.io.Serializable;

public class LoginInfo implements AutoType, Serializable {
    private static final long serialVersionUID = 1L;
    
    private int payId;
    private int orientation;

    public int getOrientation() {
        return orientation;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    @Override
    public String toString() {
        return "LoginInfo [orientation=" + orientation + "]";
    }

    public int getPayId() {
        return payId;
    }

    public void setPayId(int payId) {
        this.payId = payId;
    }

}
