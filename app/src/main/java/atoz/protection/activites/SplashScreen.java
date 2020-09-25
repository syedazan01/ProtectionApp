package atoz.protection.activites;

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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.balram.locker.utils.Locker;
import com.balram.locker.view.AppLocker;
import com.balram.locker.view.LockActivity;
import atoz.protection.R;
import atoz.protection.services.FloatingWindowService;
import atoz.protection.services.ForgroundService;
import atoz.protection.utils.AppConstant;
import atoz.protection.utils.MIUIUtils;
import atoz.protection.utils.PrefManager;
import atoz.protection.utils.Utils;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;

import static atoz.protection.utils.AppConstant.REQUEST_OVERLAY_PERMISSION;

public class SplashScreen extends AppCompatActivity {

    private static final int REQUEST_CODE_SCREEN_SHOT = 1001;
    private MediaProjectionManager mpManager;
    private static final int APP_PERMISSION_REQUEST = 212;
//    LottieAnimationView animation;
    TextView tvSplashTitle;
    //HoverView hoverView;
    public static Activity mContext;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.changeColor(this, "#00000000", true);
        mContext = this;
        handleDeepLink();
        setContentView(R.layout.splash_screen_layout);
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getResources().getString(R.string.interstitalId));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
       /* if (!isMyServiceRunning(ForgroundService.class)) {
            Intent forrgroundIntent = new Intent(this, ForgroundService.class);
            forrgroundIntent.setAction(ForgroundService.ACTION_START_FOREGROUND_SERVICE);
            startService(forrgroundIntent);
        }*/
        if (PrefManager.getBoolean(AppConstant.OVERLAY)) {
            if(Utils.isMyFloatingServiceRunning(this))
                stopService(new Intent(this, FloatingWindowService.class));
            startService(new Intent(this, FloatingWindowService.class).setAction(FloatingWindowService.LAUNCHER_WIDGET));
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

            if (!AppLocker.getInstance().getAppLock().isPasscodeSet()) {
                startHandler();
            }
            else {
                int type =Locker.UNLOCK_PASSWORD;
                Intent intent = new Intent(this, LockActivity.class);
                intent.putExtra(Locker.TYPE, type);
                startActivityForResult(intent, type);
            }
        }
//        animation = findViewById(R.id.animation);
        tvSplashTitle = findViewById(R.id.tvSplashTitle);

//        animation.playAnimation();
        Utils.setShader(Color.BLUE, Color.GREEN, tvSplashTitle);
    }


    private void startHandler() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
//                animation.clearAnimation();
                finish();
                if (PrefManager.getBoolean(AppConstant.ISLOGGEDIN)) {
                    Intent intent = new Intent(SplashScreen.this, HomePage.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(SplashScreen.this, App_intro.class);
                    startActivity(intent);
                }
                if(mInterstitialAd.isLoaded())
                {
                    mInterstitialAd.show();
                }
            }

        }, 2500);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && (requestCode == REQUEST_OVERLAY_PERMISSION || requestCode == APP_PERMISSION_REQUEST)) {
//            startService(new Intent(this, FloatingWindowService.class));
        } else {
            Utils.showToast(this, "Draw over other app permission not enable.", AppConstant.errorColor);
        }
        startHandler();
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
                            Log.e("LINK>>>", deepLink.getPath());
                            Log.e("LINK>>>", "" + id);
                            if (id != null) {
                                PrefManager.putString(AppConstant.INVITED_BY, "+"+id.trim());
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
}
