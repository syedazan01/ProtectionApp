package com.example.protectionapp.utils;

import android.graphics.Color;

public interface AppConstant {

    //DB Constant
    String DATABASE_NAME = "protection_db";

    //Pref Constant
    String ISNIGHTMODE = "nightMode";
    String IS_CALL_RECORDING_ON = "call_recording_is_on";
    String ISLOGGEDIN = "is_loggedIn";
    String USER_MOBILE = "user_mobile";
    String NOTIFICATION_ENABLE = "notifcation_enable";
    String PREF_PACKAGES_BLOCKED = "pref_packages_blocked";
    String FCMTOKEN = "fcm_token";

    //Real time database constants
    String FIREBASE_DATABASE_URL = "https://protectionapp-3baf6.firebaseio.com/";
    String FIREBASE_STORAGE_DATABASE_URL = "gs://protectionapp-3baf6.appspot.com/";
    String PERSONAL_DOCUMENT = "Personal Document";
    String USER_DETAIL = "User Details";
    String ADHAAR = "adhaar";
    String PAN = "pan";
    String DRIVING_LICENSE = "driving license";
    String BANK = "bank";
    String ATM = "atm";
    String VOTER_ID = "voter id";
    String NOTIFICATION = "Notification";
    String FILE_SHARE = "File Share";
    String PLANS = "Plans";

    //Runtime Permissions Constant
    int SYSTEM_ALERT_CODE = 900;

    //Login intent Constant
    String LOGIN_MOBILE = "login_mobile";

    //Toast Color Code
    int errorColor = Color.parseColor("#e81224");
    int succeedColor = Color.parseColor("#41B400");

    //Notification Constants
    String SETTING_NOTIFICATION_LISTENER = "enabled_notification_listeners";
    String ACTION_NOTIFICATION_LISTENER_SETTINGS = "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS";

}
