package com.example.protectionapp.activites;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.example.protectionapp.R;
import com.example.protectionapp.utils.PrefManager;
import com.example.protectionapp.utils.Utils;

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
}
