package com.example.protectionapp.activites;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.protectionapp.R;
import com.example.protectionapp.utils.AppConstant;
import com.example.protectionapp.utils.Utils;

public class Otp extends AppCompatActivity {
    Button otpsubmit;
    TextView tvSentMsg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        otpsubmit = findViewById(R.id.otpsubmitbtn);
        tvSentMsg = findViewById(R.id.tvSentMsg);
        tvSentMsg.setText(getResources().getString(R.string.sent_msg)+getIntent().getStringExtra(AppConstant.LOGIN_MOBILE));
        otpsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Otp.this,HomePage.class);
                startActivity(intent);
            }
        });
        Utils.makeButton(otpsubmit,getResources().getColor(R.color.colorPrimary));
    }
}
