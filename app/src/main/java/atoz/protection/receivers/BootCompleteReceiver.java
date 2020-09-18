package atoz.protection.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import atoz.protection.services.FloatingWindowService;

public class BootCompleteReceiver extends BroadcastReceiver {
    Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("SCREEN>>>", intent.getAction());

        Intent forrgroundIntent = new Intent(context, FloatingWindowService.class);
//        forrgroundIntent.setAction(FloatingWindowService.SCREEN_ON);
        context.startService(forrgroundIntent);
    }
}
