package com.byd.map.account.lbs;

/* JADX INFO: loaded from: classes.dex */
public class ExternalBaiduInfo {
    private int code = 0;
    private String data;
    private String extra;
    private String funtype;

    public ExternalBaiduInfo() {
    }

    public ExternalBaiduInfo(String str, String str2) {
        this.funtype = str;
        this.data = str2;
    }

    public int getCode() {
        return this.code;
    }

    public String getData() {
        return this.data;
    }

    public String getExtra() {
        return this.extra;
    }

    public String getFuntype() {
        return this.funtype;
    }

    public void setCode(int i) {
        this.code = i;
    }

    public void setData(String str) {
        this.data = str;
    }

    public void setExtra(String str) {
        this.extra = str;
    }

    public void setFuntype(String str) {
        this.funtype = str;
    }
}
