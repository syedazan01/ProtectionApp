package com.example.protectionapp.activites;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.protectionapp.R;
import com.example.protectionapp.adapters.InstalledApps;
import com.example.protectionapp.interfacecallbacks.OnNotificationChecked;
import com.example.protectionapp.model.PInfo;
import com.example.protectionapp.services.ForgroundService;
import com.example.protectionapp.utils.AppConstant;
import com.example.protectionapp.utils.PrefManager;
import com.example.protectionapp.utils.Utils;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;

import static com.example.protectionapp.utils.AppConstant.ISNIGHTMODE;

public class KillNotification extends AppCompatActivity implements OnNotificationChecked {
    ImageView ivBack;
    TextView tvToolbarTitle;
    ConstraintLayout constMostUsed, constRareUsed;
    RecyclerView rvMostInstalledApps, rvRareInstalledApps;
    androidx.appcompat.widget.SearchView searchApp;
    Switch swAll;
    SharedPreferences pref;
    ArrayList<PInfo> pInfos = new ArrayList<>();
    ArrayList<PInfo> mostPInfos = new ArrayList<>();
    InstalledApps installedAppAdapter, mostInstalledAppAdapter;
    Activity activity = KillNotification.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (PrefManager.getBoolean(ISNIGHTMODE))
            setTheme(R.style.AppTheme_Base_Night);
        else
            setTheme(R.style.AppTheme_Base_Light);
        setContentView(R.layout.activity_kill_notification);
        iniiViews();
        initActions();
    }

    private void initActions() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        swAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Intent intent = new Intent(KillNotification.this, ForgroundService.class);
                if (b) {
                    pref.edit().putBoolean(AppConstant.NOTIFICATION_ENABLE, true).apply();
                    intent.setAction(ForgroundService.ACTION_START_FOREGROUND_SERVICE);
                    startService(intent);
                } else {
                    pref.edit().putBoolean(AppConstant.NOTIFICATION_ENABLE, false).apply();
                    intent.setAction(ForgroundService.ACTION_STOP_FOREGROUND_SERVICE);
                    stopService(intent);
                }
            }
        });
        searchApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchApp.setIconifiedByDefault(true);
                searchApp.setFocusable(true);
                searchApp.setIconified(false);
                searchApp.requestFocusFromTouch();
            }
        });
        searchApp.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                Executors.newSingleThreadExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (TextUtils.isEmpty(s)) {
                                    installedAppAdapter.notifyList(pInfos);
                                    mostInstalledAppAdapter.notifyList(mostPInfos);
                                }
                            }
                        });

                        String query = s.toLowerCase(Locale.getDefault());
                        ArrayList<PInfo> filteredPInfo = new ArrayList<>();
                        ArrayList<PInfo> mostFilteredPInfo = new ArrayList<>();

                        for (PInfo pInfo : mostPInfos) {
                            String label = pInfo.getAppname().toLowerCase(Locale.getDefault());
                            if (label.contains(query)) {
                                mostFilteredPInfo.add(pInfo);
                            }
                        }
                        for (PInfo pInfo : pInfos) {
                            String label = pInfo.getAppname().toLowerCase(Locale.getDefault());
                            if (label.contains(query)) {
                                filteredPInfo.add(pInfo);
                            }
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                installedAppAdapter.notifyList(filteredPInfo);
                                mostInstalledAppAdapter.notifyList(mostFilteredPInfo);
                            }
                        });

                    }
                });

                return false;
            }
        });
    }

    private void iniiViews() {
        pref = Utils.getDefaultManager(this);
        searchApp = findViewById(R.id.searchApp);
        swAll = findViewById(R.id.swAll);
        ivBack = findViewById(R.id.ivBack);
        tvToolbarTitle = findViewById(R.id.tvToolbarTitle);
        constRareUsed = findViewById(R.id.constRareUsed);
        constMostUsed = findViewById(R.id.constMostUsed);
        rvMostInstalledApps = findViewById(R.id.rvMostInstalledApps);
        rvRareInstalledApps = findViewById(R.id.rvRareInstalledApps);
        tvToolbarTitle.setText("Kill Notifications");
        rvMostInstalledApps.setLayoutManager(new LinearLayoutManager(this));
        rvRareInstalledApps.setLayoutManager(new LinearLayoutManager(this));

        final ProgressDialog dialog = Utils.getProgressDialog(activity);

        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog.setTitle("Loading...");
                        dialog.show();
                    }
                });
                pInfos = new PInfo(activity).getInstalledApps(false);
                List<PInfo> tempPInfos = new ArrayList<>(pInfos);
                for (int i = 0; i < tempPInfos.size(); i++) {
                    PInfo pInfo = tempPInfos.get(i);
                    String appName = pInfo.getAppname().toLowerCase(Locale.getDefault());
                    if (appName.equals("whatsapp") || appName.equals("instagram") || appName.equals("facebook") || appName.equals("telegram") || appName.equals("youtube")) {
                        mostPInfos.add(pInfo);
                        pInfos.remove(pInfo);
                    }
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();

                        if (mostPInfos.size() < 0) {
                            constMostUsed.setVisibility(View.GONE);
                        }
                        installedAppAdapter = new InstalledApps(activity, pInfos, KillNotification.this, AppConstant.RAREUSED);
                        mostInstalledAppAdapter = new InstalledApps(activity, mostPInfos, KillNotification.this, AppConstant.MOSTUSED);
                        rvMostInstalledApps.setAdapter(mostInstalledAppAdapter);
                        rvRareInstalledApps.setAdapter(installedAppAdapter);
                    }
                });

            }
        });


        swAll.setChecked(pref.getBoolean(AppConstant.NOTIFICATION_ENABLE, false));
    }

    @Override
    public void onCheckboxAppChecked(int position, boolean isChecked, String typeOfList) {
        String pkg = null;
        if (typeOfList.equals(AppConstant.RAREUSED)) {
            pkg = installedAppAdapter.getItem(position).getPname();
        } else {
            pkg = mostInstalledAppAdapter.getItem(position).getPname();
        }
        if (pref.contains(AppConstant.PREF_PACKAGES_BLOCKED)) {
            HashSet pkgs = new HashSet(Arrays.asList(pref.getString(AppConstant.PREF_PACKAGES_BLOCKED, "").split(";")));
            if (isChecked) {
                pkgs.add(pkg);
            } else {
                pkgs.remove(pkg);
            }
            pref.edit().putString(AppConstant.PREF_PACKAGES_BLOCKED, TextUtils.join(";", pkgs)).apply();
        } else {
            pref.edit().putString(AppConstant.PREF_PACKAGES_BLOCKED, pkg).apply();
        }
    }

    private Boolean hasAccessGranted() {
        ContentResolver contentResolver = this.getContentResolver();
        String enabledNotificationListeners = Settings.Secure.getString(contentResolver, AppConstant.SETTING_NOTIFICATION_LISTENER);
        String packageName = this.getPackageName();
        // check to see if the enabledNotificationListeners String contains our package name
        return !(enabledNotificationListeners == null || !enabledNotificationListeners.contains(packageName));
    }

    @Override
    public void onBackPressed() {
        if (!searchApp.isIconified()) {
            searchApp.setIconified(true);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!hasAccessGranted()) {
            pref.edit().remove(AppConstant.NOTIFICATION_ENABLE).apply();
            Snackbar.make(findViewById(android.R.id.content), R.string.snackbar_not_allowed_title, Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.snackbar_not_allowed_action, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                                startActivity(new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS));
                            } else {
                                startActivity(new Intent(AppConstant.ACTION_NOTIFICATION_LISTENER_SETTINGS));
                            }
                        }
                    }).show();
            return;
        }
        showApps();
    }

    private void refreshState() {
        showApps();
    }

    private void showApps() {
        rvMostInstalledApps.setVisibility(View.VISIBLE);
        rvRareInstalledApps.setVisibility(View.VISIBLE);
        searchApp.setVisibility(View.VISIBLE);
//        Utils.showToast(activity,"No App Found",AppConstant.errorColor);
    }

    private void hideApps() {
        rvMostInstalledApps.setVisibility(View.GONE);
        rvRareInstalledApps.setVisibility(View.GONE);
        searchApp.setVisibility(View.GONE);
        Utils.showToast(activity, "No App Found", AppConstant.errorColor);
    }
}