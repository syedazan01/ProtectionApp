package com.example.protectionapp.activites;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.example.protectionapp.R;
import com.example.protectionapp.utils.AppConstant;
import com.example.protectionapp.utils.PrefManager;
import com.example.protectionapp.utils.Utils;

import static com.example.protectionapp.utils.AppConstant.ISNIGHTMODE;

public class SplashScreen extends AppCompatActivity {
LottieAnimationView animation;
TextView tvSplashTitle;
//HoverView hoverView;
Context mContext=this;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if (PrefManager.getBoolean(ISNIGHTMODE))
            setTheme(R.style.AppTheme_Base_Night);
        else
            setTheme(R.style.AppTheme_Base_Light);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.splash_screen_layout);
        animation = findViewById(R.id.animation);
//        hoverView=findViewById(R.id.hoverView);
        tvSplashTitle = findViewById(R.id.tvSplashTitle);
        animation.playAnimation();
        Utils.setShader(Color.BLUE, Color.GREEN, tvSplashTitle);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.SYSTEM_ALERT_WINDOW) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SYSTEM_ALERT_WINDOW}, AppConstant.SYSTEM_ALERT_CODE);
        }
//        getSupportActionBar().hide();
//        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
//
//
//        View overlay = inflater.inflate(R.layout.floating_layout, null);
// in Activity Context
     /*   ImageView icon = new ImageView(this); // Create an icon
        icon.setImageResource(R.drawable.login_logo);

        FloatingActionButton actionButton = new FloatingActionButton.Builder(this)
                .setContentView(icon)
                .build();
        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(this);
// repeat many times:
        ImageView itemIcon = new ImageView(this);
        itemIcon.setImageResource(R.drawable.login_logo);
        SubActionButton button1 = itemBuilder.setContentView(itemIcon).build();
        FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(this)
                .addSubActionView(button1)
                // ...
                .attachTo(actionButton)
                .build();*/
//     startService(new Intent(this, FloatingWindowService.class));
     /*   final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                PixelFormat.TRANSLUCENT);


        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        if (overlay.isShown()){
            wm.updateViewLayout(overlay, params);
        }*/

//actionMenu.open(true);
            // add overlay
//            wm.addView(actionMenu, params);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                animation.clearAnimation();
                finish();
                if (PrefManager.getBoolean(AppConstant.ISLOGGEDIN)) {
                    Intent intent = new Intent(SplashScreen.this,HomePage.class);
                    startActivity(intent);
                }
                else
                {
                    Intent intent = new Intent(SplashScreen.this,LogIn.class);
                    startActivity(intent);
                }
            }

        },2500);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==AppConstant.SYSTEM_ALERT_CODE)
        {
            if(grantResults[0]== PackageManager.PERMISSION_GRANTED)
            {
            }
        }
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
}
