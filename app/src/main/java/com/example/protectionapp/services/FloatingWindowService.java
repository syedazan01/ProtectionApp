package com.example.protectionapp.services;

import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.example.protectionapp.R;
import com.example.protectionapp.utils.PrefManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import static com.example.protectionapp.utils.AppConstant.ISNIGHTMODE;

public class FloatingWindowService extends Service {
    private WindowManager windowManager;
    private LinearLayout linearLayout;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        linearLayout = new LinearLayout(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
//        linearLayout.setBackgroundColor(Color.argb(66, 255, 0, 0));
        linearLayout.setLayoutParams(layoutParams);
      /*  TextView textView = new TextView(this);
        textView.setText("This is a floating window");
        textView.setTextColor(Color.BLUE);
        textView.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        LinearLayout.LayoutParams layoutParamsText = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
// linearLayout.setBackgroundColor(Color.argb(66, 255, 0, 0));
        textView.setLayoutParams(layoutParamsText);
*/
  /*      ImageView icon = new ImageView(this); // Create an icon
        icon.setImageResource(R.drawable.login_logo);

        FloatingActionButton actionButton = new FloatingActionButton.Builder(mContext)
                .setContentView(icon)
                .build();
        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(mContext);
// repeat many times:
        ImageView itemIcon = new ImageView(this);
        itemIcon.setImageResource(R.drawable.login_logo);
        SubActionButton button1 = itemBuilder.setContentView(itemIcon).build();
        FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(mContext)
                .addSubActionView(button1)
//                // ...
                .attachTo(actionButton)
                .build();*/
        if (PrefManager.getBoolean(ISNIGHTMODE))
            setTheme(R.style.AppTheme_Base_Night);
        else
            setTheme(R.style.AppTheme_Base_Light);
        FloatingActionButton floatingActionButton = new FloatingActionButton(this);
        floatingActionButton.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        floatingActionButton.setBackgroundColor(Color.WHITE);
        floatingActionButton.setImageResource(R.drawable.login_logo);
        linearLayout.addView(floatingActionButton);
//        actionMenu.open(true);
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(400, 150, Build.VERSION.SDK_INT > Build.VERSION_CODES.O
                ? WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
                : WindowManager.LayoutParams.TYPE_SYSTEM_ERROR,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        params.x = 0;
        params.y = 0;
        params.gravity = Gravity.END;
        windowManager.addView(linearLayout, params);

        linearLayout.setOnTouchListener(new View.OnTouchListener() {
            WindowManager.LayoutParams updatedParams = params;
            int x,y;
            float touchX,touchY;
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        x = updatedParams.x;
                        y = updatedParams.y;
                        touchX = motionEvent.getRawX();
                        touchY = motionEvent.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        updatedParams.x = (int) (x + (touchX - motionEvent.getRawX()));
                        updatedParams.y = (int) (y + (touchY - motionEvent.getRawY()));
                        windowManager.updateViewLayout(linearLayout, updatedParams);
                    default:
                        break;
                }

                return true;
            }
        })
        ;
    }

}
