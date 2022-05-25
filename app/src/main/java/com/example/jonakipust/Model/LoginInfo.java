package com.example.jonakipust.Model;

public class LoginInfo {
    private String UID;
    private String password;
    private String studentID;

    public LoginInfo(){}

    public LoginInfo(String studentID, String password, String UID) {
        this.UID = UID;
        this.password = password;
        this.studentID = studentID;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStudentID() {
        return studentID;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }
}
