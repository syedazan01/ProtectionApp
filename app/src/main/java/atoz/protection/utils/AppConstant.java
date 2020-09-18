package atoz.protection.utils;

import android.graphics.Color;

public interface AppConstant {

    //DB Constant
    String DATABASE_NAME = "protection_db";

    //Pref Constant
    String ISBLUELIGHT = "blueLightFilter";
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
    String ADHAAR = "Adhaar";
    String PAN = "PAN";
    String DRIVING_LICENSE = "Driving license";
    String BANK = "Bank";
    String ATM = "ATM";
    String VOTER_ID = "Voter id";
    String NOTIFICATION = "Notification";
    String FILE_SHARE = "File Share";
    String PLANS = "Plans";
    String STUDENT_ID = "Student";
    String PASSPORT = "Passport";
    String BIRTH_CERTIFICATE = "Birth certificate";
    String DOC_IMAGE = "Image";
    String PDF_DOC = "Pdf";
    String MEDIA_DOC = "Media Docs";
    String SOS = "Sos";

    //Runtime Permissions Constant
    int REQUEST_OVERLAY_PERMISSION = 900;

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

    //BroadCast Reciever
    String OVERLAY_ACTION = "overlay_action";


    int CHOOSE_PDF_REQUESTCODE = 666;
}
