package com.example.protectionapp.utils;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.example.protectionapp.R;
import com.example.protectionapp.RecordsActivites.PersonalRecords;
import com.example.protectionapp.RecordsActivites.SendDailog;
import com.example.protectionapp.model.AdhaarBean;
import com.example.protectionapp.model.AtmBean;
import com.example.protectionapp.model.BankBean;
import com.example.protectionapp.model.BirthCertificateBean;
import com.example.protectionapp.model.DlicenceBean;
import com.example.protectionapp.model.FetchNotification;
import com.example.protectionapp.model.FileShareBean;
import com.example.protectionapp.model.MediaDocBean;
import com.example.protectionapp.model.PanBean;
import com.example.protectionapp.model.PassportBean;
import com.example.protectionapp.model.PersonalRecord;
import com.example.protectionapp.model.PlansBean;
import com.example.protectionapp.model.SosBean;
import com.example.protectionapp.model.StudentIdBean;
import com.example.protectionapp.model.UserBean;
import com.example.protectionapp.model.VoteridBean;
import com.example.protectionapp.utils.views.RoundView;
import com.firebase.client.Firebase;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.appinvite.AppInvite;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.jaiselrahman.filepicker.activity.FilePickerActivity;
import com.jaiselrahman.filepicker.config.Configurations;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Utils {
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

    public static Dialog getRegisteredUserList(Activity activity) {
        Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.user_list_dialog);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().setLayout(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT);
        return dialog;
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
        Dialog dialog = new Dialog(context,R.style.DialogFragmentTheme);
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
        Dialog dialog = new Dialog(context,R.style.DialogFragmentTheme);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.earned_dialog);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().setLayout(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT);
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
        Dialog dialog = new Dialog(activity,R.style.DialogFragmentTheme);
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
        Dialog dialog = new Dialog(activity,R.style.DialogFragmentTheme);
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
                SendDailog sendDailog = new SendDailog(activity, true);
                sendDailog.show(activity.getSupportFragmentManager(), "Send Dialog");
                dialog.dismiss();
                ((PersonalRecords) activity).setDocumentType(docType);
            }
        });
        dialog.show();
    }
}
