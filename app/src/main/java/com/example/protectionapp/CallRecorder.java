package com.example.protectionapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class CallRecorder extends AppCompatActivity {
    Toolbar toolbar;
    ImageView ivBack;
    TextView tvToolbarTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_recorder);
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
        toolbar = findViewById(R.id.toolbar);
        ivBack = findViewById(R.id.ivBack);
        tvToolbarTitle = findViewById(R.id.tvToolbarTitle);
        tvToolbarTitle.setText("Hidden Camera Finder");
    }
}