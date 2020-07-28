package com.example.protectionapp.activites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.protectionapp.R;
import com.example.protectionapp.utils.PrefManager;

import static com.example.protectionapp.utils.AppConstant.ISNIGHTMODE;

public class CameraDetector extends AppCompatActivity {
Toolbar toolbar;
ImageView ivBack;
TextView tvToolbarTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(PrefManager.getBoolean(ISNIGHTMODE))
            setTheme(R.style.AppTheme_Base_Night);
        else
            setTheme(R.style.AppTheme_Base_Light);
        setContentView(R.layout.activity_camera_detector);
        setSupportActionBar(toolbar);
        addViews();
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

    private void addViews() {
        toolbar=findViewById(R.id.toolbar);
        ivBack=findViewById(R.id.ivBack);
        tvToolbarTitle=findViewById(R.id.tvToolbarTitle);
        tvToolbarTitle.setText("Hidden Camera Detector");
    }
}