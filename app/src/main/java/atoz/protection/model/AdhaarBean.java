package atoz.protection.model;

import java.io.Serializable;

public class AdhaarBean implements Serializable {
    int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    String mobileNo, Fullname, dateofbirth, gender, aadharNumber, Address, Adhaarimage, Adhaarimage2;

    public String getAdhaarimage2() {
        return Adhaarimage2;
    }

    public void setAdhaarimage2(String adhaarimage2) {
        Adhaarimage2 = adhaarimage2;
    }

    /* public AdhaarBean(String fullname, String dateofbirth, String gender, String aadharNumber, String address) {
            Fullname = fullname;
            this.dateofbirth = dateofbirth;
            this.gender = gender;
            this.aadharNumber = aadharNumber;
            Address = address;
        }*/
    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getAdhaarimage() {
        return Adhaarimage;
    }

    public void setAdhaarimage(String adhaarimage) {
        Adhaarimage = adhaarimage;
    }

    public String getFullname() {
        return Fullname;
    }

    public void setFullname(String fullname) {
        Fullname = fullname;
    }

    public String getDateofbirth() {
        return dateofbirth;
    }

    public void setDateofbirth(String dateofbirth) {
        this.dateofbirth = dateofbirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAadharNumber() {
        return aadharNumber;
    }

    public void setAadharNumber(String aadharNumber) {
        this.aadharNumber = aadharNumber;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }
}

//heklllohohjjnoij