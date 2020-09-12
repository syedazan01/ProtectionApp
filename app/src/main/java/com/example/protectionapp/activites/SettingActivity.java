package com.example.protectionapp.activites;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
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
                 Uri uri= Uri.parse("market://details?id"+BuildConfig.APPLICATION_ID);
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                // To count with Play market backstack, After pressing back button,
                // to taken back to our application, we need to add following flags to intent.
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id="+BuildConfig.APPLICATION_ID)));
                }
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