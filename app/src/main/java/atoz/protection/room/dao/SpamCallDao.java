package atoz.protection.room.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import atoz.protection.model.SpamBean;

import java.util.List;

@Dao
public interface SpamCallDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCalls(SpamBean spamBean);

    @Query("SELECT * FROM spamcalls")
    List<SpamBean> getSpamCalls();


    @Query("DELETE FROM spamcalls WHERE id=:id")
        void deleteSpamCall(int id);

    @Query("UPDATE spamcalls set isChecked=:isChecked WHERE id=:id")
    void updateCheck(int id,boolean isChecked);
}
