package com.example.protectionapp.RecordsActivites;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.protectionapp.R;
import com.example.protectionapp.utils.Utils;
import com.google.android.material.textfield.TextInputLayout;

public class DrivingLicence extends AppCompatActivity {
    private TextInputLayout textInputEmail;
    private TextInputLayout textInputUsername;
    private TextInputLayout textInputPassword;
    private Button btnLicenseSave;
    private ImageView ivBack;
    private TextView tvToolbarTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driving_licence);
        initViews();
        initActions();
    }

    private void initActions() {
    ivBack.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            onBackPressed();
        }
    });
    }

    private void initViews() {

//        textInputEmail = findViewById(R.id.text_input_email);
//        textInputUsername = findViewById(R.id.text_input_username);
//        textInputPassword = findViewById(R.id.text_input_password);
//        btnLicenseSave = findViewById(R.id.btnLicenseSave);
        ivBack = findViewById(R.id.ivBack);
        tvToolbarTitle = findViewById(R.id.tvToolbarTitle);
        tvToolbarTitle.setText("Driving License Form");
        Utils.makeButton(btnLicenseSave,getResources().getColor(R.color.colorPrimary),40F);
    }
}