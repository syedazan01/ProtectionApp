package com.example.protectionapp.model;

public class FetchNotification {
    String push_key, to_mobile, from_mobile, profile_Pic, created_date, msg;

    public String getPush_key() {
        return push_key;
    }

    public void setPush_key(String push_key) {
        this.push_key = push_key;
    }

    public String getTo_mobile() {
        return to_mobile;
    }

    public void setTo_mobile(String to_mobile) {
        this.to_mobile = to_mobile;
    }

    public String getFrom_mobile() {
        return from_mobile;
    }

    public void setFrom_mobile(String from_mobile) {
        this.from_mobile = from_mobile;
    }

    public String getProfile_Pic() {
        return profile_Pic;
    }

    public void setProfile_Pic(String profile_Pic) {
        this.profile_Pic = profile_Pic;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
