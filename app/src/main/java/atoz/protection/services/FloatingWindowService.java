package atoz.protection.services;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.SearchManager;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import atoz.protection.Protection;
import atoz.protection.R;
import atoz.protection.activites.CallRecorder;
import atoz.protection.activites.FileShare;
import atoz.protection.receivers.BootCompleteReceiver;
import atoz.protection.utils.AppConstant;
import atoz.protection.utils.PrefManager;
import atoz.protection.utils.Utils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import atoz.protection.fragments.UtilityFeaturesFragment;

import static atoz.protection.utils.AppConstant.ISBLUELIGHT;

public class FloatingWindowService extends Service implements View.OnClickListener {
    public static final String LAUNCHER_WIDGET = "action.floating.launcher";
    private BootCompleteReceiver bootBroadCast;
    public static final String BLUE_LIGHT_FILTER = "action.bluelight";
    public static final String SCREEN_ON = "action.screenOn";
    WindowManager.LayoutParams flaotingParams, blueLightparams, patternLockParams;
    private int bluelightFilterCode = Color.parseColor("#4DF7E6B4");
    private WindowManager mWindowManager, blueLightWindowManager, patternLockWindowManager;
    private View mFloatingView, blue_filter_container, pattern_lock_container;
    private FloatingActionButton collapsed_iv, fabFileShare, fabVoice, fabSearch, fabScreenShot, fabClose;
    private EditText etSearchQuery;

    public FloatingWindowService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        IntentFilter filter2 = new IntentFilter(Intent.ACTION_SCREEN_OFF);
        bootBroadCast = new BootCompleteReceiver();
        registerReceiver(bootBroadCast, filter);
        setTheme(R.style.AppTheme_Base_Light);


        initPatterLockWidget();


    }

    private void initPatterLockWidget() {
        pattern_lock_container = LayoutInflater.from(this).inflate(R.layout.pattern_lock_view, null);
        //Add the view to the blueLight window.
        patternLockParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
                        ? WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY :
                        WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                PixelFormat.TRANSLUCENT);

        //Specify the view position
        patternLockParams.gravity = Gravity.TOP | Gravity.START;        //Initially view will be added to top-left corner
        patternLockParams.x = 0;
        patternLockParams.y = 0;
        //Add the view to the window
        patternLockWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        pattern_lock_container.setVisibility(View.GONE);
        patternLockWindowManager.addView(pattern_lock_container, patternLockParams);
        PatternLockView patternLockView = pattern_lock_container.findViewById(R.id.patterLockView);
        patternLockView.addPatternLockListener(new PatternLockViewListener() {
            @Override
            public void onStarted() {

            }

            @Override
            public void onProgress(List<PatternLockView.Dot> progressPattern) {

            }

            @Override
            public void onComplete(List<PatternLockView.Dot> pattern) {
//                patternLockView.setPattern(PatternLockView.PatternViewMode.CORRECT,pattern);
            }

            @Override
            public void onCleared() {

            }
        });
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

        mFloatingView = LayoutInflater.from(this).inflate(R.layout.layout_floating_widget, null);
        collapsed_iv = mFloatingView.findViewById(R.id.collapsed_iv);
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
                    mFloatingView.findViewById(R.id.expanded_container).setVisibility(View.GONE);
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
                                mFloatingView.findViewById(R.id.expanded_container).setVisibility(View.VISIBLE);
                            } else {
                                mFloatingView.findViewById(R.id.expanded_container).setVisibility(View.GONE);
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
        return mFloatingView == null || mFloatingView.findViewById(R.id.expanded_container).getVisibility() == View.GONE;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(bootBroadCast);
        if (mFloatingView != null) mWindowManager.removeView(mFloatingView);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getAction() != null && intent.getAction().equals(LAUNCHER_WIDGET)) {
            if (PrefManager.getBoolean(AppConstant.OVERLAY))
                initFloatingWidget();
            else
                stopSelf();
        } else if (intent.getAction() != null && intent.getAction().equals(BLUE_LIGHT_FILTER)) {

            if (PrefManager.getBoolean(ISBLUELIGHT)) {
                initBlueLightFilterWidget();
                blue_filter_container.setBackgroundColor(bluelightFilterCode);
            } else {
                if (blue_filter_container != null)
                    blue_filter_container.setBackgroundColor(0);
            }

        } else if (intent.getAction() != null && intent.getAction().equals(SCREEN_ON))
            pattern_lock_container.setVisibility(View.VISIBLE);
        return START_STICKY;
    }

    @Override
    public void onClick(View view) {
        if(view==fabFileShare)
        {
            startActivity(new Intent(Protection.getInstance(), FileShare.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
        else if(view==fabVoice)
        {
            startActivity(new Intent(Protection.getInstance(), CallRecorder.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
        else if(view==fabScreenShot)
        {
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
            PrefManager.putBoolean(AppConstant.OVERLAY,false);
            if (UtilityFeaturesFragment.onFabClick!=null) {
                UtilityFeaturesFragment.onFabClick.onClose();
            }
            stopSelf();
        }
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
        fadeOut.setDuration(200);
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
}