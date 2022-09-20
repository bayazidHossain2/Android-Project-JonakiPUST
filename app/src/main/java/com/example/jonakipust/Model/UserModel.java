package com.example.jonakipust.Model;

public class UserModel {
    private String uid;
    private String profile;
    private String name;
    private String bloodGroup;
    private String lastDonationDate;
    private int numberOfDonation;
    private String phone;
    private String lastContact;
    private double weight;
    private int height;
    private int studentId;
    private String currentAddress;
    private String parmanentAddress;
    private StringBuilder donationHistoryUID;

    public UserModel(){}

    public UserModel(String uid,String profile,String name, String bloodGroup, String lastDonationDate,
                     int numberOfDonation, String phone,String lastContact,double weight, int height,
                     int studentId, String currentAddress, String parmanentAddress, StringBuilder donationHistoryUID) {
        this.uid = uid;
        this.profile = profile;
        this.name = name;
        this.bloodGroup = bloodGroup;
        this.lastDonationDate = lastDonationDate;
        this.numberOfDonation = numberOfDonation;
        this.phone = phone;
        this.lastContact = lastContact;
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

    public String getLastContact(){
        return lastContact;
    }

    public void setLastContact(String lastContact){ this.lastContact = lastContact; }

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
