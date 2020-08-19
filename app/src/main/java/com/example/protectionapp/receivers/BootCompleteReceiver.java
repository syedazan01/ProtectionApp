package com.example.protectionapp.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.protectionapp.activites.CallRecorder;
import com.example.protectionapp.services.ForgroundService;

public class BootCompleteReceiver extends BroadcastReceiver {
    Context context;

    @Override
    public void onReceive(Context context, Intent intent) {


        Intent serviceIntent = new Intent(context, CallRecorder.class);
        context.startService(serviceIntent);
        Intent forrgroundIntent = new Intent(context, ForgroundService.class);
        forrgroundIntent.setAction(ForgroundService.ACTION_START_FOREGROUND_SERVICE);
        context.startService(forrgroundIntent);
    }
}
