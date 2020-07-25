package com.example.protectionapp.room.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.protectionapp.model.RecordingFileData;

import java.util.List;

@Dao
public interface RecordeFileDao {
    @Insert
    void insertRecordFile(RecordingFileData recordingFileData);

    @Query("SELECT COUNT(*) FROM RecordingFileData")
    int getCount();

    @Query("select * from RecordingFileData")
    List<RecordingFileData> getRecordingFiles();
}
