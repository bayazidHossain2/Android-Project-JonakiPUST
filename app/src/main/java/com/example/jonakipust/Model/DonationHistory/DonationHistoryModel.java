package com.example.jonakipust.Model.DonationHistory;

public class DonationHistoryModel implements Comparable<DonationHistoryModel>{
    private String uid;
    private String donerUid;
    private String donationDate;
    private String shortDisc;

    public DonationHistoryModel(){}

    public DonationHistoryModel(String uid, String donerUid, String donationDate, String shortDisc) {
        this.uid = uid;
        this.donerUid = donerUid;
        this.donationDate = donationDate;
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

    public String getDonationDate() {
        return donationDate;
    }

    public void setDonationDate(String donationDate) {
        this.donationDate = donationDate;
    }

    public String getShortDisc() {
        return shortDisc;
    }

    public void setShortDisc(String shortDisc) {
        this.shortDisc = shortDisc;
    }

    @Override
    public int compareTo(DonationHistoryModel donationHistoryModel) {
        return Integer.parseInt(this.uid) - Integer.parseInt(donationHistoryModel.getUid());
    }
}
