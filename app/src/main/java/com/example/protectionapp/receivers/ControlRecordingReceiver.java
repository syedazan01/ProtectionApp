package com.example.protectionapp.receivers;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.protectionapp.services.CallRecorderService;

public class ControlRecordingReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        CallRecorderService service = CallRecorderService.getService();

        if (intent.getAction().equals(CallRecorderService.ACTION_STOP_SPEAKER)) {
            service.putSpeakerOff();
            if (nm != null)
                nm.notify(CallRecorderService.NOTIFICATION_ID, service.buildNotification(CallRecorderService.RECORD_AUTOMMATICALLY, 0));
        } else if (intent.getAction().equals(CallRecorderService.ACTION_START_SPEAKER)) {
            service.putSpeakerOn();
            if (nm != null)
                nm.notify(CallRecorderService.NOTIFICATION_ID, service.buildNotification(CallRecorderService.RECORD_AUTOMMATICALLY, 0));
        }
    }
}