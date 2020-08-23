package com.example.protectionapp.model;

public class MediaDocBean {
   int id;
    String fileName,docUrl,docMobile,docType;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDocType() {
        return docType;
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getDocUrl() {
        return docUrl;
    }

    public void setDocUrl(String docUrl) {
        this.docUrl = docUrl;
    }

    public String getDocMobile() {
        return docMobile;
    }

    public void setDocMobile(String docMobile) {
        this.docMobile = docMobile;
    }
}
