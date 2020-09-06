package com.example.protectionapp.activites;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.protectionapp.R;
import com.example.protectionapp.RecordsActivites.PersonalRecords;
import com.example.protectionapp.fragments.AccountFragment;
import com.example.protectionapp.fragments.HomeFragment;
import com.example.protectionapp.services.FloatingWindowService;
import com.example.protectionapp.utils.PrefManager;
import com.example.protectionapp.utils.Utils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.luseen.spacenavigation.SpaceNavigationView;

import theredspy15.ltecleanerfoss.MainActivity;

import static com.example.protectionapp.utils.AppConstant.ISBLUELIGHT;

public class HomePage extends AppCompatActivity implements View.OnClickListener {
    //    DrawerLayout drawerLayout;
    Toolbar toolbar;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;
    // BottomNavigationView bottomNavigationView;
    long backLong;
    Toast backToast;
    //spacenavigation
    SpaceNavigationView spaceNavigationView;
    private LinearLayout llHome, llCategory, llNotification, llProfile;
    private ImageView ivHome, ivCategory, ivNotification, ivProfile;
    private View lineHome, lineCategory, lineNotification, lineProfile;

    private FloatingActionButton fabCleaner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.changeColor(this, "#00000000", true);
        setContentView(R.layout.activity_home_page);
        initViews();
        initActions();
       /* //set up id of spacenavigaton
        spaceNavigationView = findViewById(R.id.space);
        if (PrefManager.getString(AppConstant.INVITED_BY).equals("") && !PrefManager.getBoolean(AppConstant.ISREFERED)) {
            Utils.getUserReference().child(PrefManager.getString(AppConstant.USER_MOBILE)).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    UserBean userBean = dataSnapshot.getValue(UserBean.class);
                    userBean.setRefer(true);
                    Utils.storeUserDetailsToRTD(userBean);
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
            Utils.showCongratsDialog(this);
        }
        spaceNavigationView.initWithSaveInstanceState(savedInstanceState);
        spaceNavigationView.addSpaceItem(new SpaceItem("", R.drawable.ic_baseline_home_24));
        spaceNavigationView.addSpaceItem(new SpaceItem("", R.drawable.ic_sos));
        spaceNavigationView.addSpaceItem(new SpaceItem("", R.drawable.ic_feature));
        spaceNavigationView.addSpaceItem(new SpaceItem("", R.drawable.ic_baseline_account_circle_24));
        setUpToolBar();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
        //code of spaceNavigation bar
        spaceNavigationView.setSpaceOnClickListener(new SpaceOnClickListener() {

            @Override
            public void onCentreButtonClick() {
                startActivity(new Intent(HomePage.this, MainActivity.class));
            }

            @Override
            public void onItemClick(int itemIndex, String itemName) {
                Fragment selectedFragment = null;
                switch (itemIndex) {
                    case 0:
                        selectedFragment = new HomeFragment();
                        break;
                    case 1:
                        selectedFragment = new SosFragment();
                        break;
                    case 2:
                        selectedFragment = new UtilityFeaturesFragment();
                        break;
                    case 3:
                        selectedFragment = new AccountFragment();
                        break;
                    default:
                        //return false;
                }
                if (selectedFragment != null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                }
                //return true;
            }

            @Override
            public void onItemReselected(int itemIndex, String itemName) {

            }
        });*/

        setBottomViews(0);
    }

    private void initActions() {
        llHome.setOnClickListener(this);
        llCategory.setOnClickListener(this);
        llNotification.setOnClickListener(this);
        llProfile.setOnClickListener(this);

        fabCleaner.setOnClickListener(this);
    }

    private void initViews() {

        fabCleaner = findViewById(R.id.fabCleaner);

        llHome = findViewById(R.id.llHome);
        llCategory = findViewById(R.id.llCategory);
        llNotification = findViewById(R.id.llNotification);
        llProfile = findViewById(R.id.llProfile);

        ivHome = findViewById(R.id.ivHome);
        ivCategory = findViewById(R.id.ivCategory);
        ivNotification = findViewById(R.id.ivNotification);
        ivProfile = findViewById(R.id.ivProfile);

        lineHome = findViewById(R.id.lineHome);
        lineCategory = findViewById(R.id.lineCategory);
        lineNotification = findViewById(R.id.lineNotification);
        lineProfile = findViewById(R.id.lineProfile);
    }

    private void setBottomViews(int index) {
        Fragment fragment = null;
        switch (index) {
            case 0:
                ivHome.setColorFilter(Utils.getActiveTintColor(this), PorterDuff.Mode.MULTIPLY);
                ivCategory.setColorFilter(Utils.getInActiveTintColor(this), PorterDuff.Mode.MULTIPLY);
                ivNotification.setColorFilter(Utils.getInActiveTintColor(this), PorterDuff.Mode.MULTIPLY);
                ivProfile.setColorFilter(Utils.getInActiveTintColor(this), PorterDuff.Mode.MULTIPLY);

                lineHome.setVisibility(View.VISIBLE);
                lineCategory.setVisibility(View.GONE);
                lineNotification.setVisibility(View.GONE);
                lineProfile.setVisibility(View.GONE);
                fragment = new HomeFragment();

                break;
            case 1:
                ivCategory.setColorFilter(Utils.getActiveTintColor(this), PorterDuff.Mode.MULTIPLY);
                ivHome.setColorFilter(Utils.getInActiveTintColor(this), PorterDuff.Mode.MULTIPLY);
                ivNotification.setColorFilter(Utils.getInActiveTintColor(this), PorterDuff.Mode.MULTIPLY);
                ivProfile.setColorFilter(Utils.getInActiveTintColor(this), PorterDuff.Mode.MULTIPLY);

                lineCategory.setVisibility(View.VISIBLE);
                lineHome.setVisibility(View.GONE);
                lineNotification.setVisibility(View.GONE);
                lineProfile.setVisibility(View.GONE);

                fragment = new HomeFragment();

                break;
            case 2:
                ivNotification.setColorFilter(Utils.getActiveTintColor(this), PorterDuff.Mode.MULTIPLY);
                ivCategory.setColorFilter(Utils.getInActiveTintColor(this), PorterDuff.Mode.MULTIPLY);
                ivHome.setColorFilter(Utils.getInActiveTintColor(this), PorterDuff.Mode.MULTIPLY);
                ivProfile.setColorFilter(Utils.getInActiveTintColor(this), PorterDuff.Mode.MULTIPLY);

                lineNotification.setVisibility(View.VISIBLE);
                lineCategory.setVisibility(View.GONE);
                lineHome.setVisibility(View.GONE);
                lineProfile.setVisibility(View.GONE);

                fragment = new HomeFragment();
                break;

            case 3:
                ivProfile.setColorFilter(Utils.getActiveTintColor(this), PorterDuff.Mode.MULTIPLY);
                ivCategory.setColorFilter(Utils.getInActiveTintColor(this), PorterDuff.Mode.MULTIPLY);
                ivNotification.setColorFilter(Utils.getInActiveTintColor(this), PorterDuff.Mode.MULTIPLY);
                ivHome.setColorFilter(Utils.getInActiveTintColor(this), PorterDuff.Mode.MULTIPLY);

                lineProfile.setVisibility(View.VISIBLE);
                lineCategory.setVisibility(View.GONE);
                lineNotification.setVisibility(View.GONE);
                lineHome.setVisibility(View.GONE);

                fragment = new HomeFragment();
                break;
            default:
                throw new IllegalStateException("UnknownState");
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.theme_mode:
                if (PrefManager.getBoolean(ISBLUELIGHT)) {
//                    setTheme(R.style.AppTheme_Base_Light);
                    PrefManager.putBoolean(ISBLUELIGHT, false);
                } else {
                    PrefManager.putBoolean(ISBLUELIGHT, true);
//                    setTheme(R.style.AppTheme_Base_Night);
                }
                startService(new Intent(HomePage.this, FloatingWindowService.class).setAction(FloatingWindowService.BLUE_LIGHT_FILTER));

//                recreate();

                break;
            case R.id.Call_recorder:
                Intent intent = new Intent(HomePage.this, CallRecorder.class);
                startActivity(intent);
//                Toast.makeText(this, "Call Recorder under progress", Toast.LENGTH_SHORT).show();
                break;
            case R.id.personalRecords:
//                Intent intent2 = new Intent(HomePage.this, JunkCleanActivity.class);
                Intent intent2 = new Intent(HomePage.this, PersonalRecords.class);
                startActivity(intent2);
               /* Intent intent2 = new Intent(HomePage.this, CacheCleaner.class);
                startActivity(intent2);
                Toast.makeText(this, "Cache Cleaner under progress", Toast.LENGTH_SHORT).show();*/
                break;
            case R.id.file_share:
                Intent intent3 = new Intent(HomePage.this, FileShare.class);
                startActivity(intent3);
//                Toast.makeText(this, "File Sharer under progress", Toast.LENGTH_SHORT).show();
                break;
            case R.id.camera_detector:
                Intent intent4 = new Intent(HomePage.this, CameraDetector.class);
                startActivity(intent4);
//                Toast.makeText(this, "Camera Detector under progress", Toast.LENGTH_SHORT).show();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void setUpToolBar() {
//        drawerLayout = findViewById(R.id.drawerlayout);
        toolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
//        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.app_name,R.string.app_name);
//        drawerLayout.addDrawerListener(actionBarDrawerToggle);
//        actionBarDrawerToggle.syncState();

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
        if (backLong + 2000 >= System.currentTimeMillis()) {
            backToast.cancel();
            finishAffinity();
        } else {
            backToast = Toast.makeText(this, "press back again to exit", Toast.LENGTH_SHORT);
            backToast.show();
        }
        backLong = System.currentTimeMillis();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            if (fragment instanceof AccountFragment) {
                fragment.onActivityResult(requestCode, resultCode, data);
                break;
            }
        }
    }

    @Override
    public void onClick(View view) {
        if (view == llHome) {
            setBottomViews(0);
        } else if (view == llCategory) {
            setBottomViews(1);
        } else if (view == llNotification) {
            setBottomViews(2);
        } else if (view == llProfile) {
            setBottomViews(3);
        } else if (view == fabCleaner) {
            startActivity(new Intent(HomePage.this, MainActivity.class));
        }
    }
}