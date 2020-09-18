package atoz.protection.room.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import atoz.protection.model.RecordingFileData;

import java.util.List;

@Dao
public interface RecordeFileDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertRecordFile(RecordingFileData recordingFileData);

    @Query("SELECT COUNT(*) FROM RecordingFileData")
    int getCount();

    @Query("select * from RecordingFileData")
    List<RecordingFileData> getRecordingFiles();

    @Query("delete  from RecordingFileData where id=:id")
    void deleteRecord(int id);




}
