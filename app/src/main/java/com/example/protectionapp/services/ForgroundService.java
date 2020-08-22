package com.example.protectionapp.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.TaskStackBuilder;

import com.example.protectionapp.R;
import com.example.protectionapp.activites.SplashScreen;
import com.example.protectionapp.utils.PrefManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.example.protectionapp.utils.AppConstant.ISNIGHTMODE;

public class ForgroundService extends Service {
    int volumePrev =0;
    private FrameLayout screen_frame;
    private ImageView ivScreenShot;
    private WindowManager mWindowManager;
    private View mFloatingWidget;
    private static final String TAG_FOREGROUND_SERVICE = "FOREGROUND_SERVICE";

    public static final String ACTION_START_FOREGROUND_SERVICE = "ACTION_START_FOREGROUND_SERVICE";

    public static final String ACTION_STOP_FOREGROUND_SERVICE = "ACTION_STOP_FOREGROUND_SERVICE";

    public static final String ACTION_PAUSE = "ACTION_PAUSE";

    public static final String ACTION_PLAY = "ACTION_PLAY";


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        if (PrefManager.getBoolean(ISNIGHTMODE))
            setTheme(R.style.AppTheme_Base_Night);
        else
            setTheme(R.style.AppTheme_Base_Light);

        mFloatingWidget = LayoutInflater.from(this).inflate(R.layout.layout_floating_screenshot, null);
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT, Build.VERSION.SDK_INT > Build.VERSION_CODES.O
                ? WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
                : WindowManager.LayoutParams.TYPE_SYSTEM_ERROR,
                WindowManager.LayoutParams.FORMAT_CHANGED|WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                PixelFormat.TRANSLUCENT);
//        params.gravity = Gravity.TOP | Gravity.END;
//        params.x =0;
//        params.y =100;
//        mWindowManager.updateViewLayout(mFloatingWidget,params);
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mWindowManager.addView(mFloatingWidget, params);
        Log.e(TAG_FOREGROUND_SERVICE, "My foreground service onCreate().");
      screen_frame=mFloatingWidget.findViewById(R.id.screen_frame);
      ivScreenShot=mFloatingWidget.findViewById(R.id.ivScreenShot);
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.camera_detector);
        mediaPlayer.setVolume(0,0);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();

        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                if ("android.media.VOLUME_CHANGED_ACTION".equals(intent.getAction())) {

                    int volume = intent.getIntExtra("android.media.EXTRA_VOLUME_STREAM_VALUE", 0);

                    Log.i("dfzfvbdbg", "volume = " + volume);

                        screen_frame.setVisibility(View.VISIBLE);
                        Bitmap bitmap=screenShot(mFloatingWidget);
                        storeImage(bitmap);

                        ivScreenShot.setImageBitmap(bitmap);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        screen_frame.setVisibility(View.GONE);
                    }
/*                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } finally {
                            screen_frame.setVisibility(View.GONE);
                        }

                        Log.e("sdfgbdfbf", "You have pressed volume up button");
                    } else {
                        Log.e("dfvbdfbdf", "You have pressed volume down button");
                    }*/
                    volumePrev = volume;
                }
            }
        };
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.media.VOLUME_CHANGED_ACTION");
        registerReceiver(broadcastReceiver, filter);

        if (intent != null) {
            String action = intent.getAction();
            if (action != null)

                switch (action) {
                    case ACTION_START_FOREGROUND_SERVICE:
                        startForegroundService();
//                        Toast.makeText(getApplicationContext(), "Foreground service is started.", Toast.LENGTH_LONG).show();
                        break;
                    case ACTION_STOP_FOREGROUND_SERVICE:
                        stopForegroundService();
//                        Toast.makeText(getApplicationContext(), "Foreground service is stopped.", Toast.LENGTH_LONG).show();
                        break;
                    case ACTION_PLAY:
//                        Toast.makeText(getApplicationContext(), "You click Play button.", Toast.LENGTH_LONG).show();
                        break;
                    case ACTION_PAUSE:
//                        Toast.makeText(getApplicationContext(), "You click Pause button.", Toast.LENGTH_LONG).show();
                        break;
                }
        }
        return START_STICKY;
    }
    public Bitmap screenShot(View view) {
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache(true);
        Bitmap b =Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);
        return  b;
//        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(),
//                view.getHeight(), Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(bitmap);
//        view.draw(canvas);
//        return bitmap;
    }
    private void addImageGallery( File file ) {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DATA, file.getAbsolutePath());
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg"); // or image/png
        getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
    }
    private void storeImage(Bitmap image) {
        File pictureFile = getOutputMediaFile();
        if (pictureFile == null) {
            Log.d("TAG",
                    "Error creating media file, check storage permissions: ");// e.getMessage());
            return;
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.PNG, 90, fos);
            addImageGallery(pictureFile);
            fos.close();
        } catch (FileNotFoundException e) {
            Log.d("TAG", "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d("TAG", "Error accessing file: " + e.getMessage());
        }
    }
    private  File getOutputMediaFile(){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                + "/ProtectionScreetShots");

        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date());
        File mediaFile;
        String mImageName="MI_"+ timeStamp +".jpg";
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);
        return mediaFile;
    }
    /* Used to build and start foreground service. */
    private void startForegroundService() {
        Log.d(TAG_FOREGROUND_SERVICE, "Start foreground service.");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel("my_service", "My Background Service");
        } else {

            // Create notification default intent.
            Intent intent = new Intent();
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

            // Create notification builder.
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

            // Make notification show big text.
            NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
            bigTextStyle.setBigContentTitle("Call Recording implemented by foreground service.");
            bigTextStyle.bigText("Android foreground service is a android service which can run in foreground always, it can be controlled by user via notification.");
            // Set big text style.
            builder.setStyle(bigTextStyle);

            builder.setWhen(System.currentTimeMillis());
            builder.setSmallIcon(R.drawable.login_logo);
            Bitmap largeIconBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.login_logo);
            builder.setLargeIcon(largeIconBitmap);
            // Make the notification max priority.
            builder.setPriority(Notification.PRIORITY_MAX);
            // Make head-up notification.
            builder.setFullScreenIntent(pendingIntent, true);

       /*     // Add Play button intent in notification.
            Intent playIntent = new Intent(this, ForgroundService.class);
            playIntent.setAction(ACTION_PLAY);
            PendingIntent pendingPlayIntent = PendingIntent.getService(this, 0, playIntent, 0);
            NotificationCompat.Action playAction = new NotificationCompat.Action(android.R.drawable.ic_media_play, "Play", pendingPlayIntent);
            builder.addAction(playAction);

            // Add Pause button intent in notification.
            Intent pauseIntent = new Intent(this, ForgroundService.class);
            pauseIntent.setAction(ACTION_PAUSE);
            PendingIntent pendingPrevIntent = PendingIntent.getService(this, 0, pauseIntent, 0);
            NotificationCompat.Action prevAction = new NotificationCompat.Action(android.R.drawable.ic_media_pause, "Pause", pendingPrevIntent);
            builder.addAction(prevAction);*/

            // Build the notification.
            Notification notification = builder.build();

            // Start foreground service.
            startForeground(1, notification);
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private void createNotificationChannel(String channelId, String channelName) {
        Intent resultIntent = new Intent(this, SplashScreen.class);
// Create the TaskStackBuilder and add the intent, which inflates the back stack
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationChannel chan = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelId);
        Notification notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(R.drawable.login_logo)
                .setContentTitle("App is running in background")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .setContentIntent(resultPendingIntent) //intent
                .build();
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(1, notificationBuilder.build());
        startForeground(1, notification);
    }


    private void stopForegroundService() {
        Log.d(TAG_FOREGROUND_SERVICE, "Stop foreground service.");

        // Stop foreground service and remove the notification.
        stopForeground(true);

        // Stop the foreground service.
        stopSelf();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mFloatingWidget != null) mWindowManager.removeView(mFloatingWidget);
    }
}
