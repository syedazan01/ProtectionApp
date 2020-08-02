package com.example.protectionapp.activites;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.protectionapp.R;
import com.example.protectionapp.utils.AppConstant;
import com.example.protectionapp.utils.Utils;
import com.goodiebag.pinview.Pinview;

public class Otp extends AppCompatActivity {
    Button otpsubmit;
    TextView tvSentMsg,tvToolbarTitle;
    ImageView ivBack;
    Pinview otp_View;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        initViews();
       initActions();

    }

    private void initActions() {
       otpsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Otp.this,HomePage.class);
                startActivity(intent);
            }
        });
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void initViews() {
        otpsubmit = findViewById(R.id.otpsubmitbtn);
        tvSentMsg = findViewById(R.id.tvSentMsg);
        ivBack = findViewById(R.id.ivBack);
        tvToolbarTitle = findViewById(R.id.tvToolbarTitle);
        otp_View = findViewById(R.id.otp_View);
        tvToolbarTitle.setText("Otp");
        tvSentMsg.setText(getResources().getString(R.string.sent_msg)+getIntent().getStringExtra(AppConstant.LOGIN_MOBILE));
        Utils.makeButton(otpsubmit,getResources().getColor(R.color.colorPrimary),50F);
    }
}
