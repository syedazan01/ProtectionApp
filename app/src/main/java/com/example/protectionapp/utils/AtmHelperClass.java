package com.example.protectionapp.utils;

public class AtmHelperClass {
    String bankname, atmnumber, nameoncard, cardVailidity, cvvcode;

    public AtmHelperClass(String bankname, String atmnumber, String nameoncard, String cardVailidity, String cvvcode) {
        this.bankname = bankname;
        this.atmnumber = atmnumber;
        this.nameoncard = nameoncard;
        this.cardVailidity = cardVailidity;
        this.cvvcode = cvvcode;
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
