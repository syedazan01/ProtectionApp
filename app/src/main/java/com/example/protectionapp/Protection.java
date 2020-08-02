package com.example.protectionapp;

import android.app.Application;
import android.content.Context;

import com.facebook.stetho.Stetho;
import com.google.firebase.FirebaseApp;

public class Protection extends Application {
    public static Context instance;
    @Override
    public void onCreate() {
        super.onCreate();
        instance=this;
        Stetho.initializeWithDefaults(this);
//        FirebaseApp.initializeApp(this);
    }
}
