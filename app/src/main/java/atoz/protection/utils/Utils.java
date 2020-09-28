package atoz.protection.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.graphics.drawable.GradientDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewParent;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.balram.locker.utils.Locker;
import com.balram.locker.view.AppLocker;
import com.balram.locker.view.LockActivity;
import atoz.protection.R;
import atoz.protection.RecordsActivites.SendDailog;
import atoz.protection.activites.LogIn;
import atoz.protection.interfacecallbacks.SpamCallsListener;
import atoz.protection.model.AdhaarBean;
import atoz.protection.model.AtmBean;
import atoz.protection.model.BankBean;
import atoz.protection.model.BirthCertificateBean;
import atoz.protection.model.DlicenceBean;
import atoz.protection.model.FetchNotification;
import atoz.protection.model.FileShareBean;
import atoz.protection.model.MediaDocBean;
import atoz.protection.model.PanBean;
import atoz.protection.model.PassportBean;
import atoz.protection.model.PayRequestBean;
import atoz.protection.model.PlansBean;
import atoz.protection.model.SosBean;
import atoz.protection.model.SpamBean;
import atoz.protection.model.StudentIdBean;
import atoz.protection.model.UserBean;
import atoz.protection.model.VoteridBean;
import atoz.protection.model.WalletHistory;
import atoz.protection.room.AppDatabase;
import atoz.protection.services.FloatingWindowService;
import atoz.protection.utils.views.RoundView;
import com.firebase.client.Firebase;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.appinvite.AppInvite;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.jaiselrahman.filepicker.activity.FilePickerActivity;
import com.jaiselrahman.filepicker.config.Configurations;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Utils {
    private static int[] getScreenResolution(Context context)
    {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        int width = metrics.widthPixels;
        int height = (int)(metrics.heightPixels*0.60);

        return new int[]{width,height};
    }
    public static void transparentStatusBar(Activity activity) {

//        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(activity, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(activity, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    public static void changeColor(Activity activity, String color, boolean isDark) {

//        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            if (isDark) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
            window.setStatusBarColor(Color.parseColor(color));
        } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            window.setStatusBarColor(Color.parseColor(color));
        }
    }

    public static void removeViewFormParent(View v) {
        if (v == null) return;
        ViewParent parent = v.getParent();
        if (parent instanceof FrameLayout) {
            ((FrameLayout) parent).removeView(v);
        }
    }

    private static void setWindowFlag(Activity activity, final int bits, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    public static void setShader(int startColor, int endColor, TextView tv) {
        Shader textShader = new LinearGradient(7, 2, 4, 10,
                new int[]{startColor, endColor},
                new float[]{0, 1}, Shader.TileMode.CLAMP);
        tv.getPaint().setShader(textShader);
    }

    public static void makeButton(Button button, int color, float radius) {
        button.setBackground(new RoundView(color, Utils.getRadius(radius)));
    }

    public static GradientDrawable getColoredDrawable(int startColor, int endColor) {
        int[] colors = {startColor, endColor};
        return new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, colors);
    }

    public static GradientDrawable getColoredDrawable(int startColor, int endColor, int shape, float cornerRadius) {
        int[] colors = {startColor, endColor};
        GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, colors);
        gradientDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        gradientDrawable.setShape(shape);
        gradientDrawable.setCornerRadius(cornerRadius);
        return gradientDrawable;
    }

    public static GradientDrawable getThemeGradient(float cornerRadius) {
        return getColoredDrawable(Color.parseColor("#0077FF"), Color.parseColor("#003C80"), GradientDrawable.RECTANGLE, cornerRadius);
    }

    public static GradientDrawable getDisableColorButton(float cornerRadius) {
        return getColoredDrawable(Color.parseColor("#DFDFDF"), Color.parseColor("#DFDFDF"), GradientDrawable.RECTANGLE, cornerRadius);
    }

    public static GradientDrawable getColoredDrawable(int startColor, int endColor, int shape, float cornerRadius, int strokeWidth, int strokeColor) {
        int[] colors = {startColor, endColor};
        GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, colors);
        gradientDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        gradientDrawable.setShape(shape);
        gradientDrawable.setStroke(strokeWidth, strokeColor);
        gradientDrawable.setCornerRadius(cornerRadius);
        return gradientDrawable;
    }

    public static Bitmap addGradientToImage(Bitmap originalBitmap, int startColor, int endColor) {
        int width = originalBitmap.getWidth();
        int height = originalBitmap.getHeight();
        Bitmap updatedBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(updatedBitmap);

        canvas.drawBitmap(originalBitmap, 0, 0, null);

        Paint paint = new Paint();
        LinearGradient shader = new LinearGradient(0, 0, 0, height, startColor, endColor, Shader.TileMode.CLAMP);
        paint.setShader(shader);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawRect(0, 0, width, height, paint);

        return updatedBitmap;
    }

    public static float[] getRadius(float value) {
        return new float[]{value, value, value, value, value, value, value, value};
    }

    public static void storeDocumentsInRTD(String child, String modelString) {
        Firebase reference;
        reference = new Firebase(AppConstant.FIREBASE_DATABASE_URL + AppConstant.PERSONAL_DOCUMENT + "/" + child + "/");
        if (child.equals(AppConstant.ADHAAR)) {
            AdhaarBean adhaarBean = fromJson(modelString, AdhaarBean.class);
            reference.push().setValue(adhaarBean);
        } else if (child.equals(AppConstant.PAN)) {
            PanBean panBean = fromJson(modelString, PanBean.class);
            reference.push().setValue(panBean);
        } else if (child.equals(AppConstant.DRIVING_LICENSE)) {
            DlicenceBean dlicenceBean = fromJson(modelString, DlicenceBean.class);
            reference.push().setValue(dlicenceBean);
        } else if (child.equals(AppConstant.BANK)) {
            BankBean bankBean = fromJson(modelString, BankBean.class);
            reference.push().setValue(bankBean);
        } else if (child.equals(AppConstant.ATM)) {
            AtmBean atmBean = fromJson(modelString, AtmBean.class);
            reference.push().setValue(atmBean);
        } else if (child.equals(AppConstant.VOTER_ID)) {
            VoteridBean voteridBean = fromJson(modelString, VoteridBean.class);
            reference.push().setValue(voteridBean);
        } else if (child.equals(AppConstant.STUDENT_ID)) {
            StudentIdBean studentIdBean = fromJson(modelString, StudentIdBean.class);
            reference.push().setValue(studentIdBean);
        } else if (child.equals(AppConstant.PASSPORT)) {
            PassportBean passportBean = fromJson(modelString, PassportBean.class);
            reference.push().setValue(passportBean);
        } else if (child.equals(AppConstant.BIRTH_CERTIFICATE)) {
            BirthCertificateBean birthCertificateBean = fromJson(modelString, BirthCertificateBean.class);
            reference.push().setValue(birthCertificateBean);
        } else if (child.equals(AppConstant.MEDIA_DOC)) {
            MediaDocBean mediaDocBean = fromJson(modelString, MediaDocBean.class);
            reference.push().setValue(mediaDocBean);
        }
    }

    public static void storeNotificationInRTD(FetchNotification fetchNotification) {
        Firebase reference;

        reference = new Firebase(AppConstant.FIREBASE_DATABASE_URL + AppConstant.NOTIFICATION + "/");
        fetchNotification.setPush_key(reference.push().getKey());
        reference.child(fetchNotification.getPush_key()).setValue(fetchNotification);
    }

    public static void storeSosNumbersInRTD(SosBean sosBean) {
        Firebase reference;

        reference = new Firebase(AppConstant.FIREBASE_DATABASE_URL + AppConstant.SOS + "/");
        sosBean.setPushKey(reference.push().getKey());
        reference.child(sosBean.getPushKey()).setValue(sosBean);
    }

    public static void removeSosNumbersInRTD(String pushKey) {
        Firebase reference;

        reference = new Firebase(AppConstant.FIREBASE_DATABASE_URL + AppConstant.SOS + "/");
        reference.child(pushKey).removeValue();
    }

    public static <T> String toJson(T value, Class<T> model) {
        return new Gson().toJson(value, model);
    }

    public static <T> T fromJson(String json, Class<T> model) {
        return new Gson().fromJson(json, model);
    }

    public static GoogleApiClient createGoogleClient(FragmentActivity activity, GoogleApiClient.OnConnectionFailedListener connectionFailedListener) {

        return new GoogleApiClient.Builder(activity)
                .enableAutoManage(activity, connectionFailedListener)
                .addApi(AppInvite.API)
                .build();
    }

    public static void showToast(Activity activity, String mes, int color) {
        String msg = "Something gone wrong.";
        if (mes == null) {
            mes = msg;
        } else if (mes.equalsIgnoreCase("")) {
            mes = msg;
        }
        hideKeyboard(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast_layout, activity.findViewById(R.id.toast_layout_root));
        LinearLayout root = layout.findViewById(R.id.toast_layout_root);
        root.setBackground(new RoundView(color, Utils.getRadius(100f)));
        TextView toastTextView = layout.findViewById(R.id.toastTextView);
        toastTextView.setText(mes);
        Toast toast = new Toast(activity);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

    public static void hideKeyboard(Activity _activity) {
        View view = _activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) _activity
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(),
                    0);
        }
    }

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void storeUserDetailsToRTD(UserBean userBean) {
        Firebase reference;

        reference = new Firebase(AppConstant.FIREBASE_DATABASE_URL + AppConstant.USER_DETAIL + "/");
        reference.child(PrefManager.getString(AppConstant.USER_MOBILE)).setValue(userBean);
    }
    public static void storeRewardedUserDetailsToRTD(UserBean userBean) {
        Firebase reference;

        reference = new Firebase(AppConstant.FIREBASE_DATABASE_URL + AppConstant.USER_DETAIL + "/");
        reference.child(userBean.getMobile()).setValue(userBean);
    }

    public static void storeWalletHistory(WalletHistory walletHistory) {
        Firebase reference;

        reference = new Firebase(AppConstant.FIREBASE_DATABASE_URL + AppConstant.WALLET_HISTORY + "/");
        reference.push().setValue(walletHistory);
    }

    public static void storeWithodrawalRequest(PayRequestBean payRequestBean) {
        Firebase reference;

        reference = new Firebase(AppConstant.FIREBASE_DATABASE_URL + AppConstant.PAY_REQUEST + "/");
        reference.push().setValue(payRequestBean);
    }

    public static void storeFileShareToRTD(FileShareBean fileShareBean) {
        Firebase reference;

        reference = new Firebase(AppConstant.FIREBASE_DATABASE_URL + AppConstant.FILE_SHARE + "/");
        reference.push().setValue(fileShareBean);
    }

    public static void storePlansToRTD(PlansBean plansBean) {
        Firebase reference;

        reference = new Firebase(AppConstant.FIREBASE_DATABASE_URL + AppConstant.PLANS + "/");
        reference.push().setValue(plansBean);
    }

    public static Firebase getUserReference() {
        Firebase reference;

        reference = new Firebase(AppConstant.FIREBASE_DATABASE_URL + AppConstant.USER_DETAIL + "/");
        return reference;
    }
    public static Firebase getMyWalletHistoryReference() {
        Firebase reference;

        reference = new Firebase(AppConstant.FIREBASE_DATABASE_URL + AppConstant.WALLET_HISTORY + "/");
        return reference;
    }

    public static Firebase getSosReference() {
        Firebase reference;

        reference = new Firebase(AppConstant.FIREBASE_DATABASE_URL + AppConstant.SOS + "/");
        return reference;
    }

    public static Firebase getPlansReference() {
        Firebase reference;

        reference = new Firebase(AppConstant.FIREBASE_DATABASE_URL + AppConstant.PLANS + "/");
        return reference;
    }

    public static Firebase getPersonalDocReference(String child) {
        Firebase reference;
        reference = new Firebase(AppConstant.FIREBASE_DATABASE_URL + AppConstant.PERSONAL_DOCUMENT + "/" + child + "/");
        return reference;
    }

    public static Firebase getNotificationReference() {
        Firebase reference;

        reference = new Firebase(AppConstant.FIREBASE_DATABASE_URL + AppConstant.NOTIFICATION + "/");
        return reference;
    }

    public static Firebase getFileShareReference() {
        Firebase reference;

        reference = new Firebase(AppConstant.FIREBASE_DATABASE_URL + AppConstant.FILE_SHARE + "/");
        return reference;
    }

    public static StorageReference getStorageReference() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        return storage.getReferenceFromUrl(AppConstant.FIREBASE_STORAGE_DATABASE_URL);
    }

    public static SharedPreferences getDefaultManager(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static BottomSheetDialog getRegisteredUserList(Activity activity) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(activity, R.style.AppBottomSheetDialogTheme);
        bottomSheetDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        bottomSheetDialog.setContentView(R.layout.user_list_dialog);
//        bottomSheetDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        bottomSheetDialog.findViewById(R.id.scrollView).setLayoutParams(new LinearLayout.LayoutParams(Utils.getScreenResolution(activity)[0],Utils.getScreenResolution(activity)[1]));
        bottomSheetDialog.findViewById(R.id.btnSend).setBackground(Utils.getThemeGradient(50F));
        return bottomSheetDialog;
    }

    public static void showSpamCallDialog(Activity activity) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(activity, R.style.AppBottomSheetDialogTheme);
        bottomSheetDialog.setContentView(R.layout.spam_call_dialog);
        Button btnSaveSpamCalls = bottomSheetDialog.findViewById(R.id.btnSaveSpamCalls);
        EditText etCallerName, etCallerNumber;
        etCallerName = bottomSheetDialog.findViewById(R.id.etCallerName);
        etCallerNumber = bottomSheetDialog.findViewById(R.id.etCallerNumber);
        btnSaveSpamCalls.setBackground(getThemeGradient(50F));
        btnSaveSpamCalls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(etCallerName.getText())) {
                    showToast(activity, "Enter Caller Name", AppConstant.errorColor);
                    return;
                }
                if (TextUtils.isEmpty(etCallerNumber.getText())) {
                    showToast(activity, "Enter Caller Number", AppConstant.errorColor);
                    return;
                }
                Executors.newSingleThreadExecutor().execute(new Runnable() {
                    @Override
                    public void run() {

                        SpamBean spamBean = new SpamBean();
                        spamBean.setChecked(true);
                        spamBean.setCallName(etCallerName.getText().toString().trim());
                        spamBean.setCallerNumber(etCallerNumber.getText().toString().trim());
                        AppDatabase.getAppDataBase(activity).getSpamCallDao().insertCalls(spamBean);
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                SpamCallsListener spamCallsListener = (SpamCallsListener) activity;
                                spamCallsListener.onSaved(spamBean);
                            }
                        });
                    }
                });
                bottomSheetDialog.dismiss();
            }
        });
        bottomSheetDialog.show();
    }

    public static void showSpamCallDeleteDialog(Activity activity, int id, int position) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(activity, R.style.AppBottomSheetDialogTheme);
        bottomSheetDialog.setContentView(R.layout.spam_call_delete_dialog);
        Button btnOk = bottomSheetDialog.findViewById(R.id.btnOK);
        Button btnCancel = bottomSheetDialog.findViewById(R.id.btnCancel);
        btnOk.setBackground(getThemeGradient(50F));
        btnCancel.setBackground(getColoredDrawable(activity.getResources().getColor(R.color.black), activity.getResources().getColor(R.color.black), GradientDrawable.RECTANGLE, 50F));
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Executors.newSingleThreadExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        AppDatabase.getAppDataBase(activity).getSpamCallDao().deleteSpamCall(id);

                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                SpamCallsListener spamCallsListener = (SpamCallsListener) activity;
                                spamCallsListener.onDeleted(position);
                            }
                        });
                    }
                });
                bottomSheetDialog.dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
            }
        });
        bottomSheetDialog.show();
    }
    public static void showSignOutDialog(Activity activity) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(activity, R.style.AppBottomSheetDialogTheme);
        bottomSheetDialog.setContentView(R.layout.logout_dialog);
        Button btnOk = bottomSheetDialog.findViewById(R.id.btnOK);
        Button btnCancel = bottomSheetDialog.findViewById(R.id.btnCancel);
        btnOk.setBackground(getThemeGradient(50F));
        btnCancel.setBackground(getColoredDrawable(activity.getResources().getColor(R.color.black), activity.getResources().getColor(R.color.black), GradientDrawable.RECTANGLE, 50F));
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isMyFloatingServiceRunning(activity))
                {
                    activity.stopService(new Intent(activity,FloatingWindowService.class));
                }
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();
                PrefManager.clear();
                Utils.getDefaultManager(activity).edit().clear().apply();
                bottomSheetDialog.dismiss();
                activity.finishAffinity();
                activity.startActivity(new Intent(activity, LogIn.class));
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
            }
        });
        bottomSheetDialog.show();
    }
    public static Dialog getMsgDialog(Activity activity) {
        Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.msg_dialog);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().setLayout(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT);
        return dialog;
    }

    public static ProgressDialog getProgressDialog(Activity activity) {
        ProgressDialog pd = new ProgressDialog(activity);
        pd.setTitle("Loading...");
        pd.setCancelable(false);
        return pd;
    }

    public static String getTimeAgo(String dataDate) {
        String convTime = null;

        String prefix = "";
        String suffix = "Ago";

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            Date pasTime = dateFormat.parse(dataDate);

            Date nowTime = new Date();

            long dateDiff = nowTime.getTime() - pasTime.getTime();

            long second = TimeUnit.MILLISECONDS.toSeconds(dateDiff);
            long minute = TimeUnit.MILLISECONDS.toMinutes(dateDiff);
            long hour = TimeUnit.MILLISECONDS.toHours(dateDiff);
            long day = TimeUnit.MILLISECONDS.toDays(dateDiff);

            if (second < 60) {
                convTime = second + " Seconds " + suffix;
            } else if (minute < 60) {
                convTime = minute + " Minutes " + suffix;
            } else if (hour < 24) {
                convTime = hour + " Hours " + suffix;
            } else if (day >= 7) {
                if (day > 360) {
                    convTime = (day / 360) + " Years " + suffix;
                } else if (day > 30) {
                    convTime = (day / 30) + " Months " + suffix;
                } else {
                    convTime = (day / 7) + " Week " + suffix;
                }
            } else if (day < 7) {
                convTime = day + " Days " + suffix;
            }

        } catch (ParseException e) {
            e.printStackTrace();
            Log.e("ConvTimeE", e.getMessage());
        }

        return convTime;
    }

    public static void showNoSubsDialog(Context context) {
        Dialog dialog = new Dialog(context, R.style.DialogFragmentTheme);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.no_subscribe_dialog);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().setLayout(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT);
        dialog.show();
        Button btnOk = dialog.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    public static void showCongratsDialog(Context context) {
        Dialog dialog = new Dialog(context, R.style.DialogFragmentTheme);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.earned_dialog);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().setLayout(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT);
        PrefManager.putBoolean(AppConstant.ISREFERED,false);
        dialog.show();
        LottieAnimationView lottieAnimationView = dialog.findViewById(R.id.lottie);
        lottieAnimationView.playAnimation();
        Button btnOk = dialog.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    public static void showDocsDialog(Activity activity) {
        Dialog dialog = new Dialog(activity, R.style.DialogFragmentTheme);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.media_picker_dialog);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().setLayout(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT);
        LinearLayout llMedia, llPdf;
        llMedia = dialog.findViewById(R.id.llMedia);
        llPdf = dialog.findViewById(R.id.llPdf);
        llMedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.Companion.with(activity)
                        .crop()                    //Crop image(Optional), Check Customization for more option
                        .compress(1024)            //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
                dialog.dismiss();
            }
        });
        llPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, FilePickerActivity.class);
                intent.putExtra(FilePickerActivity.CONFIGS, new Configurations.Builder()
                        .setCheckPermission(true)
                        .setShowImages(false)
                        .enableImageCapture(false)
                        .setMaxSelection(1)
                        .setShowAudios(false)
                        .setShowFiles(true)
                        .enableVideoCapture(false)
                        .setShowVideos(false)
                        .setSingleChoiceMode(true)
                        .setSingleClickSelection(true)
                        .setSkipZeroSizeFiles(true)
                        .setSuffixes("pdf")
                        .build());
                activity.startActivityForResult(intent, AppConstant.CHOOSE_PDF_REQUESTCODE);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public static void showMediaChooseDialog(String docType, String docUrl, AppCompatActivity activity) {
        Dialog dialog = new Dialog(activity, R.style.DialogFragmentTheme);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.media_send_view_dialog);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().setLayout(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT);
        LinearLayout llView, llSend;
        llView = dialog.findViewById(R.id.llView);
        llSend = dialog.findViewById(R.id.llSend);
        llView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProgressDialog pd = getProgressDialog(activity);
                pd.show();
                Utils.getStorageReference()
                        .child(AppConstant.MEDIA_DOC + "/" + docUrl)
                        .getDownloadUrl()
                        .addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                pd.dismiss();
                                Intent it = new Intent(Intent.ACTION_VIEW);
                                String mediaType;
                                if (docType.equals(AppConstant.DOC_IMAGE))
                                    mediaType = "image/*";
                                else
                                    mediaType = "application/pdf";
                                it.setDataAndType(task.getResult(), mediaType);
                                activity.startActivity(it);
                            }
                        });
                dialog.dismiss();
            }
        });
        llSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendDailog sendDailog = new SendDailog(activity, true, R.style.AppBottomSheetDialogTheme);
                LayoutInflater layoutInflater = activity.getLayoutInflater();
                View bootomSheetView = layoutInflater.inflate(R.layout.senddailog_bottomsheet, null);
                sendDailog.setContentView(bootomSheetView);
                sendDailog.show();
                //sendDailog.show(activity.getSupportFragmentManager(), "Send Dialog");
                dialog.dismiss();
//                ((PersonalRecords) activity).setDocumentType(docType);
            }
        });
        dialog.show();
    }

    public static double getStatusBarHeight(Context context) {
        int statusBarHeight = 0;
        //获取status_bar_height资源的ID
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            statusBarHeight = context.getResources().getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }

    /**
     * 获取竖屏下状态栏高度
     */
    public static double getStatusBarHeightPortrait(Context context) {
        int statusBarHeight = 0;
        //获取status_bar_height_portrait资源的ID
        int resourceId = context.getResources().getIdentifier("status_bar_height_portrait", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            statusBarHeight = context.getResources().getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }

    /**
     * 获取NavigationBar的高度
     */
    public static int getNavigationBarHeight(Context context) {
        if (!hasNavigationBar(context)) {
            return 0;
        }
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height",
                "dimen", "android");
        //获取NavigationBar的高度
        return resources.getDimensionPixelSize(resourceId);
    }

    /**
     * 是否存在NavigationBar
     */
    public static boolean hasNavigationBar(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Display display = getWindowManager(context).getDefaultDisplay();
            Point size = new Point();
            Point realSize = new Point();
            display.getSize(size);
            display.getRealSize(realSize);
            return realSize.x != size.x || realSize.y != size.y;
        } else {
            boolean menu = ViewConfiguration.get(context).hasPermanentMenuKey();
            boolean back = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
            return !(menu || back);
        }
    }

    /**
     * 获取屏幕宽度
     */
    public static int getScreenWidth(Context context, boolean isIncludeNav) {
        if (isIncludeNav) {
            return context.getResources().getDisplayMetrics().widthPixels + getNavigationBarHeight(context);
        } else {
            return context.getResources().getDisplayMetrics().widthPixels;
        }
    }

    /**
     * 获取屏幕高度
     */
    public static int getScreenHeight(Context context, boolean isIncludeNav) {
        if (isIncludeNav) {
            return context.getResources().getDisplayMetrics().heightPixels + getNavigationBarHeight(context);
        } else {
            return context.getResources().getDisplayMetrics().heightPixels;
        }
    }

    /**
     * 获取Activity
     */
    public static Activity scanForActivity(Context context) {
        if (context == null) return null;
        if (context instanceof Activity) {
            return (Activity) context;
        } else if (context instanceof ContextWrapper) {
            return scanForActivity(((ContextWrapper) context).getBaseContext());
        }
        return null;
    }

    /**
     * dp转为px
     */
    public static int dp2px(Context context, float dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, context.getResources().getDisplayMetrics());
    }

    /**
     * sp转为px
     */
    public static int sp2px(Context context, float dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, dpValue, context.getResources().getDisplayMetrics());
    }

    /**
     * 如果WindowManager还未创建，则创建一个新的WindowManager返回。否则返回当前已创建的WindowManager。
     */
    public static WindowManager getWindowManager(Context context) {
        return (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    }

    /**
     * 边缘检测
     */
    public static boolean isEdge(Context context, MotionEvent e) {
        int edgeSize = dp2px(context, 40);
        return e.getRawX() < edgeSize
                || e.getRawX() > getScreenWidth(context, true) - edgeSize
                || e.getRawY() < edgeSize
                || e.getRawY() > getScreenHeight(context, true) - edgeSize;
    }


    public static final int NO_NETWORK = 0;
    public static final int NETWORK_CLOSED = 1;
    public static final int NETWORK_ETHERNET = 2;
    public static final int NETWORK_WIFI = 3;
    public static final int NETWORK_MOBILE = 4;
    public static final int NETWORK_UNKNOWN = -1;

    /**
     * 判断当前网络类型
     */
    public static int getNetworkType(Context context) {
        //改为context.getApplicationContext()，防止在Android 6.0上发生内存泄漏
        ConnectivityManager connectMgr = (ConnectivityManager) context.getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectMgr == null) {
            return NO_NETWORK;
        }

        NetworkInfo networkInfo = connectMgr.getActiveNetworkInfo();
        if (networkInfo == null) {
            // 没有任何网络
            return NO_NETWORK;
        }
        if (!networkInfo.isConnected()) {
            // 网络断开或关闭
            return NETWORK_CLOSED;
        }
        if (networkInfo.getType() == ConnectivityManager.TYPE_ETHERNET) {
            // 以太网网络
            return NETWORK_ETHERNET;
        } else if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            // wifi网络，当激活时，默认情况下，所有的数据流量将使用此连接
            return NETWORK_WIFI;
        } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
            // 移动数据连接,不能与连接共存,如果wifi打开，则自动关闭
            switch (networkInfo.getSubtype()) {
                // 2G
                case TelephonyManager.NETWORK_TYPE_GPRS:
                case TelephonyManager.NETWORK_TYPE_EDGE:
                case TelephonyManager.NETWORK_TYPE_CDMA:
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                case TelephonyManager.NETWORK_TYPE_IDEN:
                    // 3G
                case TelephonyManager.NETWORK_TYPE_UMTS:
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                case TelephonyManager.NETWORK_TYPE_HSPA:
                case TelephonyManager.NETWORK_TYPE_EVDO_B:
                case TelephonyManager.NETWORK_TYPE_EHRPD:
                case TelephonyManager.NETWORK_TYPE_HSPAP:
                    // 4G
                case TelephonyManager.NETWORK_TYPE_LTE:
                    // 5G
                case TelephonyManager.NETWORK_TYPE_NR:
                    return NETWORK_MOBILE;
            }
        }
        // 未知网络
        return NETWORK_UNKNOWN;
    }

    /**
     * 通过反射获取Application
     *
     * @deprecated 不在使用，后期谷歌可能封掉改接口
     */
    @SuppressLint("PrivateApi")
    @Deprecated
    public static Application getApplication() {
        try {
            return (Application) Class.forName("android.app.ActivityThread")
                    .getMethod("currentApplication").invoke(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取当前系统时间
     */
    public static String getCurrentSystemTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        Date date = new Date();
        return simpleDateFormat.format(date);
    }

    /**
     * 格式化时间
     */
    public static String stringForTime(int timeMs) {
        int totalSeconds = timeMs / 1000;

        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;

        if (hours > 0) {
            return String.format(Locale.getDefault(), "%d:%02d:%02d", hours, minutes, seconds);
        } else {
            return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        }
    }

    /**
     * 获取集合的快照
     */
    @NonNull
    public static <T> List<T> getSnapshot(@NonNull Collection<T> other) {
        List<T> result = new ArrayList<>(other.size());
        for (T item : other) {
            if (item != null) {
                result.add(item);
            }
        }
        return result;
    }

    public static boolean isMyFloatingServiceRunning(Activity activity) {
        ActivityManager manager = (ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (FloatingWindowService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static void showMediaChooseBottomSheet(Activity activity) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(activity, R.style.AppBottomSheetDialogTheme);
        bottomSheetDialog.setContentView(R.layout.imagepicker_layout);


        CardView cardViewCam, cardViewGall;
        cardViewCam = bottomSheetDialog.findViewById(R.id.cardviewCAm);
        cardViewGall = bottomSheetDialog.findViewById(R.id.cardviewGallery);
        cardViewCam.setBackground(getThemeGradient(150F));
        cardViewGall.setBackground(getThemeGradient(150F));
        cardViewGall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.Companion.with(activity)
                        .crop()//Crop image(Optional), Check Customization for more option
                        .compress(1024)            //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                        .galleryOnly()
                        .start();
                bottomSheetDialog.dismiss();
            }
        });

        cardViewCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.Companion.with(activity)
                        .crop()//Crop image(Optional), Check Customization for more option
                        .compress(1024)            //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                        .cameraOnly()
                        .start();
                bottomSheetDialog.dismiss();
            }
        });
        bottomSheetDialog.show();
    }

    public static int getActiveTintColor(Activity activity) {
        return ContextCompat.getColor(activity, R.color.themeColor);
    }

    public static int getInActiveTintColor(Activity activity) {
        return ContextCompat.getColor(activity, R.color.inactiveColor);
    }

    public static void setBlueLightTheme(Activity activity, ImageView ivBlueLightFilter) {
        ivBlueLightFilter.setColorFilter(ContextCompat.getColor(activity, PrefManager.getBoolean(AppConstant.ISBLUELIGHT) ? R.color.black : R.color.themeColor), PorterDuff.Mode.MULTIPLY);
        PrefManager.putBoolean(AppConstant.ISBLUELIGHT, !PrefManager.getBoolean(AppConstant.ISBLUELIGHT));
        activity.startService(new Intent(activity, FloatingWindowService.class).setAction(FloatingWindowService.BLUE_LIGHT_FILTER));
    }

    public static void showAppLockDialog(Activity activity) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(activity, R.style.AppBottomSheetDialogTheme);
        bottomSheetDialog.setContentView(R.layout.applock_dialog);
        Switch swAppLock = bottomSheetDialog.findViewById(R.id.swEnableApplock);
        CardView cardAppLock = bottomSheetDialog.findViewById(R.id.cardAppLock);
        Button btnPin = bottomSheetDialog.findViewById(R.id.btnPin);
        btnPin.setBackground(getThemeGradient(50F));
        if (AppLocker.getInstance().getAppLock().isPasscodeSet()) {
            btnPin.setText("Change Passcode PIN");
            cardAppLock.setVisibility(View.VISIBLE);
            swAppLock.setChecked(true);
        }
        swAppLock.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                int type = AppLocker.getInstance().getAppLock().isPasscodeSet() ? Locker.DISABLE_PASSLOCK
                        : Locker.ENABLE_PASSLOCK;
                Intent intent = new Intent(activity, LockActivity.class);
                intent.putExtra(Locker.TYPE, type);
                activity.startActivityForResult(intent, type);
                bottomSheetDialog.dismiss();
            }
        });
        btnPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AppLocker.getInstance().getAppLock().isPasscodeSet()) {
                    Intent intent = new Intent(activity, LockActivity.class);
                    intent.putExtra(Locker.TYPE, Locker.CHANGE_PASSWORD);
                    intent.putExtra(Locker.MESSAGE,
                            "Enter Old Passcode PIN");
                    activity.startActivityForResult(intent, Locker.CHANGE_PASSWORD);

                } else {
                    int type = Locker.ENABLE_PASSLOCK;
                    Intent intent = new Intent(activity, LockActivity.class);
                    intent.putExtra(Locker.TYPE, type);
                    activity.startActivityForResult(intent, type);
                    bottomSheetDialog.dismiss();
                }
            }
        });
        bottomSheetDialog.show();

    }

    public static void setupGooglePay(Activity activity,String amount)
    {
        Uri uri =
                new Uri.Builder()
                        .scheme("upi")
                        .authority("pay")
                        .appendQueryParameter("pa", "8529112711@kotak")       // virtual ID
                        .appendQueryParameter("pn", "A2zCreation")          // name
//                        .appendQueryParameter("mc", "your-merchant-code")          // optional
//                        .appendQueryParameter("tr", "your-transaction-ref-id")     // optional
                        .appendQueryParameter("tn", "Purchase plans")       // any note about payment
                        .appendQueryParameter("am", amount)           // amount
                        .appendQueryParameter("cu", "INR")                         // currency
//                        .appendQueryParameter("url", "your-transaction-url")       // optional
                        .build();
        String GOOGLE_PAY_PACKAGE_NAME = "com.google.android.apps.nbu.paisa.user";
        int GOOGLE_PAY_REQUEST_CODE = AppConstant.UPI_PAYMENT;

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(uri);
        intent.setPackage(GOOGLE_PAY_PACKAGE_NAME);
        try {
            activity.startActivityForResult(intent, GOOGLE_PAY_REQUEST_CODE);
        } catch (Exception e) {
            e.printStackTrace();
            showToast(activity,"Please Install Google Pay First",AppConstant.errorColor);
        }
    }
    public static boolean isOnline(Context context) {

        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            //should check null because in airplane mode it will be null
            return (netInfo != null && netInfo.isConnected());
        } catch (NullPointerException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static String getDaysLeft(String inputDateString)
    {
        Calendar calCurr = Calendar.getInstance();
        Calendar day = Calendar.getInstance();
        String remainingDate="";
        try {
            day.setTime(Objects.requireNonNull(new SimpleDateFormat("dd/MMM/yyyy", Locale.getDefault()).parse(inputDateString)));
            long diff=day.getTimeInMillis()-calCurr.getTimeInMillis();
            long daysLeft=TimeUnit.DAYS.convert(diff,TimeUnit.MILLISECONDS);
            if (daysLeft>0) {
                remainingDate="Days Left: "+daysLeft;
            }
            else
                remainingDate="";
        } catch (ParseException e) {
            e.printStackTrace();
            remainingDate="";
        }
        return remainingDate;
    }
}
