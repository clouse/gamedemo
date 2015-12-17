package com.atet.tvmarket.entity.pay;

import com.atet.tvmarket.entity.AutoType;

public class LoginResult implements AutoType {
    private int retcode;
    private String username;
    private int uid;

    public int getRetcode() {
        return retcode;
    }

    public void setRetcode(int retcode) {
        this.retcode = retcode;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    @Override
    public String toString() {
        return "LoginResult [retcode=" + retcode + ", username=" + username
                + ", uid=" + uid + "]";
    }

}
