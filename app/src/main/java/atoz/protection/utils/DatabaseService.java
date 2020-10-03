package atoz.protection.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.provider.Telephony;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import atoz.protection.model.PInfo;
import atoz.protection.model.UserBean;
import atoz.protection.room.AppDatabase;

import static atoz.protection.utils.PrefManager.PREF_NAME;

public class DatabaseService extends Worker {
    private Context mContext;
    private AppDatabase database;

    public DatabaseService(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        mContext=context;
        database=AppDatabase.getAppDataBase(context);

    }

    @NonNull
    @Override
    public Result doWork() {
        database.getInstalledAppsDao().delete();
        int flags = PackageManager.GET_META_DATA |
                PackageManager.GET_SHARED_LIBRARY_FILES |
                PackageManager.GET_UNINSTALLED_PACKAGES;
        List<PackageInfo> packs = mContext.getPackageManager().getInstalledPackages(flags);
        for (int i = 0; i < packs.size(); i++) {
            PackageInfo appInfo = packs.get(i);
            String appName=appInfo.applicationInfo.loadLabel(mContext.getPackageManager()).toString().toLowerCase();
            if ((isSystemPackage(appInfo) && !(appName.equals("youtube") || appInfo.packageName.toLowerCase().equals(Telephony.Sms.getDefaultSmsPackage(mContext).toLowerCase())))) {
                continue;
            }
            /*if (appInfo.applicationInfo.loadLabel(mContext.getPackageManager()).toString().toLowerCase().equals("youtube") && ((isSystemPackage(appInfo)) || (appInfo.versionName == null))) {
                // System application
            } else {*/
                // Installed by user
                if (!mContext.getPackageName().equals(appInfo.packageName)) {
                    PInfo newInfo = new PInfo(mContext);
                    newInfo.setAppname(appName);
                    newInfo.setPname(appInfo.packageName);
                    newInfo.setVersionName(appInfo.versionName);
                    newInfo.setVersionCode(appInfo.versionCode);
                    newInfo.setIcon(newInfo.drawableToString(appInfo.applicationInfo.loadIcon(mContext.getPackageManager())));
                    database.getInstalledAppsDao().insertAppsInfo(newInfo);

                }
            }
//        Toast.makeText(mContext, "Running", Toast.LENGTH_SHORT).show();
        return Result.success();
    }
    private boolean isSystemPackage(PackageInfo pkgInfo) {
        Log.e("APPNAME>>>",Telephony.Sms.getDefaultSmsPackage(mContext).toLowerCase());
        return ((pkgInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
    }
}
