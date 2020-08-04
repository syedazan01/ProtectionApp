package com.example.protectionapp.activites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.protectionapp.R;
import com.example.protectionapp.adapters.InstalledApps;
import com.example.protectionapp.model.PInfo;

import java.util.ArrayList;

public class KillNotification extends AppCompatActivity {
    ImageView ivBack;
    TextView tvToolbarTitle;
    RecyclerView rvInstalledApps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kill_notification);
        iniiViews();
        initActions();
    }

    private void initActions() {
    }

    private void iniiViews() {
        ivBack = findViewById(R.id.ivBack);
        tvToolbarTitle = findViewById(R.id.tvToolbarTitle);
        rvInstalledApps = findViewById(R.id.rvInstalledApps);
        tvToolbarTitle.setText("Kill Notifications");
      rvInstalledApps.setLayoutManager(new LinearLayoutManager(this));
        ProgressDialog dialog=new ProgressDialog(this);
        dialog.setTitle("Loading...");
        dialog.show();
        ArrayList<PInfo> pInfos=new ArrayList<>();
        pInfos=new PInfo(this).getInstalledApps(false);

      rvInstalledApps.setAdapter(new InstalledApps(this,pInfos));
      dialog.dismiss();
    }
}