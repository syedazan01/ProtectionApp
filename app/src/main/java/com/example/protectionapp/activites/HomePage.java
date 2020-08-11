package com.example.protectionapp.activites;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.protectionapp.R;
import com.example.protectionapp.fragments.AccountFragment;
import com.example.protectionapp.fragments.AppLockFragment;
import com.example.protectionapp.fragments.HomeFragment;
import com.example.protectionapp.fragments.PersonalRecordFragment;
import com.example.protectionapp.utils.PrefManager;
import com.example.protectionapp.utils.Utils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.luseen.spacenavigation.SpaceItem;
import com.luseen.spacenavigation.SpaceNavigationView;
import com.luseen.spacenavigation.SpaceOnClickListener;

import org.mazhuang.cleanexpert.ui.JunkCleanActivity;

import static com.example.protectionapp.utils.AppConstant.ISNIGHTMODE;

public class HomePage extends AppCompatActivity  {
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;
   // BottomNavigationView bottomNavigationView;
    long backLong;
    Toast backToast;
    //spacenavigation
    SpaceNavigationView spaceNavigationView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(PrefManager.getBoolean(ISNIGHTMODE))
            setTheme(R.style.AppTheme_Base_Night);
        else
            setTheme(R.style.AppTheme_Base_Light);
        setContentView(R.layout.activity_home_page);


        //set up id of spacenavigaton
        spaceNavigationView = findViewById(R.id.space);
        spaceNavigationView.initWithSaveInstanceState(savedInstanceState);
        spaceNavigationView.addSpaceItem(new SpaceItem("", R.drawable.ic_baseline_library_books_24));
        spaceNavigationView.addSpaceItem(new SpaceItem("", R.drawable.ic_baseline_screen_lock_portrait_24 ));
        spaceNavigationView.addSpaceItem(new SpaceItem("", R.drawable.ic_baseline_sms_failed_24 ));
        spaceNavigationView.addSpaceItem(new SpaceItem("", R.drawable.ic_baseline_account_circle_24));
        setUpToolBar();
      //  bottomNavigationView = findViewById(R.id.bottom_navigation);
      //  bottomNavigationView.setOnNavigationItemSelectedListener(this);
        navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                drawerLayout.closeDrawer(GravityCompat.START);
                switch (menuItem.getItemId()){
                    case R.id.kill_notification:
                       new Handler().postDelayed(new Runnable() {
                           @Override
                           public void run() {
                               startActivity(new Intent(HomePage.this,KillNotification.class));
                           }
                       },200);
                        return true;
                    case R.id.about_us:
                        Toast.makeText(HomePage.this, "work is not complete yet, App is in progress", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.setting:
                        Toast.makeText(HomePage.this, "Work is not complete yet, App is in progress", Toast.LENGTH_SHORT).show();
                        return true;
                }
                return false;
            }
        });
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new HomeFragment()).commit();
       // bottomNavigationView.getMenu().findItem(R.id.home).setChecked(true);


        //code of spaceNavigation bar
        spaceNavigationView.setSpaceOnClickListener(new SpaceOnClickListener() {

            @Override
            public void onCentreButtonClick() {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new HomeFragment()).commit();

            }

            @Override
            public void onItemClick(int itemIndex, String itemName) {
                Fragment selectedFragment = null;
                switch (itemIndex){
                    case 0:
                        selectedFragment = new  PersonalRecordFragment();
                        break;
                    case 1:
                        selectedFragment = new  AppLockFragment();
                        break;
                    case 2:
                        selectedFragment = new AppLockFragment();
                        break;
                    case 3:
                        selectedFragment = new AccountFragment();
                        break;
                    default:
                        //return false;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectedFragment).commit();
                //return true;
            }

            @Override
            public void onItemReselected(int itemIndex, String itemName) {

            }
        });
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
            case R.id.theme_mode:
                if(PrefManager.getBoolean(ISNIGHTMODE))
                {
                    setTheme(R.style.AppTheme_Base_Light);
                    PrefManager.putBoolean(ISNIGHTMODE,false);
                }
                else
                {
                    PrefManager.putBoolean(ISNIGHTMODE,true);
                    setTheme(R.style.AppTheme_Base_Night);
                }
                recreate();

                break;
            case R.id.Call_recorder:
                Intent intent = new Intent(HomePage.this, CallRecorder.class);
                startActivity(intent);
//                Toast.makeText(this, "Call Recorder under progress", Toast.LENGTH_SHORT).show();
                break;
            case R.id.cache_cleaner:
                Intent intent2 = new Intent(HomePage.this, JunkCleanActivity.class);
                startActivity(intent2);
               /* Intent intent2 = new Intent(HomePage.this, CacheCleaner.class);
                startActivity(intent2);
                Toast.makeText(this, "Cache Cleaner under progress", Toast.LENGTH_SHORT).show();*/
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

    /*@Override
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
    }*/


    @Override
    public void onBackPressed() {
        if(backLong+2000>=System.currentTimeMillis())
        {
            backToast.cancel();
            finishAffinity();
        }
        else
        {
            backToast=Toast.makeText(this, "press back again to exit", Toast.LENGTH_SHORT);
            backToast.show();
        }
        backLong=System.currentTimeMillis();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for(Fragment fragment : getSupportFragmentManager().getFragments()) {
            if(fragment instanceof AccountFragment)
            {
                fragment.onActivityResult(requestCode,resultCode,data);
                break;
            }
        }
    }
}