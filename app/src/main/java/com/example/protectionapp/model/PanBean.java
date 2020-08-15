package com.example.protectionapp.model;

import java.io.Serializable;

public class PanBean implements Serializable {
    String fullName, fatherName, dateOfBirth, PermanentAccountNumber, panimage, panmobile;

    public String getPanimage() {
        return panimage;
    }

    public void setPanimage(String panimage) {
        this.panimage = panimage;
    }

    public String getPanmobile() {
        return panmobile;
    }

    public void setPanmobile(String panmobile) {
        this.panmobile = panmobile;
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
