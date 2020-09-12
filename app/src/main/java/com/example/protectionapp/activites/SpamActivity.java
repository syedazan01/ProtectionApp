package com.example.protectionapp.activites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.protectionapp.R;
import com.example.protectionapp.adapters.SpamCallAdapter;
import com.example.protectionapp.interfacecallbacks.SpamCallsListener;
import com.example.protectionapp.model.SpamBean;
import com.example.protectionapp.room.AppDatabase;
import com.example.protectionapp.room.dao.SpamCallDao;
import com.example.protectionapp.utils.Utils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class SpamActivity extends AppCompatActivity implements SpamCallsListener, SpamCallAdapter.SpamCallRecyclerViewListener {
    private ImageView ivBack;
    private RecyclerView rvSpamCall;
    LinearLayout llNoSpam;
    SpamCallAdapter spamCallAdapter;
    private ImageView ivAdd;
    SpamCallDao spamCallDao;
    List<SpamBean> spamBeanList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spam);
        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this,Manifest.permission.READ_PHONE_STATE)!=PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.CALL_PHONE, Manifest.permission.READ_PHONE_STATE }, 0);
        }
        initViews();
        initActions();
    }

    private void initViews() {
        rvSpamCall = findViewById(R.id.rvSpamCall);
        llNoSpam = findViewById(R.id.llNoSpam);
        ivBack = findViewById(R.id.ivBack);
        ivAdd = findViewById(R.id.ivAdd);
        TextView tvToolbarTitle = findViewById(R.id.tvToolbarTitle);
        tvToolbarTitle.setText("Spam Calls");
        spamCallAdapter = new SpamCallAdapter(this);
        spamCallDao = AppDatabase.getAppDataBase(this).getSpamCallDao();
    rvSpamCall.setLayoutManager(new LinearLayoutManager(this));
    rvSpamCall.setAdapter(spamCallAdapter);
    }

    private void initActions() {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                spamBeanList=spamCallDao.getSpamCalls();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (spamBeanList.size()>0) {
                            spamCallAdapter.updateList(spamBeanList);
                        rvSpamCall.setVisibility(View.VISIBLE);
                        llNoSpam.setVisibility(View.GONE);
                        }

                    }
                });

            }
        });
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.showSpamCallDialog(SpamActivity.this);
            }
        });
    }

    @Override
    public void onSaved(SpamBean spamBean) {
        spamBeanList.add(spamBean);
        rvSpamCall.setVisibility(View.VISIBLE);
        llNoSpam.setVisibility(View.GONE);
        spamCallAdapter.updateList(spamBeanList);
    }

    @Override
    public void onDeleted(int position) {
        spamBeanList.remove(position);
        if (spamBeanList.size()>0) {
            spamCallAdapter.notifyItemRemoved(position);
        }
        else
        {
            rvSpamCall.setVisibility(View.GONE);
            llNoSpam.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDelete(int id, int position) {
        Utils.showSpamCallDeleteDialog(this, id, position);
    }

    @Override
    public void onChecked(boolean b, int id) {
        spamCallDao.updateCheck(id, b);
    }
}