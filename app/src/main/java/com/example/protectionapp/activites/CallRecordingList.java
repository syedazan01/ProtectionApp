package com.example.protectionapp.activites;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.protectionapp.R;

public class CallRecordingList extends AppCompatActivity {
ImageView ivBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_recording_list);
        initViews();
        initActions();
    }
    private void initViews()
    {
        ivBack=findViewById(R.id.ivBack);
        TextView tvToolbarTitle=findViewById(R.id.tvToolbarTitle);
        tvToolbarTitle.setText("Call Recording");
    }
    private void initActions()
    {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}