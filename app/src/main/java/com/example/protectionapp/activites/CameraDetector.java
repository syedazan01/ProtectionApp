package com.example.protectionapp.activites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.protectionapp.R;
import com.example.protectionapp.utils.PrefManager;

import static com.example.protectionapp.utils.AppConstant.ISNIGHTMODE;

public class CameraDetector extends AppCompatActivity  {
    Toolbar toolbar;
    Button camDetect,tipsBt;
    ImageView ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(toolbar);
        findview();
        initActions();


    }

    private void findview() {
        camDetect = findViewById(R.id.btnCameraByRM);
        tipsBt =findViewById(R.id.tips_camBT);
    }


    private void initActions() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        tipsBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(CameraDetector.this,CamDetector_tips.class);
                startActivity(intent);

            }
        });
        camDetect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CameraDetector.this, CameraDectectorByRM.class);
                startActivity(intent);

            }
        });
    }


};