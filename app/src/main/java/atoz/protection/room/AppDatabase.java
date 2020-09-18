package atoz.protection.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import atoz.protection.model.RecordingFileData;
import atoz.protection.model.SpamBean;
import atoz.protection.room.dao.RecordeFileDao;
import atoz.protection.room.dao.SpamCallDao;
import atoz.protection.utils.AppConstant;

@Database(entities = {RecordingFileData.class, SpamBean.class}, exportSchema = false, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase INSTANCE=null;
    public static AppDatabase getAppDataBase(Context context)
    {
        if(INSTANCE==null)
        {
            INSTANCE= Room.databaseBuilder(context.getApplicationContext(),AppDatabase.class, AppConstant.DATABASE_NAME).build();
        }
        return INSTANCE;
    }
    abstract public RecordeFileDao getRecordFileDao();
    abstract public SpamCallDao getSpamCallDao();
}
