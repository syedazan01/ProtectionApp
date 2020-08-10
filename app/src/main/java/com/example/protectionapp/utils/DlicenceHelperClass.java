package com.example.protectionapp.utils;

public class DlicenceHelperClass {
    String fullname,son_of,licenceNumber,bloodGroup,dateOfBirth,dateOfIssue,validity;

    public DlicenceHelperClass(String fullname, String son_of, String licenceNumber, String bloodGroup, String dateOfBirth, String dateOfIssue, String validity) {
        this.fullname = fullname;
        this.son_of = son_of;
        this.licenceNumber = licenceNumber;
        this.bloodGroup = bloodGroup;
        this.dateOfBirth = dateOfBirth;
        this.dateOfIssue = dateOfIssue;
        this.validity = validity;
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
