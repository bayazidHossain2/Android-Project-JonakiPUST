package com.example.jonakipust.Model.DonationHistory;

public class DonationHistoryModel {
    private String uid;
    private String donerUid;
    private String postUid;
    private String shortDisc;

    public DonationHistoryModel(){}

    public DonationHistoryModel(String uid, String donerUid, String postUid, String shortDisc) {
        this.uid = uid;
        this.donerUid = donerUid;
        this.postUid = postUid;
        this.shortDisc = shortDisc;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDonerUid() {
        return donerUid;
    }

    public void setDonerUid(String donerUid) {
        this.donerUid = donerUid;
    }

    public String getPostUid() {
        return postUid;
    }

    public void setPostUid(String postUid) {
        this.postUid = postUid;
    }

    public String getShortDisc() {
        return shortDisc;
    }

    public void setShortDisc(String shortDisc) {
        this.shortDisc = shortDisc;
    }
}
