package com.example.protectionapp.model;

import java.io.Serializable;

public class PanBean implements Serializable {
    int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    String fullName, fatherName, dateOfBirth, PermanentAccountNumber, panimage, panimage2, panmobile;

    public String getPanimage2() {
        return panimage2;
    }

    public void setPanimage2(String panimage2) {
        this.panimage2 = panimage2;
    }

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
