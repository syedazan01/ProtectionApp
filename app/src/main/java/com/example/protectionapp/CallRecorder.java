package com.example.protectionapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

import com.example.protectionapp.fragments.PlayerFragment;
import com.example.protectionapp.fragments.Recording_fragment;
import com.example.protectionapp.fragments.SettingFragment;
import com.google.android.material.tabs.TabLayout;

public class CallRecorder extends AppCompatActivity {
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_recorder);
        toolbar = findViewById(R.id.toolbar_recorder);
        tabLayout = findViewById(R.id.tablayout_recorder);
        viewPager = findViewById(R.id.viewpager_recorder);

        setSupportActionBar(toolbar);
        setUpViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);


    }
    private void setUpViewPager(ViewPager viewPager){
        ViewPageAdapter viewPageAdapter = new ViewPageAdapter(getSupportFragmentManager(),3);
        viewPageAdapter.addFragment(new Recording_fragment(),"Recording");
        viewPageAdapter.addFragment(new PlayerFragment(),"Player");
        viewPageAdapter.addFragment(new SettingFragment(),"Setting");
        viewPager.setAdapter(viewPageAdapter);
    }
}