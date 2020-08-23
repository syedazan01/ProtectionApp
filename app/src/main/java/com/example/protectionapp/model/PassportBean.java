package com.example.protectionapp.model;

import java.io.Serializable;

public class PassportBean implements Serializable {
    int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    String passportnumber, type, countrycode, nationalty, fullname, sex, dateofbirth, placeofbirth, placeofissue, dateofissue, dateofexpiry, mobilenumber, passportimage1, passportimage2;

    public String getPassportnumber() {
        return passportnumber;
    }

    public void setPassportnumber(String passportnumber) {
        this.passportnumber = passportnumber;
    }

    public String getMobilenumber() {
        return mobilenumber;
    }

    public void setMobilenumber(String mobilenumber) {
        this.mobilenumber = mobilenumber;
    }

    public String getPassportimage1() {
        return passportimage1;
    }

    public void setPassportimage1(String passportimage1) {
        this.passportimage1 = passportimage1;
    }

    public String getPassportimage2() {
        return passportimage2;
    }

    public void setPassportimage2(String passportimage2) {
        this.passportimage2 = passportimage2;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCountrycode() {
        return countrycode;
    }

    public void setCountrycode(String countrycode) {
        this.countrycode = countrycode;
    }

    public String getNationalty() {
        return nationalty;
    }

    public void setNationalty(String nationalty) {
        this.nationalty = nationalty;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getDateofbirth() {
        return dateofbirth;
    }

    public void setDateofbirth(String dateofbirth) {
        this.dateofbirth = dateofbirth;
    }

    public String getPlaceofbirth() {
        return placeofbirth;
    }

    public void setPlaceofbirth(String placeofbirth) {
        this.placeofbirth = placeofbirth;
    }

    public String getPlaceofissue() {
        return placeofissue;
    }

    public void setPlaceofissue(String placeofissue) {
        this.placeofissue = placeofissue;
    }

    public String getDateofissue() {
        return dateofissue;
    }

    public void setDateofissue(String dateofissue) {
        this.dateofissue = dateofissue;
    }

    public String getDateofexpiry() {
        return dateofexpiry;
    }

    public void setDateofexpiry(String dateofexpiry) {
        this.dateofexpiry = dateofexpiry;
    }

}
