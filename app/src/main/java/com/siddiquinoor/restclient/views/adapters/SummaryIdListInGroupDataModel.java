package com.siddiquinoor.restclient.views.adapters;

/**
 * Created by USER on 9/6/2016.
 */
public class SummaryIdListInGroupDataModel {
    private String nMemId;// 15 digit id
    private String memName;
    private String srvName; // criteria

    public String getnMemId() {
        return nMemId;
    }

    public void setnMemId(String nMemId) {
        this.nMemId = nMemId;
    }

    public String getMemName() {
        return memName;
    }

    public void setMemName(String memName) {
        this.memName = memName;
    }

    public String getSrvName() {
        return srvName;
    }

    public void setSrvName(String srvName) {
        this.srvName = srvName;
    }
}
