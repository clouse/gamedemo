package com.atet.tvmarket.entity.pay;

import com.atet.tvmarket.entity.AutoType;

public class PayResult implements AutoType {
    private int resultCode;
    private String signValue;
    private String appId;
    private String waresId;
    private String exorderno;

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public String getSignValue() {
        return signValue;
    }

    public void setSignValue(String signValue) {
        this.signValue = signValue;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getWaresId() {
        return waresId;
    }

    public void setWaresId(String waresId) {
        this.waresId = waresId;
    }

    public String getExorderno() {
        return exorderno;
    }

    public void setExorderno(String exorderno) {
        this.exorderno = exorderno;
    }

    @Override
    public String toString() {
        return "PayResult [resultCode=" + resultCode + ", signValue="
                + signValue + ", appId=" + appId + ", waresId=" + waresId
                + ", exorderno=" + exorderno + "]";
    }

}
