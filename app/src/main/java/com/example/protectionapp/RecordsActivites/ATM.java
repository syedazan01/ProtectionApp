package com.example.protectionapp.RecordsActivites;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.protectionapp.R;
import com.example.protectionapp.utils.Utils;

public class ATM extends AppCompatActivity {
    private Button btnAtmScan,btnAtmSave;
    private ImageView ivBack;
    private TextView tvToolbarTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a_t_m);
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
        btnAtmScan=findViewById(R.id.btnAtmScan);
        btnAtmSave=findViewById(R.id.btnAtmSave);
        ivBack=findViewById(R.id.ivBack);
        tvToolbarTitle=findViewById(R.id.tvToolbarTitle);
        tvToolbarTitle.setText("ATM Detail Form");
        Utils.makeButton(btnAtmScan,getResources().getColor(R.color.colorAccent),40F);
        Utils.makeButton(btnAtmSave,getResources().getColor(R.color.colorPrimary),40F);
    }
}