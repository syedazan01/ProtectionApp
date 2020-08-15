package com.example.protectionapp.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.protectionapp.R;
import com.example.protectionapp.activites.SplashScreen;
import com.example.protectionapp.utils.AppConstant;
import com.example.protectionapp.utils.PrefManager;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class NotificationsMessagingService extends FirebaseMessagingService {
    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
    }

    @Override
    public void onMessageReceived(@NotNull RemoteMessage remoteMessage) {
        String title = remoteMessage.getNotification().getTitle();
        String message = remoteMessage.getData().get("msg");

        SharedPreferences sharedPreferences = getSharedPreferences(PrefManager.PREF_NAME, MODE_PRIVATE);
        if (sharedPreferences.getBoolean(AppConstant.ISLOGGEDIN, false)) {
            sendNotification(
                    "",
                    title,
                    message
            );
        }
    }

    private void sendNotification(
            String id,
            String title,
            String message
    ) {
        // The id of the channel.
        final String CHANNEL_ID = "default";
        final String CHANNEL_NAME = "Default";

        Intent notificationIntent = new Intent(this, SplashScreen.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);


        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel defaultChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(defaultChannel);
        }

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(title))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
//                .setLargeIcon(bitmap)
                .setSmallIcon(R.drawable.login_logo)
                .setPriority(Notification.PRIORITY_MAX)
                .setContentTitle(title)
                .setContentText(message)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        Notification notification = builder.build();
        notification.defaults |= Notification.DEFAULT_VIBRATE;
        notificationManager.notify(100, notification);
    }
}
