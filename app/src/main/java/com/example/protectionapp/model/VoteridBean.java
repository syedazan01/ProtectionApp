package com.example.protectionapp.model;

public class VoteridBean {
    String FullName, FathersName, Gender, dateofbirth, Address, Assemblyname;

    public VoteridBean(String fullName, String fathersName, String gender, String dateofbirth, String address, String assemblyname) {
        FullName = fullName;
        FathersName = fathersName;
        Gender = gender;
        this.dateofbirth = dateofbirth;
        Address = address;
        Assemblyname = assemblyname;
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
