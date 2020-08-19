package com.example.protectionapp.activites;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.example.protectionapp.R;
import com.example.protectionapp.services.FloatingWindowService;
import com.example.protectionapp.services.ForgroundService;
import com.example.protectionapp.utils.AppConstant;
import com.example.protectionapp.utils.MIUIUtils;
import com.example.protectionapp.utils.PrefManager;
import com.example.protectionapp.utils.Utils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;

import static com.example.protectionapp.utils.AppConstant.ISNIGHTMODE;
import static com.example.protectionapp.utils.AppConstant.REQUEST_OVERLAY_PERMISSION;

public class SplashScreen extends AppCompatActivity {
    private static final int APP_PERMISSION_REQUEST = 212;
    LottieAnimationView animation;
    TextView tvSplashTitle;
    //HoverView hoverView;
    public static Activity mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if (PrefManager.getBoolean(ISNIGHTMODE))
            setTheme(R.style.AppTheme_Base_Night);
        else
            setTheme(R.style.AppTheme_Base_Light);
        super.onCreate(savedInstanceState);
        mContext = this;
       /* List<PlansBean> plansBeans=new ArrayList<>();
        plansBeans.add(new PlansBean("200 \u20B9","Primium Plan","For 3 Month",""));
        plansBeans.add(new PlansBean("100 \u20B9","Silver Plan","For 2 Month",""));
        plansBeans.add(new PlansBean("50 \u20B9","Platinum Plan","For 4 Month",""));
        plansBeans.add(new PlansBean("20 \u20B9","Basic Plan","For 1 Month",""));
        for (PlansBean plansBean : plansBeans)
        {
            Utils.storePlansToRTD(this,plansBean);
        }*/

        setContentView(R.layout.splash_screen_layout);
//        checkAccessibilityPermission();
        if (!isMyServiceRunning(ForgroundService.class)) {
            Intent forrgroundIntent = new Intent(this, ForgroundService.class);
            forrgroundIntent.setAction(ForgroundService.ACTION_START_FOREGROUND_SERVICE);
            startService(forrgroundIntent);
        }
        if (PrefManager.getBoolean(AppConstant.OVERLAY)) {
            startService(new Intent(this, FloatingWindowService.class));
        }

        if (Build.VERSION.SDK_INT >= 19 && MIUIUtils.isMIUI() && !MIUIUtils.isFloatWindowOptionAllowed(this)) {
            Log.i("TAG", "MIUI DEVICE: Screen Overlay Not allowed");
            startActivityForResult(MIUIUtils.toFloatWindowPermission(this, getPackageName()), REQUEST_OVERLAY_PERMISSION);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, APP_PERMISSION_REQUEST);
        } else {
//            startService(new Intent(this, FloatingWindowService.class));
            startHandler();
            handleDeepLink();
        }
        animation = findViewById(R.id.animation);
        tvSplashTitle = findViewById(R.id.tvSplashTitle);

        animation.playAnimation();
        Utils.setShader(Color.BLUE, Color.GREEN, tvSplashTitle);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.d("TAG", "Key pressed");
        if (event.getAction() == KeyEvent.KEYCODE_VOLUME_DOWN) {
            MediaProjectionManager projectionManager = (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);
            startActivityForResult(projectionManager.createScreenCaptureIntent(), 900);
        }
//this prevents the key from performing the base function. Replace with super.onKeyDown to let it perform it's original function, after being consumed by your app.
        return true;

    }

    private void startHandler() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                animation.clearAnimation();
                finish();
                if (PrefManager.getBoolean(AppConstant.ISLOGGEDIN)) {
                    Intent intent = new Intent(SplashScreen.this, HomePage.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(SplashScreen.this, LogIn.class);
                    startActivity(intent);
                }
            }

        }, 2500);
    }

   /* @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE) {
            if (Settings.canDrawOverlays(this)) {
                showWindowManager();
            }
        }
    }
    public void showWindowManager() {
        if (requestPermission()) {
            return;
        }

        WindowManager.LayoutParams p =
                new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT,
                        WindowManager.LayoutParams.WRAP_CONTENT,
                        Build.VERSION.SDK_INT > Build.VERSION_CODES.O
                                ? WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
                                : WindowManager.LayoutParams.TYPE_SYSTEM_ERROR,
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                        PixelFormat.TRANSLUCENT);


        final WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        LayoutInflater layoutInflater =
                (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        final View popupView = layoutInflater.inflate(R.layout.window_manager_layout, null);
        windowManager.addView(popupView, p);

        // dismiss windowManager after 3s
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                windowManager.removeView(popupView);
            }
        }, 3000);
    }
    public boolean requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE);
                return true;
            }
        }
        return false;
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && (requestCode == REQUEST_OVERLAY_PERMISSION || requestCode == APP_PERMISSION_REQUEST)) {
//            startService(new Intent(this, FloatingWindowService.class));
        } else {
            Utils.showToast(this, "Draw over other app permission not enable.", AppConstant.errorColor);
        }
        handleDeepLink();
        startHandler();
    }

    private void handleDeepLink() {
        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                        Uri deepLink = null;
                        if (pendingDynamicLinkData != null) {
                            deepLink = pendingDynamicLinkData.getLink();
                            String id = deepLink.getQueryParameter(AppConstant.INVITED_BY);
                            Log.d("LINK>>>", deepLink.getPath());
                            Log.d("LINK>>>", "" + id);
                            if (id != null) {
                                PrefManager.putString(AppConstant.INVITED_BY, id);
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("LINK>>>", "getDynamicLink:onFailure", e);
                    }
                });
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public boolean checkAccessibilityPermission() {
        int accessEnabled = 0;
        try {
            accessEnabled = Settings.Secure.getInt(this.getContentResolver(), Settings.Secure.ACCESSIBILITY_ENABLED);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        if (accessEnabled == 0) {
            /** if not construct intent to request permission */
            Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            /** request permission via start activity for result */
            startActivity(intent);
            return false;
        } else {
            return true;
        }
    }
}
