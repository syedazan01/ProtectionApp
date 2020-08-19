package com.example.protectionapp.model;

import java.io.Serializable;

public class DlicenceBean implements Serializable {
    String fullname, son_of, licenceNumber, bloodGroup, dateOfBirth, dateOfIssue, validity, mobileno, DLimage, DLimage2;

    public String getDLimage2() {
        return DLimage2;
    }

    public void setDLimage2(String DLimage2) {
        this.DLimage2 = DLimage2;
    }

    public String getMobileno() {
        return mobileno;
    }

    public void setMobileno(String mobileno) {
        this.mobileno = mobileno;
    }

    public String getDLimage() {
        return DLimage;
    }

    public void setDLimage(String DLimage) {
        this.DLimage = DLimage;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getSon_of() {
        return son_of;
    }

    public void setSon_of(String son_of) {
        this.son_of = son_of;
    }

    public String getLicenceNumber() {
        return licenceNumber;
    }

    public void setLicenceNumber(String licenceNumber) {
        this.licenceNumber = licenceNumber;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getDateOfIssue() {
        return dateOfIssue;
    }

    public void setDateOfIssue(String dateOfIssue) {
        this.dateOfIssue = dateOfIssue;
    }

    public String getValidity() {
        return validity;
    }

    public void setValidity(String validity) {
        this.validity = validity;
    }
}
