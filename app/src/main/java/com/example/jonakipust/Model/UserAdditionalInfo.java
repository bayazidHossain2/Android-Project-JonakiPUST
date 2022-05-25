package com.example.jonakipust.Model;

import java.util.ArrayList;

public class UserAdditionalInfo {
    private String uid;
    private double weight;
    private int height;
    private int studentId;
    private String currentAddress;
    private String parmanentAddress;
    private StringBuilder donationHistoryUID;

    public UserAdditionalInfo(){}

    public UserAdditionalInfo( String uid, double weight, int height, int studentId,
                              String currentAddress, String parmanentAddress, StringBuilder donationHistoryUID) {
        this.uid = uid;
        this.weight = weight;
        this.height = height;
        this.studentId = studentId;
        this.currentAddress = currentAddress;
        this.parmanentAddress = parmanentAddress;
        this.donationHistoryUID = donationHistoryUID;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getBmi() {
        double heightMeter = (height*2.540000000000001)/100;
        String sBMI = String.valueOf(weight/(heightMeter*heightMeter));
        if(sBMI.length()>5){
            sBMI = sBMI.substring(0,5);
        }
        return sBMI;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public int getHeight() {
        return height;
    }
    public String getHeightString() {
        int fit = (int) (height/12);
        return fit+"fit "+(height-(fit*12))+"inch";
    }

    public String getHeightFit() {
        int fit = (int) (height/12);
        return fit+"";
    }

    public String getHeightInch() {
        int fit = (int) (height/12);
        return (height-(fit*12))+"";
    }
    public void setHeight(int height) {
        this.height = height;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getCurrentAddress() {
        return currentAddress;
    }

    public void setCurrentAddress(String currentAddress) {
        this.currentAddress = currentAddress;
    }

    public String getParmanentAddress() {
        return parmanentAddress;
    }

    public void setParmanentAddress(String parmanentAddress) {
        this.parmanentAddress = parmanentAddress;
    }

    public String getDonationHistoryUID() {
        return donationHistoryUID.toString();
    }

    public void setDonationHistoryUID(String donationHistoryUID) {
        this.donationHistoryUID = new StringBuilder(donationHistoryUID);
    }
}
