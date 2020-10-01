package atoz.protection.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;

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
        List<PackageInfo> packs = mContext.getPackageManager().getInstalledPackages(0);
        for (int i = 0; i < packs.size(); i++) {
            PackageInfo p = packs.get(i);
            if ((p.versionName == null)) {
                continue;
            }
            PInfo newInfo = new PInfo(mContext);
            newInfo.setAppname(p.applicationInfo.loadLabel(mContext.getPackageManager()).toString());
            newInfo.setPname(p.packageName);
            newInfo.setVersionName(p.versionName);
            newInfo.setVersionCode(p.versionCode);
            newInfo.setIcon(newInfo.drawableToString(p.applicationInfo.loadIcon(mContext.getPackageManager())));
            database.getInstalledAppsDao().insertAppsInfo(newInfo);
        }
        return Result.success();
    }
}
