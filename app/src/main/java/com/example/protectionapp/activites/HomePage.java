package com.example.protectionapp.activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.protectionapp.CacheCleaner;
import com.example.protectionapp.CallRecorder;
import com.example.protectionapp.CameraDetector;
import com.example.protectionapp.FileShare;
import com.example.protectionapp.R;
import com.example.protectionapp.fragments.AccountFragment;
import com.example.protectionapp.fragments.AppLockFragment;
import com.example.protectionapp.fragments.HomeFragment;
import com.example.protectionapp.fragments.PersonalRecordFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class HomePage extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;
    BottomNavigationView bottomNavigationView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        setUpToolBar();
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.about_us:
                        Toast.makeText(HomePage.this, "work is not complete yet, App is in progress", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.setting:
                        Toast.makeText(HomePage.this, "Work is not complete yet, App is in progress", Toast.LENGTH_SHORT).show();
                        break;
                }
                return false;
            }
        });
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new HomeFragment()).commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.Call_recorder:
                Intent intent = new Intent(HomePage.this, CallRecorder.class);
                startActivity(intent);
                Toast.makeText(this, "Call Recorder under progress", Toast.LENGTH_SHORT).show();
                break;
            case R.id.cache_cleaner:
                Intent intent2 = new Intent(HomePage.this, CacheCleaner.class);
                startActivity(intent2);
                Toast.makeText(this, "Cache Cleaner under progress", Toast.LENGTH_SHORT).show();
                break;
            case R.id.file_share:
                Intent intent3 = new Intent(HomePage.this, FileShare.class);
                startActivity(intent3);
                Toast.makeText(this, "File Sharer under progress", Toast.LENGTH_SHORT).show();
                break;
            case R.id.camera_detector:
                Intent intent4 = new Intent(HomePage.this, CameraDetector.class);
                startActivity(intent4);
                Toast.makeText(this, "Camera Detector under progress", Toast.LENGTH_SHORT).show();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void setUpToolBar()
    {
        drawerLayout = findViewById(R.id.drawerlayout);
        toolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.app_name,R.string.app_name);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment selectedFragment = null;
        switch (menuItem.getItemId()){
            case R.id.home:
                selectedFragment = new HomeFragment();
                break;
            case R.id.personalRecords:
                selectedFragment = new PersonalRecordFragment();
                break;
            case R.id.applock:
                selectedFragment = new AppLockFragment();
                break;
                case R.id.account:
                    selectedFragment = new AccountFragment();
                    break;
            default:
                return false;
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectedFragment).commit();
        return true;
    }
}