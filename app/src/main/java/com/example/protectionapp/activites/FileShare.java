package com.example.protectionapp.activites;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.example.protectionapp.R;
import com.example.protectionapp.ViewPageAdapter;
import com.example.protectionapp.fragments.ReceivedFragment;
import com.example.protectionapp.fragments.SendFragment;
import com.example.protectionapp.utils.PrefManager;
import com.google.android.material.tabs.TabLayout;

import static com.example.protectionapp.utils.AppConstant.ISNIGHTMODE;

public class FileShare extends AppCompatActivity {
    Toolbar toolbar;
    ImageView ivBack;
    TextView tvToolbarTitle;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(PrefManager.getBoolean(ISNIGHTMODE))
            setTheme(R.style.AppTheme_Base_Night);
        else
            setTheme(R.style.AppTheme_Base_Light);
        setContentView(R.layout.activity_file_share);
        addViews();
        initActions();
        setUpViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
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