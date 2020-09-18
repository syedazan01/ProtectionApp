package atoz.protection.services;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import atoz.protection.R;
import atoz.protection.activites.SplashScreen;
import atoz.protection.model.RecordingFileData;
import atoz.protection.room.AppDatabase;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;

import atoz.protection.fragments.Recording_fragment;

/**
 * Created by Daniel on 12/28/2014.
 */
public class RecordingService extends Service {

    private static final String LOG_TAG = "RecordingService";

    private String mFileName = null;
    private String mFilePath = null;
    Toast toast;


//    NotificationManager mgr;
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
        if (Recording_fragment.mRecorder != null) {
            stopRecording();
        }

        super.onDestroy();
    }

    public void startRecording() {
        setFileNameAndPath();

        Recording_fragment.mRecorder = new MediaRecorder();
        Recording_fragment.mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        Recording_fragment.mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);

// After set output format
        Recording_fragment.mRecorder.setOutputFile(mFilePath);
        Recording_fragment.mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        Recording_fragment.mRecorder.setAudioChannels(1);
            Recording_fragment.mRecorder.setAudioSamplingRate(44100);
            Recording_fragment.mRecorder.setAudioEncodingBitRate(192000);

        try {
            Recording_fragment.mRecorder.prepare();
            Recording_fragment.mRecorder.start();
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
            Recording_fragment.mRecorder.stop();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
        mElapsedMillis = (System.currentTimeMillis() - mStartingTimeMillis);
        Recording_fragment.mRecorder.release();
//        mgr.cancel(1);
        if(toast!=null)
            toast.cancel();
        toast= Toast.makeText(this, getString(R.string.toast_recording_finish) + " " + mFilePath, Toast.LENGTH_LONG);
        toast.show();
        //remove notification
        if (mIncrementTimerTask != null) {
            mIncrementTimerTask.cancel();
            mIncrementTimerTask = null;
        }

        Recording_fragment.mRecorder = null;

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
                    if(Recording_fragment.onRecordFileSave!=null)
                    {
                        Recording_fragment.onRecordFileSave.onSave();
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
                /*mgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    NotificationChannel defaultChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
                    mgr.createNotificationChannel(defaultChannel);
                }
                mgr.notify(1, createNotification());*/

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
                        .setContentText(mTimerFormat.format(mElapsedSeconds*1000))
                        .setOngoing(true);

        mBuilder.setContentIntent(PendingIntent.getActivities(getApplicationContext(), 0,
                new Intent[]{new Intent(getApplicationContext(), SplashScreen.class)}, 0));

        return mBuilder.build();
    }
}
