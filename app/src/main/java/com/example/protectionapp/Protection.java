package com.example.protectionapp;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.multidex.MultiDexApplication;

import com.example.protectionapp.utils.AppConstant;
import com.example.protectionapp.utils.PrefManager;
import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

public class Protection extends MultiDexApplication {
    private static Context mInstance;
    @Override
    public void onCreate() {
        super.onCreate();
        mInstance=this;
        Firebase.setAndroidContext(this);
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if(task.isSuccessful())
                    PrefManager.putString(AppConstant.FCMTOKEN,task.getResult().getToken());
            }
        });
    }
    public static Context getInstance()
    {
        if(mInstance==null)
            mInstance=new Protection();
        return mInstance;
    }
}
