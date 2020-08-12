package org.mazhuang.cleanexpert;

import android.app.Application;
import android.content.Context;

import com.facebook.stetho.Stetho;
import com.firebase.client.Firebase;
import com.google.firebase.FirebaseApp;

public class Protection extends Application {
    private static Protection sInstance;

    public static Application getInstance() {
        return sInstance;
    }
    public static Context instance;
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        sInstance = this;
        Stetho.initializeWithDefaults(this);
        Firebase.setAndroidContext(this);
        FirebaseApp.initializeApp(this);
    }
}
