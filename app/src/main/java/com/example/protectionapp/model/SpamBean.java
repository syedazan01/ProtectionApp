package com.example.protectionapp.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "SpamCalls")
public class SpamBean {
    @PrimaryKey(autoGenerate = true)
    int id;
    String callName,callerNumber;
    boolean isChecked;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getCallName() {
        return callName;
    }

    public void setCallName(String callName) {
        this.callName = callName;
    }

    public String getCallerNumber() {
        return callerNumber;
    }

    public void setCallerNumber(String callerNumber) {
        this.callerNumber = callerNumber;
    }
}
