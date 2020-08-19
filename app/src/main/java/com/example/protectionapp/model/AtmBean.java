package com.example.protectionapp.model;

import java.io.Serializable;

public class AtmBean implements Serializable {
    String bankname, atmnumber, nameoncard, cardVailidity, cvvcode, mobile, atmimage, atmimage2;

    public String getAtmimage2() {
        return atmimage2;
    }

    public void setAtmimage2(String atmimage2) {
        this.atmimage2 = atmimage2;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAtmimage() {
        return atmimage;
    }

    public void setAtmimage(String atmimage) {
        this.atmimage = atmimage;
    }

    public String getBankname() {
        return bankname;
    }

    public void setBankname(String bankname) {
        this.bankname = bankname;
    }

    public String getAtmnumber() {
        return atmnumber;
    }

    public void setAtmnumber(String atmnumber) {
        this.atmnumber = atmnumber;
    }

    public String getNameoncard() {
        return nameoncard;
    }

    public void setNameoncard(String nameoncard) {
        this.nameoncard = nameoncard;
    }

    public String getCardVailidity() {
        return cardVailidity;
    }

    public void setCardVailidity(String cardVailidity) {
        this.cardVailidity = cardVailidity;
    }

    public String getCvvcode() {
        return cvvcode;
    }

    public void setCvvcode(String cvvcode) {
        this.cvvcode = cvvcode;
    }
}
