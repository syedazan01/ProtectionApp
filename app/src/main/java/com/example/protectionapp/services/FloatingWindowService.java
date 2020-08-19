package com.example.protectionapp.services;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.example.protectionapp.R;
import com.example.protectionapp.activites.CallRecorder;
import com.example.protectionapp.activites.CameraDetector;
import com.example.protectionapp.activites.FileShare;
import com.example.protectionapp.fragments.UtilityFeaturesFragment;
import com.example.protectionapp.utils.AppConstant;
import com.example.protectionapp.utils.PrefManager;
import com.github.clans.fab.FloatingActionButton;

import static com.example.protectionapp.utils.AppConstant.ISNIGHTMODE;

public class FloatingWindowService extends Service {
    private static final float CLICK_DRAG_TOLERANCE = 100;
    private WindowManager mWindowManager;
    private View mFloatingWidget;
    private FloatingActionButton fabCameraDetector, fabVoiceRecorder, fabFileShare, fabClose;

    @Override
    public void onCreate() {
        super.onCreate();
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        if (PrefManager.getBoolean(ISNIGHTMODE))
            setTheme(R.style.AppTheme_Base_Night);
        else
            setTheme(R.style.AppTheme_Base_Light);

        mFloatingWidget = LayoutInflater.from(this).inflate(R.layout.layout_floating_widget, null);
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, Build.VERSION.SDK_INT > Build.VERSION_CODES.O
                ? WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
                : WindowManager.LayoutParams.TYPE_SYSTEM_ERROR,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.CENTER | Gravity.END;
        params.x = 0;
        params.y = 100;

        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mWindowManager.addView(mFloatingWidget, params);
        final View collapsedView = mFloatingWidget.findViewById(R.id.collapse_view);
        final View expandedView = mFloatingWidget.findViewById(R.id.expanded_container);
        fabVoiceRecorder = mFloatingWidget.findViewById(R.id.fabVoiceRecorder);
        fabCameraDetector = mFloatingWidget.findViewById(R.id.fabCameraDetector);
        fabFileShare = mFloatingWidget.findViewById(R.id.fabFileShare);
        fabClose = mFloatingWidget.findViewById(R.id.closeMenuItem);
        fabVoiceRecorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(FloatingWindowService.this, CallRecorder.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });
        fabCameraDetector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(FloatingWindowService.this, CameraDetector.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });
        fabFileShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(FloatingWindowService.this, FileShare.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });
        fabClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PrefManager.putBoolean(AppConstant.OVERLAY, false);
                if (UtilityFeaturesFragment.onFabClick != null) {
                    UtilityFeaturesFragment.onFabClick.onClose();
                }
                stopSelf();
            }
        });
        ImageView closeButtonCollapsed = mFloatingWidget.findViewById(R.id.close_btn);
        closeButtonCollapsed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopSelf();
            }
        });

        ImageView closeButton = mFloatingWidget.findViewById(R.id.close_button);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                collapsedView.setVisibility(View.VISIBLE);
                expandedView.setVisibility(View.GONE);
            }
        });

        mFloatingWidget.findViewById(R.id.rootMenu).setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;

            @Override
            public boolean onTouch(View view, MotionEvent event) {
               /* switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initialX = params.x;
                        initialY = params.y;
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        return true;
                    case MotionEvent.ACTION_UP:
                        int Xdiff = (int) (event.getRawX() - initialTouchX);
                        int Ydiff = (int) (event.getRawY() - initialTouchY);
                        if (Xdiff < 10 && Ydiff < 10) {

                        }
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        params.x = initialX + (int) (event.getRawX() - initialTouchX);
                        params.y = initialY + (int) (event.getRawY() - initialTouchY);
                        mWindowManager.updateViewLayout(mFloatingWidget, params);
                        return true;
                }
                return false;
            }*/
                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:

                        initialTouchX = view.getX() - event.getRawX();
                        initialTouchY = view.getY() - event.getRawY();
                        break;

                    case MotionEvent.ACTION_MOVE:

                        view.animate()
                                .x(event.getRawX() + initialTouchX)
                                .y(event.getRawY() + initialTouchY)
                                .setDuration(0)
                                .start();
                        break;
                    default:
                        return false;
                }
                return true;
            }
        });
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mFloatingWidget != null) mWindowManager.removeView(mFloatingWidget);
    }

    public interface OnFabClick {
        void onClose();
    }
}
