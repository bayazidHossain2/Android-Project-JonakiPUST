package com.example.jonakipust.Model;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class UserShortModel {
    private String uid;
    private String name;
    private String bloodGroup;
    private String lastDonationDate;
    private int numberOfDonation;
    private String lastContact;
    private String profile;

    public UserShortModel(String uid, String name, String bloodGroup, String lastDonationDate, int numberOfDonation,
                          String lastContact, String profile) {
        this.uid = uid;
        this.name = name;
        this.bloodGroup = bloodGroup;
        this.lastDonationDate = lastDonationDate;
        this.numberOfDonation = numberOfDonation;
        this.lastContact = lastContact;
        this.profile = profile;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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

    public String getLastContact() {
        return lastContact;
    }

    public void setLastContact(String lastContuct) {
        this.lastContact = lastContuct;
    }

    public String getProfile() {
        return profile;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getTimeDiffrernce(){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String currentTime = dateTimeFormatter.format(LocalDateTime.now());
        String cur = currentTime.substring(6,10);
        String prev = lastContact.substring(6,10);
        int year,cyear,month,cmonth,day,cday,hour,chour,min,cmin;
        year=Integer.parseInt(prev);
        cyear=Integer.parseInt(cur);
        cur = currentTime.substring(3,5);
        prev = lastContact.substring(3,5);
        month = Integer.parseInt(prev);
        cmonth = Integer.parseInt(cur);
        cur = currentTime.substring(0,2);
        prev = lastContact.substring(0,2);
        day = Integer.parseInt(prev);
        cday = Integer.parseInt(cur);
        cur = currentTime.substring(11,13);
        prev = lastContact.substring(11,13);
        hour = Integer.parseInt(prev);
        chour = Integer.parseInt(cur);
        cur = currentTime.substring(14,16);
        prev = lastContact.substring(14,16);
        min = Integer.parseInt(prev);
        cmin = Integer.parseInt(cur);
        int dyear,dmonth,dday,dhour,dmin;
        if(cmin<min){
            chour--;
            cmin+=60;
        }dmin = cmin - min;
        if(chour<hour){
            cday--;
            chour+=24;
        }dhour = chour - hour;
        if(cday<day){
            cmonth--;
            cday+=30;
        }dday = cday - day;
        if(cmonth<month){
            cyear--;
            cmonth+=12;
        }dmonth = cmonth - month;
        dyear = cyear - year;
        if(dyear>0){
            return dyear+" year ago.";
        }else if(dmonth>0){
            return dmonth+" month ago.";
        }else if(dday>0){
            return dday+" day ago.";
        }else if(dhour>0){
            return dhour+" hour ago.";
        }else if(dmin>0){
            return dmin+" min ago.";
        }
        return "few second ago.";
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean isPrepared(){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String[] cTimes = dateTimeFormatter.format(LocalDateTime.now()).split("/");
        String[] dTimes = lastDonationDate.split(" ")[0].split("/");
        int year,cyear,month,cmonth,day,cday;
        year=Integer.parseInt(dTimes[2]);
        cyear=Integer.parseInt(cTimes[2]);
        month = Integer.parseInt(dTimes[1]);
        cmonth = Integer.parseInt(cTimes[1]);
        day = Integer.parseInt(dTimes[0]);
        cday = Integer.parseInt(cTimes[0]);
        int dyear,dmonth,dday;
        if(cday<day){
            cmonth--;
            cday+=30;
        }dday = cday - day;
        if(cmonth<month){
            cyear--;
            cmonth+=12;
        }dmonth = cmonth - month;
        dyear = cyear - year;
        if(dyear>=1 || dmonth>=4){
            return true;
        }else{
            return false;
        }
    }
}
