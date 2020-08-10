package com.example.protectionapp.utils;

public class PanHelperClass {
    String fullName,fatherName,dateOfBirth,PermanentAccountNumber;

    public PanHelperClass(String fullName, String fatherName, String dateOfBirth, String permanentAccountNumber) {
        this.fullName = fullName;
        this.fatherName = fatherName;
        this.dateOfBirth = dateOfBirth;
        PermanentAccountNumber = permanentAccountNumber;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPermanentAccountNumber() {
        return PermanentAccountNumber;
    }

    public void setPermanentAccountNumber(String permanentAccountNumber) {
        PermanentAccountNumber = permanentAccountNumber;
    }
}
