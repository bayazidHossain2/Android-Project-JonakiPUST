package com.example.jonakipust.Model;

public class UserModel {
    private String uid;
    private String profile;
    private String name;
    private String bloodGroup;
    private String lastDonationDate;
    private int numberOfDonation;
    private String phone;

    public UserModel(){}

    public UserModel(String uid,String profile,String name, String bloodGroup, String lastDonationDate,
                     int numberOfDonation, String phone) {
        this.uid = uid;
        this.profile = profile;
        this.name = name;
        this.bloodGroup = bloodGroup;
        this.lastDonationDate = lastDonationDate;
        this.numberOfDonation = numberOfDonation;
        this.phone = phone;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getLastDonationDate() {
        return lastDonationDate;
    }

    public void setLastDonationDate(String lastDonationDate) {
        this.lastDonationDate = lastDonationDate;
    }

    public int getNumberOfDonation() {
        return numberOfDonation;
    }

    public void setNumberOfDonation(int numberOfDonation) {
        this.numberOfDonation = numberOfDonation;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
