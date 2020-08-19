package com.example.protectionapp.utils;

import android.graphics.Color;

public interface AppConstant {

    //DB Constant
    String DATABASE_NAME = "protection_db";

    //Pref Constant
    String ISNIGHTMODE = "nightMode";
    String OVERLAY = "overlay";
    String MOSTUSED = "mostUsed";
    String RAREUSED = "rareUsed";
    String SCREENSHOT_ONVOLUME = "Screenshot_Onvolume_is_on";
    String IS_CALL_RECORDING_ON = "call_recording_is_on";
    String IS_SUBSCRIBE = "isSubscribe";
    String ISLOGGEDIN = "is_loggedIn";
    String USER_MOBILE = "user_mobile";
    String NOTIFICATION_ENABLE = "notifcation_enable";
    String PREF_PACKAGES_BLOCKED = "pref_packages_blocked";
    String FCMTOKEN = "fcm_token";
    String INVITED_BY = "invitedBy";
    String ISREFERED = "is_refered";

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
    String STUDENT_ID = "Student";
    String PASSPORT = "passport";

    //Runtime Permissions Constant
    int REQUEST_OVERLAY_PERMISSION = 900;

    //Login intent Constant
    String LOGIN_MOBILE = "login_mobile";

    //Toast Color Code
    int errorColor = Color.parseColor("#e81224");
    int succeedColor = Color.parseColor("#41B400");

    //Notification Constants
    String SETTING_NOTIFICATION_LISTENER = "enabled_notification_listeners";
    String ACTION_NOTIFICATION_LISTENER_SETTINGS = "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS";

    //BroadCast Reciever
    String OVERLAY_ACTION = "overlay_action";


//    String SOURCE = "source";
//    String SPEAKER_USE = "speaker_use";
//    String ENABLED = "enabled";
//    String FORMAT = "format";
//    String MODE = "mode";
//    String STORAGE = "storage";
//    String STORAGE_PATH = "storage_path";
}
