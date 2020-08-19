package com.example.protectionapp.activites;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.protectionapp.R;
import com.example.protectionapp.cameraDetector.CamDetector_tips;
import com.example.protectionapp.cameraDetector.CameraDectectorByRM;

public class CameraDetector extends AppCompatActivity {
    Toolbar toolbar;
    Button camDetect, tipsBt, btnInfraredCamera;
    ImageView ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_detector);
        setSupportActionBar(toolbar);
        findview();
        initActions();


    }

    private void findview() {
        camDetect = findViewById(R.id.btnCameraByRM);
        tipsBt = findViewById(R.id.tips_camBT);
        ivBack = findViewById(R.id.ivBack);
        btnInfraredCamera = findViewById(R.id.btnInfraredCamera);
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
                Intent intent= new Intent(CameraDetector.this, CamDetector_tips.class);
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
        btnInfraredCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CameraDetector.this, IRCameraDetector.class);
                startActivity(intent);
            }
        });
    }


}