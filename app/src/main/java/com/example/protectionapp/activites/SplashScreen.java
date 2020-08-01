package com.example.protectionapp.activites;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.example.protectionapp.R;
import com.example.protectionapp.hoverwindow.HoverService;
import com.example.protectionapp.hoverwindow.MyHoverMenu;
import com.example.protectionapp.utils.AppConstant;
import com.example.protectionapp.utils.PrefManager;
import com.example.protectionapp.utils.Utils;

import io.mattcarroll.hover.HoverView;
import io.mattcarroll.hover.window.WindowViewController;

import static com.example.protectionapp.utils.AppConstant.ISNIGHTMODE;

public class SplashScreen extends AppCompatActivity {
LottieAnimationView animation;
TextView tvSplashTitle;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(PrefManager.getBoolean(ISNIGHTMODE))
            setTheme(R.style.AppTheme_Base_Night);
        else
            setTheme(R.style.AppTheme_Base_Light);
        setContentView(R.layout.splash_screen_layout);
        animation=findViewById(R.id.animation);
        tvSplashTitle=findViewById(R.id.tvSplashTitle);
        animation.playAnimation();
        Utils.setShader(Color.BLUE,Color.GREEN,tvSplashTitle);
        if (ActivityCompat.checkSelfPermission(this,Manifest.permission.SYSTEM_ALERT_WINDOW)!=PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.SYSTEM_ALERT_WINDOW}, AppConstant.SYSTEM_ALERT_CODE);
        }
        else
         startService(new Intent(this, HoverService.class));
      /*  // Create a HoverView to display in a Window:
        HoverView hoverView = HoverView.createForWindow(
                this,
                new WindowViewController(
                        (WindowManager) getSystemService(Context.WINDOW_SERVICE)
                )
        );
//        hoverView.setOnExitListener(onExitListener);
        hoverView.addToWindow();
        hoverView.setMenu(new MyHoverMenu(this));
        hoverView.collapse();*/
//        getSupportActionBar().hide();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                animation.clearAnimation();
                Intent intent = new Intent(SplashScreen.this,LogIn.class);
                startActivity(intent);
                finish();


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
                startService(new Intent(this, HoverService.class));
            }
        }
    }
}
