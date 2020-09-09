package com.example.protectionapp.activites;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.protectionapp.BuildConfig;
import com.example.protectionapp.R;

public class SettingActivity extends AppCompatActivity {
    RelativeLayout rltAboutus, rltTermsCon, rltPrivacyP, rltRateus;
    TextView tvAppVer;
    ImageView ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initViews();
        initAction();
    }

    private void initAction() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        rltAboutus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SettingActivity.this, About_us.class);
                startActivity(i);
            }
        });

        rltTermsCon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SettingActivity.this, Terms_Condition.class);
                startActivity(i);
            }
        });

        rltPrivacyP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SettingActivity.this, Privacy_poly.class);
                startActivity(i);
            }
        });

        rltRateus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SettingActivity.this, Rate_us.class);
                startActivity(i);
            }
        });
    }

    private void initViews() {
        rltAboutus = findViewById(R.id.rltAboutUs);
        rltTermsCon = findViewById(R.id.rltTandC);
        rltPrivacyP = findViewById(R.id.rltPrivacy);
        rltRateus = findViewById(R.id.rltRateUs);
        tvAppVer = findViewById(R.id.tvAppVer);
        ivBack = findViewById(R.id.ivBack);

        tvAppVer.setText("App Version \n" + BuildConfig.VERSION_NAME);
    }
}