package com.example.protectionapp.RecordsActivites;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.protectionapp.R;
import com.google.android.material.textfield.TextInputLayout;

public class DrivingLicence extends AppCompatActivity {
    private TextInputLayout textInputEmail;
    private TextInputLayout textInputUsername;
    private TextInputLayout textInputPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driving_licence);
        textInputEmail = findViewById(R.id.text_input_email);
        textInputUsername = findViewById(R.id.text_input_username);
        textInputPassword = findViewById(R.id.text_input_password);
    }
}