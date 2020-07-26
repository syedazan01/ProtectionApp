package com.example.protectionapp.services;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;


import androidx.core.app.NotificationCompat;

import com.example.protectionapp.R;
import com.example.protectionapp.activites.SplashScreen;
import com.example.protectionapp.model.RecordingFileData;
import com.example.protectionapp.room.AppDatabase;
import com.example.protectionapp.room.dao.RecordeFileDao;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static com.example.protectionapp.fragments.Recording_fragment.mRecorder;
import static com.example.protectionapp.fragments.Recording_fragment.onRecordFileSave;

/**
 * Created by Daniel on 12/28/2014.
 */
public class RecordingService extends Service {

    private static final String LOG_TAG = "RecordingService";

    private String mFileName = null;
    private String mFilePath = null;
    Toast toast;


    NotificationManager mgr;
    private long mStartingTimeMillis = 0;
    private long mElapsedMillis = 0;
    private int mElapsedSeconds = 0;
    private OnTimerChangedListener onTimerChangedListener = null;
    private static final SimpleDateFormat mTimerFormat = new SimpleDateFormat("mm:ss", Locale.getDefault());

    private Timer mTimer = null;
    private TimerTask mIncrementTimerTask = null;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public interface OnTimerChangedListener {
        void onTimerChanged(int seconds);
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startRecording();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        if (mRecorder != null) {
            stopRecording();
        }

        super.onDestroy();
    }

    public void startRecording() {
        setFileNameAndPath();

        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);

// After set output format
        mRecorder.setOutputFile(mFilePath);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        mRecorder.setAudioChannels(1);
            mRecorder.setAudioSamplingRate(44100);
            mRecorder.setAudioEncodingBitRate(192000);

        try {
            mRecorder.prepare();
            mRecorder.start();
            mStartingTimeMillis = System.currentTimeMillis();
            startTimer();

            //startTimer();
            //startForeground(1, createNotification());

        } catch (IOException e) {
            Log.e(LOG_TAG, e.getMessage());
        }
    }

    public void setFileNameAndPath(){


            Executors.newSingleThreadExecutor().execute(new Runnable() {
                @Override
                public void run() {
                    int count = 0;
                    File f;

                    do {
                        count++;
                        final int mCount;
                        mCount = AppDatabase.getAppDataBase(RecordingService.this).getRecordFileDao().getCount();
                        mFileName = getString(R.string.default_file_name)
                                + "_" + (mCount+count) + ".mp4";
                        mFilePath = Environment.getExternalStorageDirectory().getAbsolutePath();
                        mFilePath += "/SoundRecorder/" + mFileName;

                        f = new File(mFilePath);
                    }

                    while (f.exists() && !f.isDirectory());
                }

    });
    }

    public void stopRecording() {
        try {
            mRecorder.stop();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
        mElapsedMillis = (System.currentTimeMillis() - mStartingTimeMillis);
        mRecorder.release();
        mgr.cancel(1);
        if(toast!=null)
            toast.cancel();
        toast= Toast.makeText(this, getString(R.string.toast_recording_finish) + " " + mFilePath, Toast.LENGTH_LONG);
        toast.show();
        //remove notification
        if (mIncrementTimerTask != null) {
            mIncrementTimerTask.cancel();
            mIncrementTimerTask = null;
        }

        mRecorder = null;

        try {

            Executors.newSingleThreadExecutor().execute(new Runnable() {
                @Override
                public void run() {
                    RecordingFileData recordingFileData=new RecordingFileData();
                    recordingFileData.setFileName(mFileName);
                    recordingFileData.setFilePath(mFilePath);
                    recordingFileData.setCreated_date(new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss").format(new Date()));
                    recordingFileData.setEllipseMillis(mElapsedMillis);
                    AppDatabase.getAppDataBase(RecordingService.this).getRecordFileDao().insertRecordFile(recordingFileData);
                    if(onRecordFileSave!=null)
                    {
                        onRecordFileSave.onSave();
                    }
                }
            });
        } catch (Exception e){
            Log.e(LOG_TAG, "exception", e);
        }
    }

    private void startTimer() {
        mTimer = new Timer();
        // The id of the channel.
        final String CHANNEL_ID = "default";
        final String CHANNEL_NAME = "Default";
        mIncrementTimerTask = new TimerTask() {
            @Override
            public void run() {
                mElapsedSeconds++;
                if (onTimerChangedListener != null)
                    onTimerChangedListener.onTimerChanged(mElapsedSeconds);
                mgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    NotificationChannel defaultChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
                    mgr.createNotificationChannel(defaultChannel);
                }
                mgr.notify(1, createNotification());

            }
        };
        mTimer.scheduleAtFixedRate(mIncrementTimerTask, 1000, 1000);
    }

    //TODO:
    private Notification createNotification() {
        NotificationCompat.Builder mBuilder =
                (NotificationCompat.Builder)  new NotificationCompat.Builder(getApplicationContext(),"default")
                        .setSmallIcon(R.drawable.recording_button_icon)
                        .setContentTitle(getString(R.string.notification_recording))
                        .setContentText(mTimerFormat.format(0))
                        .setOngoing(true);

        mBuilder.setContentIntent(PendingIntent.getActivities(getApplicationContext(), 0,
                new Intent[]{new Intent(getApplicationContext(), SplashScreen.class)}, 0));

        return mBuilder.build();
    }
}
