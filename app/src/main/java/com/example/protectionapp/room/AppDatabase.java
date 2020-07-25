package com.example.protectionapp.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.protectionapp.model.RecordingFileData;
import com.example.protectionapp.room.dao.RecordeFileDao;
import com.example.protectionapp.utils.AppConstant;

@Database(entities = {RecordingFileData.class}, exportSchema = false, version = 1)
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
}
