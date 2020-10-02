package atoz.protection.model;

import java.io.Serializable;

public class BirthCertificateBean implements Serializable {
    int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    String moblilenumber, fathername, mothername, childname, sex, dateofbirth, hospitalname, imageview1, imageview2,documentType;

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getMoblilenumber() {
        return moblilenumber;
    }

    public void setMoblilenumber(String moblilenumber) {
        this.moblilenumber = moblilenumber;
    }

    public String getFathername() {
        return fathername;
    }

    public void setFathername(String fathername) {
        this.fathername = fathername;
    }

    public String getMothername() {
        return mothername;
    }

    public void setMothername(String mothername) {
        this.mothername = mothername;
    }

    public String getChildname() {
        return childname;
    }

    public void setChildname(String childname) {
        this.childname = childname;
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

    public String getHospitalname() {
        return hospitalname;
    }

    public void setHospitalname(String hospitalname) {
        this.hospitalname = hospitalname;
    }

    public String getImageview1() {
        return imageview1;
    }

    public void setImageview1(String imageview1) {
        this.imageview1 = imageview1;
    }

    public String getImageview2() {
        return imageview2;
    }

    public void setImageview2(String imageview2) {
        this.imageview2 = imageview2;
    }
}
