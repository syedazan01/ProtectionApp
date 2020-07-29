package com.example.protectionapp.activites;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.protectionapp.R;

public class Otp extends AppCompatActivity {
    Button otpsubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        otpsubmit = findViewById(R.id.otpsubmitbtn);
        otpsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Otp.this,HomePage.class);
                startActivity(intent);
            }
        });
    }
}
