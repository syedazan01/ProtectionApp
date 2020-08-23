package com.example.protectionapp.model;

import java.io.Serializable;

public class StudentIdBean implements Serializable {
    int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    String institutionname, enroll, rollno, fullname, fathername, branch, Studntidimage, Studntidimage2, mobilenumber;


    public String getStudntidimage2() {
        return Studntidimage2;
    }

    public void setStudntidimage2(String studntidimage2) {
        Studntidimage2 = studntidimage2;
    }

    public String getInstitutionname() {
        return institutionname;
    }

    public void setInstitutionname(String institutionname) {
        this.institutionname = institutionname;
    }

    public String getStudntidimage() {
        return Studntidimage;
    }

    public String getMobilenumber() {
        return mobilenumber;
    }

    public void setMobilenumber(String mobilenumber) {
        this.mobilenumber = mobilenumber;
    }

    public void setStudntidimage(String studntidimage) {
        Studntidimage = studntidimage;
    }

    public String getEnroll() {
        return enroll;
    }

    public void setEnroll(String enroll) {
        this.enroll = enroll;
    }

    public String getRollno() {
        return rollno;
    }

    public void setRollno(String rollno) {
        this.rollno = rollno;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getFathername() {
        return fathername;
    }

    public void setFathername(String fathername) {
        this.fathername = fathername;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }
}
