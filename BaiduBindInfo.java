package com.byd.map.account.lbs;

/* JADX INFO: loaded from: classes.dex */
public class BaiduBindInfo {
    private String bdDisplayName;
    private String bdPortrait;
    private String bdUid;
    private String extra;
    private String oemUid;

    public String getBdDisplayName() {
        return this.bdDisplayName;
    }

    public String getBdPortrait() {
        return this.bdPortrait;
    }

    public String getBdUid() {
        return this.bdUid;
    }

    public String getExtra() {
        return this.extra;
    }

    public String getOemUid() {
        return this.oemUid;
    }

    public void setBdDisplayName(String str) {
        this.bdDisplayName = str;
    }

    public void setBdPortrait(String str) {
        this.bdPortrait = str;
    }

    public void setBdUid(String str) {
        this.bdUid = str;
    }

    public void setExtra(String str) {
        this.extra = str;
    }

    public void setOemUid(String str) {
        this.oemUid = str;
    }

    public String toString() {
        return "BaiduBindInfo{bdDisplayName='" + this.bdDisplayName + "', bdUid='" + this.bdUid + "', bdPortrait='" + this.bdPortrait + "', oemUid='" + this.oemUid + "', extra='" + this.extra + "'}";
    }
}
