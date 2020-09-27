package atoz.protection.services;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.SearchManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.Image;
import android.media.ImageReader;
import android.media.MediaRecorder;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroupOverlay;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.TaskStackBuilder;

import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import atoz.protection.Protection;
import atoz.protection.R;
import atoz.protection.activites.CallRecorder;
import atoz.protection.activites.FileShare;
import atoz.protection.activites.SplashScreen;
import atoz.protection.fragments.HomeFragment;
import atoz.protection.receivers.BootCompleteReceiver;
import atoz.protection.utils.AppConstant;
import atoz.protection.utils.PrefManager;
import atoz.protection.utils.Utils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Semaphore;

import atoz.protection.fragments.UtilityFeaturesFragment;

import static android.app.Activity.RESULT_OK;
import static android.hardware.display.DisplayManager.VIRTUAL_DISPLAY_FLAG_PRESENTATION;
import static atoz.protection.utils.AppConstant.ISBLUELIGHT;

public class FloatingWindowService extends Service implements View.OnClickListener {
    private ImageReader mImageReader;

    private int windowWidth;
    private int windowHeight;
    private int mScreenDensity;

    public static int mResultCode;
    public static Intent mResultData;
    //Screenshot variable
    private Intent mediaProjectionIntent;
    private boolean isGetUserPermission;
    public  MediaProjection mProjection;

    private static final String TAG = "RECORDERSERVICE";
    private static final String EXTRA_RESULT_CODE = "resultcode";
    private static final String EXTRA_DATA = "data";
    private Intent intent;


    public static final String LAUNCHER_WIDGET = "action.floating.launcher";
    public static final String BLUE_LIGHT_FILTER = "action.bluelight";
    public static final String SCREEN_ON = "action.screenOn";
    WindowManager.LayoutParams flaotingParams, blueLightparams;
    private int bluelightFilterCode = Color.parseColor("#4DF7E6B4");
    private WindowManager mWindowManager, blueLightWindowManager;
    private View mFloatingView, blue_filter_container;
    private FloatingActionButton collapsed_iv, fabFileShare, fabVoice, fabSearch, fabScreenShot, fabClose;
    private EditText etSearchQuery;
    private LinearLayout llExpandView;
    private MediaProjectionManager mProjectionManager;
    private VirtualDisplay mVirtualDisplay;

    public FloatingWindowService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        startForegroundService();
      createVirtualEnvironment();
//        startActivity(mProjectionManager.createScreenCaptureIntent().setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        initFloatingWidget();
        initBlueLightFilterWidget();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void initBlueLightFilterWidget() {
        blue_filter_container = LayoutInflater.from(this).inflate(R.layout.blue_light_filter, null);
        if (PrefManager.getBoolean(ISBLUELIGHT))
            blue_filter_container.setBackgroundColor(bluelightFilterCode);
        //Add the view to the blueLight window.
        blueLightparams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
                        ? WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY :
                        WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                PixelFormat.TRANSLUCENT);

        //Specify the view position
        blueLightparams.gravity = Gravity.TOP | Gravity.START;        //Initially view will be added to top-left corner
        blueLightparams.x = 0;
        blueLightparams.y = 0;
        //Add the view to the window
        blueLightWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        blueLightWindowManager.addView(blue_filter_container, blueLightparams);
    }

    private void initFloatingWidget() {
        //Inflate the floating view layout we created
        setTheme(R.style.AppTheme_Base_Light);
        mFloatingView = LayoutInflater.from(this).inflate(R.layout.layout_floating_widget, null);
        mFloatingView.setVisibility(View.GONE);
        collapsed_iv = mFloatingView.findViewById(R.id.collapsed_iv);
        llExpandView = mFloatingView.findViewById(R.id.expanded_container);
        fabFileShare = mFloatingView.findViewById(R.id.fabFileShare);
        fabVoice = mFloatingView.findViewById(R.id.fabVoice);
        fabSearch = mFloatingView.findViewById(R.id.fabSearch);
        etSearchQuery = mFloatingView.findViewById(R.id.etSearchQuery);
        fabScreenShot = mFloatingView.findViewById(R.id.fabScreenShot);
        fabClose = mFloatingView.findViewById(R.id.fabClose);

        fabFileShare.setOnClickListener(this);
        fabVoice.setOnClickListener(this);
        fabSearch.setOnClickListener(this);
        fabScreenShot.setOnClickListener(this);
        fabClose.setOnClickListener(this);
        etSearchQuery.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    Utils.hideKeyboardFrom(FloatingWindowService.this,etSearchQuery);
                    flaotingParams.flags=WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
                    mWindowManager.updateViewLayout(mFloatingView,flaotingParams);
                    collapseView();
                    fadAniamtion();
                    Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra(SearchManager.QUERY, etSearchQuery.getText().toString()); // query contains search string
                    startActivity(intent);

                    return true;
                }
                return false;
            }
        });
       /* etSearchQuery.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH
                        || i == EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                        && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    Utils.hideKeyboardFrom(FloatingWindowService.this,etSearchQuery);
                    String escapedQuery = null;
                    try {
                        escapedQuery = URLEncoder.encode(etSearchQuery.getText().toString(), "UTF-8");
                        Uri uri = Uri.parse("http://www.google.com/#q=" + escapedQuery);
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        etSearchQuery.setVisibility(View.GONE);
                        mFloatingView.findViewById(R.id.expanded_container).setVisibility(View.GONE);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                        Toast.makeText(FloatingWindowService.this, "Invalid text", Toast.LENGTH_SHORT).show();
                    }

                    return true;
                }
                return false;
            }
        });*/
        //Add the view to the window.
        flaotingParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
                        ? WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY :
                        WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        //Specify the view position
        flaotingParams.gravity = Gravity.TOP | Gravity.START;        //Initially view will be added to top-left corner
        flaotingParams.x = 0;
        flaotingParams.y = 100;
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mWindowManager.addView(mFloatingView, flaotingParams);
        //Drag and move floating view using user's touch action.
        collapsed_iv.setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        //remember the initial position.
                        initialX = flaotingParams.x;
                        initialY = flaotingParams.y;

                        //get the touch location
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        return true;
                    case MotionEvent.ACTION_UP:
                        int Xdiff = (int) (event.getRawX() - initialTouchX);
                        int Ydiff = (int) (event.getRawY() - initialTouchY);

                        //The check for Xdiff <10 && YDiff< 10 because sometime elements moves a little while clicking.
                        //So that is click event.
                        if (v == collapsed_iv && Xdiff < 10 && Ydiff < 10) {
                            if (isViewCollapsed()) {
                                //When user clicks on the image view of the collapsed layout,
                                //visibility of the collapsed layout will be changed to "View.GONE"
                                //and expanded view will become visible.
//                                collapsedView.setVisibility(View.GONE);
                                llExpandView.setVisibility(View.VISIBLE);
                            } else {
                                llExpandView.setVisibility(View.GONE);
                            }
                            fadAniamtion();
                        }
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        //Calculate the X and Y coordinates of the view.
                        flaotingParams.x = initialX + (int) (event.getRawX() - initialTouchX);
                        flaotingParams.y = initialY + (int) (event.getRawY() - initialTouchY);

                        //Update the layout with new X & Y coordinate
                        mWindowManager.updateViewLayout(mFloatingView, flaotingParams);
                        return true;
                }
                return false;
            }
        });
    }
    /**
     * Detect if the floating view is collapsed or expanded.
     *
     * @return true if the floating view is collapsed.
     */
    private boolean isViewCollapsed() {
        return mFloatingView == null || llExpandView.getVisibility() == View.GONE;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy()");
        tearDownMediaProjection();
        if (mFloatingView != null) mWindowManager.removeView(mFloatingView);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getAction() != null && intent.getAction().equals(LAUNCHER_WIDGET)) {
            if (PrefManager.getBoolean(AppConstant.OVERLAY))
            {
                this.intent=intent;
                mFloatingView.setVisibility(View.VISIBLE);
            }
            else
            {
                mFloatingView.setVisibility(View.GONE);
            }
        } else if (intent.getAction() != null && intent.getAction().equals(BLUE_LIGHT_FILTER)) {

            if (PrefManager.getBoolean(ISBLUELIGHT)) {

                blue_filter_container.setBackgroundColor(bluelightFilterCode);
            } else {
                if (blue_filter_container != null)
                    blue_filter_container.setBackgroundColor(0);
            }

        } /*else if (intent.getAction() != null && intent.getAction().equals(SCREEN_ON))
            pattern_lock_container.setVisibility(View.VISIBLE);*/
        return START_STICKY;
    }

    @Override
    public void onClick(View view) {
        if(view==fabFileShare)
        {
            collapseView();
            startActivity(new Intent(Protection.getInstance(), FileShare.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
        else if(view==fabVoice)
        {
            collapseView();
            startActivity(new Intent(Protection.getInstance(), CallRecorder.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
        else if(view==fabScreenShot)
        {
            collapseView();
            if(PrefManager.getBoolean(AppConstant.CAPTURE_SCREEN))
            {

                captureScreen();
            }
            else
                Toast.makeText(this, "Allow Screen Capture Permission", Toast.LENGTH_SHORT).show();
            /*Display display = mWindowManager.getDefaultDisplay();
            DisplayMetrics metrics = new DisplayMetrics();
            display.getMetrics(metrics);
            int width = metrics.widthPixels;
            int height = metrics.heightPixels;
            Bitmap bitmap = Bitmap.createBitmap(width,
                    height, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            view.draw(canvas);
            storeImage(bitmap);*/
        }
        else if(view==fabSearch)
        {
            etSearchQuery.setVisibility(View.VISIBLE);
            setAlphaAnimation(etSearchQuery);
            flaotingParams.flags=0;
            mWindowManager.updateViewLayout(mFloatingView,flaotingParams);

        }
        else if(view==fabClose)
        {
            collapseView();
            PrefManager.putBoolean(AppConstant.OVERLAY,false);
            if (HomeFragment.onFabClick!=null) {
                HomeFragment.onFabClick.onClose();
            }
            mFloatingView.setVisibility(View.GONE);
        }
    }

    private void collapseView() {
        llExpandView.setVisibility(View.GONE);
        etSearchQuery.setVisibility(View.GONE);

    }
    private void startToShot() {
        Handler start = new Handler();
        start.postDelayed(new Runnable() {
            public void run() {
                startCapture();
                stopScreenCapture();
            }
        }, 800);

    }
    @SuppressLint("WrongConstant")
    private void setUpVirtualDisplay() {
        Log.i(TAG, "Setting up a VirtualDisplay: " +
                windowWidth + "x" + windowHeight +
                " (" + mScreenDensity + ")");
        mImageReader = ImageReader.newInstance(windowWidth, windowHeight, PixelFormat.RGBA_8888, 2); //ImageFormat.RGB_565
        mVirtualDisplay = mProjection.createVirtualDisplay("ScreenCapture",
                windowWidth, windowHeight, mScreenDensity,
                DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                mImageReader.getSurface(), null, null);
    }
    private void setUpMediaProjection() {
        mResultData = ((Protection) getApplication()).getIntent();
        mResultCode = ((Protection) getApplication()).getResult();
        Log.i(TAG, "mResultData=" + mResultData);
        Log.i(TAG, "mResultCode=" + mResultCode);
        mProjectionManager = ((Protection) getApplication()).getMediaProjectionManager();
        mProjection = mProjectionManager.getMediaProjection(mResultCode, mResultData);
        Log.i(TAG, "mMediaProjection defined");
    }
    private void startCapture() {

        Image image = mImageReader.acquireLatestImage();
        int width = image.getWidth();
        int height = image.getHeight();
        final Image.Plane[] planes = image.getPlanes();
        final ByteBuffer buffer = planes[0].getBuffer();
        int pixelStride = planes[0].getPixelStride();
        int rowStride = planes[0].getRowStride();
        int rowPadding = rowStride - pixelStride * width;
        Bitmap bitmap = Bitmap.createBitmap(width + rowPadding / pixelStride, height, Bitmap.Config.ARGB_8888);
        bitmap.copyPixelsFromBuffer(buffer);
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height);
        image.close();
        storeImage(bitmap);
    }
    @SuppressLint("WrongConstant")
    private void captureScreen() {

            if (mProjection != null) {
                Log.i(TAG, "exec startShot()/setUpVirtualDisplay()");
                setUpVirtualDisplay();
            } else {
                Log.i(TAG, "exec startShot()/setUpVirtualDisplay()/setUpMediaProjection()");
                setUpMediaProjection();
                setUpVirtualDisplay();
            }
            mFloatingView.setVisibility(View.GONE);
            startToShot();
         /* try {  Display display = mWindowManager.getDefaultDisplay();
            final DisplayMetrics metrics = new DisplayMetrics();
            display.getMetrics(metrics);
            Point size = new Point();
            display.getRealSize(size);
            final int mWidth = size.x;
            final int mHeight = size.y;
            int mDensity = metrics.densityDpi;

            final ImageReader mImageReader = ImageReader.newInstance(mWidth, mHeight, PixelFormat.RGBA_8888, 2);

            final Handler handler = new Handler();
//            MediaProjectionManager mProjectionManager = (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);
//             int resultCode = intent.getIntExtra(EXTRA_RESULT_CODE, 0);
//            MediaProjection mProjection=mProjectionManager.getMediaProjection(RESULT_OK,intent);
//            int flags = DisplayManager.VIRTUAL_DISPLAY_FLAG_OWN_CONTENT_ONLY | DisplayManager.VIRTUAL_DISPLAY_FLAG_PUBLIC;
            int flags=DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR;
//            mProjection=(MediaProjection) mProjectionManager.getMediaProjection(RESULT_OK,intent);
            mResultData = ((Protection) getApplication()).getIntent();
            mResultCode = ((Protection) getApplication()).getResult();
            Log.i(TAG, "mResultData=" + mResultData);
            Log.i(TAG, "mResultCode=" + mResultCode);
            mProjectionManager = ((Protection) getApplication()).getMediaProjectionManager();
            mProjection = mProjectionManager.getMediaProjection(mResultCode, mResultData);
            mVirtualDisplay=mProjection.createVirtualDisplay("screen-mirror", mWidth, mHeight, mDensity, flags, mImageReader.getSurface(), null, handler);
            mFloatingView.setVisibility(View.GONE);
            mImageReader.setOnImageAvailableListener(new ImageReader.OnImageAvailableListener() {
                @Override
                public void onImageAvailable(ImageReader reader) {
                    reader.setOnImageAvailableListener(null, handler);
                    Image image = reader.acquireLatestImage();

                    final Image.Plane[] planes = image.getPlanes();
                    final ByteBuffer buffer = planes[0].getBuffer();

                    int pixelStride = planes[0].getPixelStride();
                    int rowStride = planes[0].getRowStride();
                    int rowPadding = rowStride - pixelStride * metrics.widthPixels;
                    // create bitmap
                    Bitmap bmp = Bitmap.createBitmap(metrics.widthPixels + (int) ((float) rowPadding / (float) pixelStride), metrics.heightPixels, Bitmap.Config.ARGB_8888);
                    bmp.copyPixelsFromBuffer(buffer);

                    image.close();
                    reader.close();

                    Bitmap realSizeBitmap = Bitmap.createBitmap(bmp, 0, 0, metrics.widthPixels, bmp.getHeight());
                    bmp.recycle();

                    storeImage(realSizeBitmap);
                    *//* do something with [realSizeBitmap] *//*
                }
            }, handler);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Try Again", Toast.LENGTH_SHORT).show();
        }*/
    }

    public interface OnFabClick {
        void onClose();
    }

    private void fadAniamtion() {
    setAlphaAnimation(fabFileShare);
    setAlphaAnimation(fabVoice);
    setAlphaAnimation(fabSearch);
    setAlphaAnimation(fabClose);
    flaotingParams.flags=WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
    mWindowManager.updateViewLayout(mFloatingView,flaotingParams);
    }
    public static void setAlphaAnimation(View v) {
        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(v, "alpha",  1f, .8f);
        fadeOut.setDuration(100);
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(v, "alpha", .8f, 1f);
        fadeIn.setDuration(100);

        final AnimatorSet mAnimationSet = new AnimatorSet();

        mAnimationSet.play(fadeIn).after(fadeOut);

        mAnimationSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
//                mAnimationSet.start();
            }
        });
        mAnimationSet.start();
    }
    public Bitmap takescreenshotOfView(View view) {
        View v = view.getRootView();
        v.setDrawingCacheEnabled(true);
        Bitmap b = v.getDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(v.getWidth(),
                v.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
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
            fos.close();
        } catch (FileNotFoundException e) {
            Log.d("TAG", "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d("TAG", "Error accessing file: " + e.getMessage());
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            File f1 = new File("file://" + pictureFile.getAbsolutePath());
            Uri contentUri = Uri.fromFile(f1);
            mediaScanIntent.setData(contentUri);
            sendBroadcast(mediaScanIntent);
        } else {
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://"+pictureFile.getAbsolutePath())));
        }
        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(pictureFile)));
        mFloatingView.setVisibility(View.VISIBLE);
        Toast.makeText(this, "Save Screenshot into gallery", Toast.LENGTH_SHORT).show();
    }
    private  File getOutputMediaFile(){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                + "/ProtectionScreenShot");

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
        String mImageName="ic_ScreenShot"+ timeStamp +".jpg";
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);
        return mediaFile;
    }
    private void startForegroundService() {
        Log.d("TAG_FOREGROUND_SERVICE", "Start foreground service.");
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
        Log.d("TAG_FOREGROUND_SERVICE", "Stop foreground service.");

        // Stop foreground service and remove the notification.
        stopForeground(true);

        // Stop the foreground service.
        stopSelf();
    }

    private void tearDownMediaProjection() {
        if (mProjection != null) {
            mProjection.stop();
            mProjection = null;
        }
        Log.i(TAG, "mMediaProjection undefined");
    }

    private void stopScreenCapture() {
        if (mVirtualDisplay == null) {
            return;
        }
        mVirtualDisplay.release();
        mVirtualDisplay = null;
        Log.i(TAG, "virtual display stopped");
    }
    private void createVirtualEnvironment() {
        windowWidth = getScreenWidth(this);
        windowHeight = getScreenHeight(this);
        mScreenDensity = getScreenDensity(this);
        mProjectionManager = (MediaProjectionManager) getApplication().getSystemService(Context.MEDIA_PROJECTION_SERVICE);
    }
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }
    /**
     * 获得屏幕宽度
     * @param context
     * @return
     */
    public  int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }
    public  int getScreenDensity(Context context) {
        return (int)context.getResources().getDisplayMetrics().density;
    }
}