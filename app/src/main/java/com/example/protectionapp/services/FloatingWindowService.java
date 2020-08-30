package com.example.protectionapp.services;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Service;
import android.content.Intent;
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
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.protectionapp.Protection;
import com.example.protectionapp.R;
import com.example.protectionapp.activites.CallRecorder;
import com.example.protectionapp.activites.CameraDetector;
import com.example.protectionapp.activites.FileShare;
import com.example.protectionapp.fragments.UtilityFeaturesFragment;
import com.example.protectionapp.utils.AppConstant;
import com.example.protectionapp.utils.PrefManager;
import com.example.protectionapp.utils.Utils;
import com.example.protectionapp.utils.views.FloatView;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.example.protectionapp.fragments.UtilityFeaturesFragment.onFabClick;
import static com.example.protectionapp.utils.AppConstant.ISBLUELIGHT;

public class FloatingWindowService extends Service implements View.OnClickListener {
    public static final String STOP_LAUNCHER_WIDGET = "action.stopservice";
    public static final String BLUE_LIGHT_FILTER = "action.bluelight";
    private WindowManager mWindowManager,blueLightWindowManager;
    private int bluelightFilterCode=Color.parseColor("#4DF7E6B4");
    private View mFloatingView,blue_filter_container;
    private FloatingActionButton collapsed_iv,fabFileShare,fabVoice,fabSearch,fabScreenShot,fabClose;
    public FloatingWindowService() {
    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

            setTheme(R.style.AppTheme_Base_Light);
        //Inflate the floating view layout we created
        blue_filter_container = LayoutInflater.from(this).inflate(R.layout.blue_light_filter, null);
        if(PrefManager.getBoolean(ISBLUELIGHT))
            blue_filter_container.setBackgroundColor(bluelightFilterCode);
        mFloatingView = LayoutInflater.from(this).inflate(R.layout.layout_floating_widget, null);
        collapsed_iv=mFloatingView.findViewById(R.id.collapsed_iv);
        fabFileShare=mFloatingView.findViewById(R.id.fabFileShare);
        fabVoice=mFloatingView.findViewById(R.id.fabVoice);
        fabSearch=mFloatingView.findViewById(R.id.fabSearch);
        fabScreenShot=mFloatingView.findViewById(R.id.fabScreenShot);
        fabClose=mFloatingView.findViewById(R.id.fabClose);

        fabFileShare.setOnClickListener(this);
        fabVoice.setOnClickListener(this);
        fabSearch.setOnClickListener(this);
        fabScreenShot.setOnClickListener(this);
        fabClose.setOnClickListener(this);
        //Add the view to the window.
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
                        ? WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY :
                        WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        //Specify the view position
        params.gravity = Gravity.TOP | Gravity.START;        //Initially view will be added to top-left corner
        params.x = 0;
        params.y = 100;


        //Add the view to the blueLight window.
        final WindowManager.LayoutParams blueLightparams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
                        ? WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY :
                        WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                PixelFormat.TRANSLUCENT);

        //Specify the view position
        blueLightparams.gravity = Gravity.TOP | Gravity.START;        //Initially view will be added to top-left corner
        blueLightparams.x = 0;
        blueLightparams.y = 0;
        //Add the view to the window
        blueLightWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        blueLightWindowManager.addView(blue_filter_container, blueLightparams);

        mWindowManager.addView(mFloatingView, params);

        //The root element of the collapsed view layout
//        final View collapsedView = mFloatingView.findViewById(R.id.collapse_view);
        //The root element of the expanded view layout
//        final View expandedView = mFloatingView.findViewById(R.id.expanded_container);


        //Set the close button
        /*ImageView closeButtonCollapsed = (ImageView) mFloatingView.findViewById(R.id.close_btn);
        closeButtonCollapsed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //close the service and remove the from from the window
                stopSelf();
            }
        });*/

        //Set the view while floating view is expanded.
        //Set the play button.
        /*ImageView playButton = (ImageView) mFloatingView.findViewById(R.id.play_btn);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(FloatingViewService.this, "Playing the song.", Toast.LENGTH_LONG).show();
            }
        });

        //Set the next button.
        ImageView nextButton = (ImageView) mFloatingView.findViewById(R.id.next_btn);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(FloatingViewService.this, "Playing next song.", Toast.LENGTH_LONG).show();
            }
        });

        //Set the pause button.
        ImageView prevButton = (ImageView) mFloatingView.findViewById(R.id.prev_btn);
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(FloatingViewService.this, "Playing previous song.", Toast.LENGTH_LONG).show();
            }
        });

        //Set the close button
        ImageView closeButton = (ImageView) mFloatingView.findViewById(R.id.close_button);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                collapsedView.setVisibility(View.VISIBLE);
                expandedView.setVisibility(View.GONE);
            }
        });

        //Open the application on thi button click
        ImageView openButton = (ImageView) mFloatingView.findViewById(R.id.open_button);
        openButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Open the application  click.
                Intent intent = new Intent(FloatingViewService.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

                //close the service and remove view from the view hierarchy
                stopSelf();
            }
        });*/

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
                        initialX = params.x;
                        initialY = params.y;

                        //get the touch location
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        return true;
                    case MotionEvent.ACTION_UP:
                        int Xdiff = (int) (event.getRawX() - initialTouchX);
                        int Ydiff = (int) (event.getRawY() - initialTouchY);

                        //The check for Xdiff <10 && YDiff< 10 because sometime elements moves a little while clicking.
                        //So that is click event.
                        if (v==collapsed_iv && Xdiff < 10 && Ydiff < 10) {
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
                        params.x = initialX + (int) (event.getRawX() - initialTouchX);
                        params.y = initialY + (int) (event.getRawY() - initialTouchY);

                        //Update the layout with new X & Y coordinate
                        mWindowManager.updateViewLayout(mFloatingView, params);
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
        if (mFloatingView != null) mWindowManager.removeView(mFloatingView);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getAction() != null && intent.getAction().equals(STOP_LAUNCHER_WIDGET))
            stopSelf();
        else if(intent.getAction()!=null && intent.getAction().equals(BLUE_LIGHT_FILTER))
        {
            if(PrefManager.getBoolean(ISBLUELIGHT))
                blue_filter_container.setBackgroundColor(bluelightFilterCode);
            else
                blue_filter_container.setBackgroundColor(0);
        }

        return START_STICKY;
    }

    @Override
    public void onClick(View view) {
        if(view==fabFileShare)
        {
            startActivity(new Intent(Protection.getInstance(),FileShare.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
        else if(view==fabVoice)
        {
            startActivity(new Intent(Protection.getInstance(),CallRecorder.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
        else if(view==fabScreenShot)
        {
            ViewTreeObserver vto = mFloatingView.getViewTreeObserver();
            vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    storeImage(takescreenshotOfView(mFloatingView));
                }
            });

        }
        else if(view==fabClose)
        {
            PrefManager.putBoolean(AppConstant.OVERLAY,false);
            if (onFabClick!=null) {
                onFabClick.onClose();
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