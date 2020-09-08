package com.example.protectionapp.activites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.protectionapp.R;

public class SettingActivity extends AppCompatActivity {
    CardView aboutus,termsCon,privacyP,rateus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initViews();
        initAction();
    }

    private void initAction() {
        aboutus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SettingActivity.this,About_us.class);
                startActivity(i);
            }
        });

        termsCon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SettingActivity.this,Terms_Condition.class);
                startActivity(i);
            }
        });

        privacyP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SettingActivity.this,Privacy_poly.class);
                startActivity(i);
            }
        });

        rateus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SettingActivity.this,Rate_us.class);
                startActivity(i);
            }
        });
    }

    private void initViews() {
        aboutus = findViewById(R.id.card_aboutUs);
        termsCon = findViewById(R.id.cardTermsC);
        privacyP = findViewById(R.id.cardPrivacyPolicy);
        rateus = findViewById(R.id.cardRateUs);
    }
}