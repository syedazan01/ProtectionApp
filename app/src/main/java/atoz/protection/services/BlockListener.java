package atoz.protection.services;

import android.content.SharedPreferences;
import android.os.Build;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

import atoz.protection.utils.AppConstant;
import atoz.protection.utils.PrefManager;
import atoz.protection.utils.Utils;

import java.util.Arrays;
import java.util.HashSet;

public class BlockListener extends NotificationListenerService {
    @Override
    public void onNotificationPosted(StatusBarNotification notification) {
        super.onNotificationPosted(notification);
        SharedPreferences preferences= Utils.getDefaultManager(BlockListener.this);
        SharedPreferences prefManager=this.getSharedPreferences(PrefManager.PREF_NAME,MODE_PRIVATE);
        if(preferences.getBoolean(AppConstant.NOTIFICATION_ENABLE,false))
        {

            HashSet blocked = new HashSet(Arrays.asList(preferences.getString(AppConstant.PREF_PACKAGES_BLOCKED, "").split(";")));
            if (prefManager.getBoolean(AppConstant.RARE_NOTIFICATION,false) || blocked.contains(notification.getPackageName())) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    cancelNotification(notification.getKey());
                } else {
                    cancelNotification(notification.getPackageName(), notification.getTag(), notification.getId());
                }
            }
        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        super.onNotificationRemoved(sbn);
    }
}
