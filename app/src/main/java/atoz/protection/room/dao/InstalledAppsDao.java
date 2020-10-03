package atoz.protection.room.dao;

import android.provider.Telephony;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.ArrayList;
import java.util.List;

import atoz.protection.activites.KillNotification;
import atoz.protection.adapters.InstalledApps;
import atoz.protection.model.PInfo;

@Dao
public interface InstalledAppsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAppsInfo(PInfo pInfo);

    @Query("SELECT * FROM InstalledApps WHERE pname!=:smsName AND appname!='whatsapp' AND appname!='instagram' AND appname!='facebook' AND appname!='telegram' AND appname!='youtube' GROUP BY pname LIMIT 10 OFFSET :count")
    List<PInfo> getInstallsApps(String smsName,int count);

    /*if (pInfo.getPname().equals(Telephony.Sms.getDefaultSmsPackage(KillNotification.this))
               || appName.equals("whatsapp") ||
               appName.equals("instagram") ||
               appName.equals("facebook") ||
               appName.equals("telegram") ||
               appName.equals("youtube")) {
   */
    @Query("SELECT * FROM InstalledApps WHERE pname=:smsName OR appname='whatsapp' OR appname='instagram' OR appname='facebook' OR appname='telegram' OR appname='youtube' GROUP BY pname")
    List<PInfo> getMostInstalledApps(String smsName);


    @Query("DELETE FROM InstalledApps")
    void delete();
}
