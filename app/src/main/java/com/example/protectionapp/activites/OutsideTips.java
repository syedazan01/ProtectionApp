package com.example.protectionapp.activites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.protectionapp.R;

public class OutsideTips extends AppCompatActivity {
    private TextView textView;
    ImageView ivBack;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outside_tips);
        setSupportActionBar(toolbar);
        textView = findViewById(R.id.outside_tipsTV);
        textView.setText("In outside world, you are not supposed to hide your identity in official places so just be cautious and behave accordingly.\n" +
                "More info\n" +
                "\n" +
                "Some easy steps can save you from uneasy situations");

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}