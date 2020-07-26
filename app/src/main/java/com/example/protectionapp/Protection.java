package com.example.protectionapp;

import android.app.Application;

import com.facebook.stetho.Stetho;

public class Protection extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }
}
