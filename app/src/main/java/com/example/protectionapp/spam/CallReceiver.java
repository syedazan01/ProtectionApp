package com.example.protectionapp.spam;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.protectionapp.R;
import com.example.protectionapp.activites.SplashScreen;
import com.example.protectionapp.model.SpamBean;
import com.example.protectionapp.room.AppDatabase;
import com.example.protectionapp.room.dao.SpamCallDao;

import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.Executors;

import static android.content.Context.NOTIFICATION_SERVICE;

public class CallReceiver extends BroadcastReceiver {
    private static final String TAG = "NoPhoneSpam";

    private static final int NOTIFY_REJECTED = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (TelephonyManager.ACTION_PHONE_STATE_CHANGED.equals(intent.getAction()) &&
                intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(TelephonyManager.EXTRA_STATE_RINGING)) {
            String incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
            Log.i(TAG, "Received call: " + incomingNumber);

            Settings settings = new Settings(context);
            if (TextUtils.isEmpty(incomingNumber)) {
                // private number (no caller ID)
                if (settings.blockHiddenNumbers())
                    rejectCall(context, null,null);

            } else {
                SpamCallDao spamCallDao = AppDatabase.getAppDataBase(context).getSpamCallDao();
                Executors.newSingleThreadExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        List<SpamBean> spamBeanList = spamCallDao.getSpamCalls();
                        if (spamBeanList != null) {
                           for(SpamBean spamBean : spamBeanList)
                           {
                               if(spamBean.isChecked() && (spamBean.getCallerNumber().equalsIgnoreCase(incomingNumber)))
                               {
                                   rejectCall(context, spamBean.getCallerNumber(),spamBean.getCallName());
                                   BlacklistObserver.notifyUpdated();
                                   break;
                               }
                           }
                        }
                    }
                });
            }
        }
    }

    protected void rejectCall(@NonNull Context context, String number,String name) {
        TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        Class c = null;
        try {
            c = Class.forName(tm.getClass().getName());
            Method m = c.getDeclaredMethod("getITelephony");
            m.setAccessible(true);

            ITelephony telephony = (ITelephony)m.invoke(tm);

            telephony.endCall();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Settings settings = new Settings(context);
        if (settings.showNotifications()) {
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                NotificationChannel defaultChannel = new NotificationChannel("SpamVer1.0","spamCall", NotificationManager.IMPORTANCE_HIGH);
                notificationManager.createNotificationChannel(defaultChannel);
            }
            Notification notify = new NotificationCompat.Builder(context,"SpamVer1.0")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("Call Rejected")
                    .setContentText(number != null ? (name != null ? name : number) : "Private Number")
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setCategory(NotificationCompat.CATEGORY_CALL)
                    .setShowWhen(true)
                    .setAutoCancel(true)
                    .setContentIntent(PendingIntent.getActivity(context, 0, new Intent(context, SplashScreen.class), PendingIntent.FLAG_UPDATE_CURRENT))
                    .addPerson("tel:" + number)
                    .setGroup("rejected")
                    .build();

            String tag = number != null ? number : "private";
            notificationManager.notify(tag, NOTIFY_REJECTED, notify);
        }

    }

}