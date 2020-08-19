package com.example.protectionapp.model;

import java.io.Serializable;

public class BankBean implements Serializable {
    String AccountHolderName, accountNumber, ifscCode, branchName, BankName, mobile, bankimage, bankimage2;

    public String getBankimage2() {
        return bankimage2;
    }

    public void setBankimage2(String bankimage2) {
        this.bankimage2 = bankimage2;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getBankimage() {
        return bankimage;
    }

    public void setBankimage(String bankimage) {
        this.bankimage = bankimage;
    }

    public String getAccountHolderName() {
        return AccountHolderName;
    }

    public void setAccountHolderName(String accountHolderName) {
        AccountHolderName = accountHolderName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getIfscCode() {
        return ifscCode;
    }

    public void setIfscCode(String ifscCode) {
        this.ifscCode = ifscCode;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getBankName() {
        return BankName;
    }

    public void setBankName(String bankName) {
        BankName = bankName;
    }
}
