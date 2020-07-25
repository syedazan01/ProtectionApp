package com.example.protectionapp.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "RecordingFileData")
public class RecordingFileData {
    @PrimaryKey(autoGenerate = true) int  id;
    String fileName;
    String created_date;
    long ellipseMillis;
    String filePath;

    public long getEllipseMillis() {
        return ellipseMillis;
    }

    public void setEllipseMillis(long ellipseMillis) {
        this.ellipseMillis = ellipseMillis;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }
}
