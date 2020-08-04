package com.example.protectionapp;

public class UserHelperClass {
    String Fullname,dateofbirth,gender,aadharNumber,Address;

    public UserHelperClass(String fullname, String dateofbirth, String gender, String aadharNumber, String address) {
        Fullname = fullname;
        this.dateofbirth = dateofbirth;
        this.gender = gender;
        this.aadharNumber = aadharNumber;
        Address = address;
    }

    public String getFullname() {
        return Fullname;
    }

    public void setFullname(String fullname) {
        Fullname = fullname;
    }

    public String getDateofbirth() {
        return dateofbirth;
    }

    public void setDateofbirth(String dateofbirth) {
        this.dateofbirth = dateofbirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAadharNumber() {
        return aadharNumber;
    }

    public void setAadharNumber(String aadharNumber) {
        this.aadharNumber = aadharNumber;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }
}

//heklllohohjjnoij
