package com.example.protectionapp.model;

import java.io.Serializable;

public class VoteridBean implements Serializable {
    String FullName, FathersName, Gender, dateofbirth, Address, Assemblyname, voterMobileNo, voterImage;

    public String getVoterMobileNo() {
        return voterMobileNo;
    }

    public void setVoterMobileNo(String voterMobileNo) {
        this.voterMobileNo = voterMobileNo;
    }

    public String getVoterImage() {
        return voterImage;
    }

    public void setVoterImage(String voterImage) {
        this.voterImage = voterImage;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getFathersName() {
        return FathersName;
    }

    public void setFathersName(String fathersName) {
        FathersName = fathersName;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getDateofbirth() {
        return dateofbirth;
    }

    public void setDateofbirth(String dateofbirth) {
        this.dateofbirth = dateofbirth;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getAssemblyname() {
        return Assemblyname;
    }

    public void setAssemblyname(String assemblyname) {
        Assemblyname = assemblyname;
    }
}
