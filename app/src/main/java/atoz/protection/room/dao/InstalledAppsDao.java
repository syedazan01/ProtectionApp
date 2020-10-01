package atoz.protection.room.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.ArrayList;
import java.util.List;

import atoz.protection.adapters.InstalledApps;
import atoz.protection.model.PInfo;

@Dao
public interface InstalledAppsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAppsInfo(PInfo pInfo);

    @Query("SELECT * FROM InstalledApps")
    List<PInfo> getInstallsApps();
}
