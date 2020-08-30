package com.example.protectionapp.activites;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.example.protectionapp.R;
import com.example.protectionapp.RecordsActivites.SendDailog;
import com.example.protectionapp.ViewPageAdapter;
import com.example.protectionapp.fragments.ReceivedFragment;
import com.example.protectionapp.fragments.SendFragment;
import com.example.protectionapp.utils.PrefManager;
import com.google.android.material.tabs.TabLayout;

import static com.example.protectionapp.utils.AppConstant.ISBLUELIGHT;

public class FileShare extends AppCompatActivity implements SendDailog.SendDialogListener {
    Toolbar toolbar;
    ImageView ivBack;
    TextView tvToolbarTitle;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    public void applyTexts(String message, String password) {
        if (viewPager.getCurrentItem() == 0) {
            SendFragment.sendDialogListener.applyTexts(message, password);
        } else
            ReceivedFragment.sendDialogListener.applyTexts(message, password);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       /* if (PrefManager.getBoolean(ISBLUELIGHT))
            setTheme(R.style.AppTheme_Base_Night);
        else*/
            setTheme(R.style.AppTheme_Base_Light);
        setContentView(R.layout.activity_file_share);
        addViews();
        initActions();
        setUpViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (viewPager.getCurrentItem() == 0) {
                    if (SendFragment.sendClickListener != null) {
                        SendFragment.sendClickListener.onSent();
                    }
                } else {
                    if (ReceivedFragment.recievedClickListener != null) {
                        ReceivedFragment.recievedClickListener.onRecieved();
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public interface SendClickListener {
        void onSent();
    }

    public interface RecievedClickListener {
        void onRecieved();
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
        tabLayout = findViewById(R.id.fileshareTL);
        viewPager=findViewById(R.id.viewpager_fileshare);

        toolbar = findViewById(R.id.toolbar);
        ivBack = findViewById(R.id.ivBack);
        tvToolbarTitle = findViewById(R.id.tvToolbarTitle);
        tvToolbarTitle.setText("File Sharer");
    }
    private void setUpViewPager(ViewPager viewPager){
        ViewPageAdapter viewPageAdapter = new ViewPageAdapter(getSupportFragmentManager(),2);
        viewPageAdapter.addFragment(new SendFragment(),"SEND");
        viewPageAdapter.addFragment(new ReceivedFragment(),"RECEIVED");
//        viewPageAdapter.addFragment(new SettingFragment(),"Setting");
        viewPager.setAdapter(viewPageAdapter);
    }
}