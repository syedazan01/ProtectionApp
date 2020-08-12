package com.example.protectionapp.services;

import android.util.Log;

import com.google.firebase.messaging.RemoteMessage;
import com.pusher.pushnotifications.fcm.MessagingService;

import org.jetbrains.annotations.NotNull;

public class NotificationsMessagingService extends MessagingService {
    @Override
    public void onMessageReceived(@NotNull RemoteMessage remoteMessage) {
        Log.e("LHJLP)JJLIK", "Hii");
    }
}
