package atoz.protection.activites;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.provider.Telephony;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.SwitchCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import atoz.protection.R;
import atoz.protection.adapters.InstalledApps;
import atoz.protection.interfacecallbacks.OnNotificationChecked;
import atoz.protection.model.PInfo;
import atoz.protection.room.AppDatabase;
import atoz.protection.utils.AppConstant;
import atoz.protection.utils.PrefManager;
import atoz.protection.utils.Utils;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class KillNotification extends AppCompatActivity implements OnNotificationChecked {
    ImageView ivBack;
    TextView tvToolbarTitle;
    ConstraintLayout constMostUsed, constRareUsed;
    RecyclerView rvMostInstalledApps, rvRareInstalledApps;
    LinearLayoutManager linearLayoutManager;
    androidx.appcompat.widget.SearchView searchApp;
    SwitchCompat swAll,swPriorApps;
    SharedPreferences pref;
    ArrayList<PInfo> pInfos = new ArrayList<>();
    ArrayList<PInfo> mostPInfos = new ArrayList<>();
    InstalledApps installedAppAdapter, mostInstalledAppAdapter;
    Activity activity = KillNotification.this;
    private ProgressDialog pd;
    private CompoundButton.OnCheckedChangeListener swAllListener=new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if (hasAccessGranted()) {
                installedAppAdapter.setAllCheck(b,true);
            }
            else
            {
                swAll.setOnCheckedChangeListener(null);
                swAll.setChecked(false);
                swAll.setOnCheckedChangeListener(this);
                Utils.showToast(KillNotification.this,"Allow Notification from device settings",AppConstant.errorColor);
            }
            PrefManager.putBoolean(AppConstant.RARE_NOTIFICATION,b);
        }
    };
    private CompoundButton.OnCheckedChangeListener swPriorListener=new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if (hasAccessGranted()) {
                mostInstalledAppAdapter.setAllCheck(b,true);
            }
            else
            {
                swPriorApps.setOnCheckedChangeListener(null);
                swPriorApps.setChecked(false);
                swPriorApps.setOnCheckedChangeListener(this);
                Utils.showToast(KillNotification.this,"Allow Notification from device settings",AppConstant.errorColor);
            }
            PrefManager.putBoolean(AppConstant.MOST_NOTIFICATION,b);

        }
    };
    private int loadCount=1;
    private boolean isLoad=true;
    private boolean isLoading=true;
    private boolean isLastPage=false;
    private ProgressBar pbScroll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      /*  if (PrefManager.getBoolean(ISBLUELIGHT))
            setTheme(R.style.AppTheme_Base_Night);
        else*/
            //setTheme(R.style.AppTheme_Base_Light);
        setContentView(R.layout.activity_kill_notification);
        iniiViews();
        initActions();
    }

    private void getInstalledAppsList() {
//        pd.show();
        /*Observable<List<PInfo>> pInfoobserve = Observable.create(emitter -> {
            pInfos = new PInfo(activity).getInstalledApps(false);
            List<PInfo> tempPInfos = new ArrayList<>(pInfos);
            for (int i = 0; i < tempPInfos.size(); i++) {
                PInfo pInfo = tempPInfos.get(i);
                String appName = pInfo.getAppname().toLowerCase();
                if (appName.equals("whatsapp") || appName.equals("instagram") || appName.equals("facebook") || appName.equals("telegram") || appName.equals("youtube")) {
                    mostPInfos.add(pInfo);
                    pInfos.remove(pInfo);
                }
            }
        });*/
       /* Observable.just(getPackageManager().getInstalledPackages(0))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> {pd.show();})
                .doOnNext(packageInfos -> {
                    Log.e("fbefesfb",packageInfos.get(1).packageName);
                    for (int j = 0; j < packageInfos.size(); j++) {
                        PackageInfo p = packageInfos.get(j);
                        if (p.versionName == null) {
                            continue;
                        }
                        PInfo newInfo = new PInfo(KillNotification.this);
                        newInfo.appname = p.applicationInfo.loadLabel(getPackageManager()).toString();
                        newInfo.pname = p.packageName;
                        newInfo.versionName = p.versionName;
                        newInfo.versionCode = p.versionCode;
                        newInfo.icon = p.applicationInfo.loadIcon(getPackageManager());
                    }
                        List<PInfo> tempPInfos = new ArrayList<>(pInfos);
                        for (int i = 0; i < tempPInfos.size(); i++) {
                            PInfo pInfo = tempPInfos.get(i);
                            String appName = pInfo.getAppname().toLowerCase();
                            if (appName.equals("whatsapp") || appName.equals("instagram") || appName.equals("facebook") || appName.equals("telegram") || appName.equals("youtube")) {
                                mostPInfos.add(pInfo);
                                pInfos.remove(pInfo);
                            }
                        }
                        if (mostPInfos.size() < 0) {
                            constMostUsed.setVisibility(View.GONE);
                        }
                        installedAppAdapter = new InstalledApps(activity, (ArrayList<PInfo>) pInfos, KillNotification.this, AppConstant.RAREUSED);
                        mostInstalledAppAdapter = new InstalledApps(activity, mostPInfos, KillNotification.this, AppConstant.MOSTUSED);
                        rvMostInstalledApps.setAdapter(mostInstalledAppAdapter);
                        rvRareInstalledApps.setAdapter(installedAppAdapter);
                        pd.dismiss();
                })
                .subscribe(new Observer<List<PackageInfo>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(List<PackageInfo> packageInfos) {
                        Log.e("fbefesfb",packageInfos.get(0).packageName);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });*/
    }

    private void initActions() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        swAll.setOnCheckedChangeListener(swAllListener);
        swPriorApps.setOnCheckedChangeListener(swPriorListener);
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
            public boolean onQueryTextSubmit(String s)
            {
                if (TextUtils.isEmpty(s)) {
                    installedAppAdapter.submitList(pInfos);
                    mostInstalledAppAdapter.submitList(mostPInfos);
                }

                String query = s.toLowerCase();
                ArrayList<PInfo> filteredPInfo = new ArrayList<>();
                ArrayList<PInfo> mostFilteredPInfo = new ArrayList<>();

                for (PInfo pInfo : mostPInfos) {
                    String label = pInfo.getAppname().toLowerCase();
                    if (label.contains(query)) {
                        mostFilteredPInfo.add(pInfo);
                    }
                }
                for (PInfo pInfo : pInfos) {
                    String label = pInfo.getAppname().toLowerCase();
                    if (label.contains(query)) {
                        filteredPInfo.add(pInfo);
                    }
                }
                installedAppAdapter.submitList(filteredPInfo);
                mostInstalledAppAdapter.submitList(mostFilteredPInfo);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (TextUtils.isEmpty(s)) {
                    installedAppAdapter.submitList(pInfos);
                    mostInstalledAppAdapter.submitList(mostPInfos);
                }
                return false;
            }
        });
    }

    private void iniiViews() {
        pref = Utils.getDefaultManager(this);
        pbScroll = findViewById(R.id.pbScroll);
        searchApp = findViewById(R.id.searchApp);
        swPriorApps = findViewById(R.id.swPriorApps);
        swAll = findViewById(R.id.swAll);
        ivBack = findViewById(R.id.ivBack);
        tvToolbarTitle = findViewById(R.id.tvToolbarTitle);
        constRareUsed = findViewById(R.id.constRareUsed);
        constMostUsed = findViewById(R.id.constMostUsed);
        rvMostInstalledApps = findViewById(R.id.rvMostInstalledApps);
        rvRareInstalledApps = findViewById(R.id.rvRareInstalledApps);
        tvToolbarTitle.setText("Kill Notifications");
        rvMostInstalledApps.setLayoutManager(new LinearLayoutManager(this));
        linearLayoutManager=new LinearLayoutManager(this);
        rvRareInstalledApps.setLayoutManager(linearLayoutManager);
        installedAppAdapter = new InstalledApps(activity, pInfos, KillNotification.this, AppConstant.RAREUSED);
        mostInstalledAppAdapter = new InstalledApps(activity, mostPInfos, KillNotification.this, AppConstant.MOSTUSED);
        rvMostInstalledApps.setAdapter(mostInstalledAppAdapter);
       /* mAutoLoadMoreAdapter = new AutoLoadMoreAdapter(this, installedAppAdapter);
        mAutoLoadMoreAdapter.setOnLoadListener(new AutoLoadMoreAdapter.OnLoadListener() {
            @Override
            public void onRetry() {
                //do retry
                new InstalledAppsAsyncTask().execute();
            }

            @Override
            public void onLoadMore() {
                //do load more
                new InstalledAppsAsyncTask().execute();
            }
        });*/
        /*mAutoLoadMoreAdapter.setConfig(new AutoLoadMoreConfig
                .Builder()
                .loadingView(R.layout.custom_loading)
                .loadFailedView(R.layout.custom_load_failed)
                .create());*/
        rvRareInstalledApps.setAdapter(installedAppAdapter);
        rvRareInstalledApps.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int visibleItemCount = linearLayoutManager.getChildCount();
                int totalItemCount = linearLayoutManager.getItemCount();
                int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();

                if (!isLoading && !recyclerView.canScrollHorizontally(1)) {
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                            && firstVisibleItemPosition >= 0
                            && totalItemCount >= 10) {

                        new InstalledAppsAsyncTask().execute();
                    }
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

            }
        });
        pd = Utils.getProgressDialog(this);
//        getInstalledAppsList();
//        final ProgressDialog dialog = Utils.getProgressDialog(activity);
//        pd.show();
        /*Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                pInfos = (ArrayList<PInfo>) AppDatabase.getAppDataBase(KillNotification.this).getInstalledAppsDao().getList();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


//                pInfos = new PInfo(activity).getInstalledApps(false);
                List<PInfo> tempPInfos = new ArrayList<>(pInfos);
                for (int i = 0; i < tempPInfos.size(); i++) {
                    PInfo pInfo = tempPInfos.get(i);
                    String appName = pInfo.getAppname().toLowerCase();
                    if (pInfo.getPname().equals(Telephony.Sms.getDefaultSmsPackage(KillNotification.this)) || appName.equals("whatsapp") || appName.equals("instagram") || appName.equals("facebook") || appName.equals("telegram") || appName.equals("youtube")) {
                        mostPInfos.add(pInfo);
                        pInfos.remove(pInfo);
                    }
                }

                        if (mostPInfos.size() < 0) {
                            constMostUsed.setVisibility(View.GONE);
                        }
                   mostInstalledAppAdapter.notifyList(mostPInfos);
                        installedAppAdapter.notifyList(pInfos);
                        pd.dismiss();

                    }
                });
            }
        });*/
        new MostInstalledAppsAsyncTask().execute();
        new InstalledAppsAsyncTask().execute();
    }

    @Override
    public void onCheckboxAppChecked(int position, boolean isChecked, String typeOfList) {
        String pkg = null;
        if (typeOfList.equals(AppConstant.RAREUSED)) {
            if(!isChecked)
            {
                Log.e("CHECKED>>>","RARE");
                PrefManager.putBoolean(AppConstant.RARE_NOTIFICATION,false);
                swAll.setOnCheckedChangeListener(null);
                swAll.setChecked(false);
                swAll.setOnCheckedChangeListener(swAllListener);
            }
            pkg = installedAppAdapter.getItem(position).getPname();
        } else {
            if(!isChecked)
            {
                Log.e("CHECKED>>>","MOST");
                PrefManager.putBoolean(AppConstant.MOST_NOTIFICATION,false);
                swPriorApps.setOnCheckedChangeListener(null);
                swPriorApps.setChecked(false);
                swPriorApps.setOnCheckedChangeListener(swPriorListener);
            }
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

    public Boolean hasAccessGranted() {
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
        pref.edit().putBoolean(AppConstant.NOTIFICATION_ENABLE,true).apply();
        if (PrefManager.getBoolean(AppConstant.RARE_NOTIFICATION)) {
            swAll.setChecked(true);
        }
        if (PrefManager.getBoolean(AppConstant.MOST_NOTIFICATION)) {
            swPriorApps.setChecked(true);
        }
//        Utils.showToast(activity,"No App Found",AppConstant.errorColor);
    }

    private void hideApps() {
        rvMostInstalledApps.setVisibility(View.GONE);
        rvRareInstalledApps.setVisibility(View.GONE);
        searchApp.setVisibility(View.GONE);
        Utils.showToast(activity, "No App Found", AppConstant.errorColor);
    }
class InstalledAppsAsyncTask extends AsyncTask<Void,Void,List<PInfo>>{
    @Override
    protected List<PInfo> doInBackground(Void... voids) {

        return AppDatabase.getAppDataBase(KillNotification.this).getInstalledAppsDao().getInstallsApps(Telephony.Sms.getDefaultSmsPackage(KillNotification.this).toLowerCase(),loadCount);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pbScroll.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onPostExecute(List<PInfo> pInfos) {
        super.onPostExecute(pInfos);

        /*if (isLoad) {
            mostPInfos.clear();
            isLoad=false;
            List<PInfo> tempPInfos = new ArrayList<>(pInfos);
            for (int i = 0; i < tempPInfos.size(); i++) {
                PInfo pInfo = tempPInfos.get(i);
                String appName = pInfo.getAppname().toLowerCase();
                if (pInfo.getPname().equals(Telephony.Sms.getDefaultSmsPackage(KillNotification.this)) || appName.equals("whatsapp") || appName.equals("instagram") || appName.equals("facebook") || appName.equals("telegram") || appName.equals("youtube")) {
                    mostPInfos.add(pInfo);
                    pInfos.remove(pInfo);
                }
            }
            if (mostPInfos.size()== 0) {
                constMostUsed.setVisibility(View.GONE);
            }
            mostInstalledAppAdapter.notifyList(mostPInfos);
        }*/
        KillNotification.this.pInfos.addAll(pInfos);
        int lastCount;
        /*if(loadCount-10<0)
            lastCount=0;
        else
            lastCount=loadCount-10;*/
//        installedAppAdapter.removeNotifyList(lastCount,KillNotification.this.pInfos);
        installedAppAdapter.notifyList(loadCount,KillNotification.this.pInfos);
        pd.dismiss();
        if (isLoad) {
            isLoad=false;
            loadCount=0;
        }
        loadCount+=10;
        isLoading=false;
        pbScroll.setVisibility(View.GONE);
    }
}
    class MostInstalledAppsAsyncTask extends AsyncTask<Void,Void,List<PInfo>>{
        @Override
        protected List<PInfo> doInBackground(Void... voids) {

            return AppDatabase.getAppDataBase(KillNotification.this).getInstalledAppsDao().getMostInstalledApps(Telephony.Sms.getDefaultSmsPackage(KillNotification.this).toLowerCase());
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(List<PInfo> pInfos) {
            super.onPostExecute(pInfos);
            if (pInfos.size()== 0) {
                constMostUsed.setVisibility(View.GONE);
                swPriorApps.setVisibility(View.GONE);
            }
            else
            {
                swPriorApps.setVisibility(View.VISIBLE);
                constMostUsed.setVisibility(View.VISIBLE);
                mostPInfos=(ArrayList<PInfo>) pInfos;
                mostInstalledAppAdapter.notifyList(0,(ArrayList<PInfo>) pInfos);
            }

        }
    }
}