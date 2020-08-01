package com.example.protectionapp.hoverwindow;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import io.mattcarroll.hover.Content;

public class MyContent implements Content {
    Context context;
    String screen;

    public MyContent(Context context, String screen) {
        this.context = context;
        this.screen = screen;
    }

    @NonNull
    @Override
    public View getView() {
        return null;
    }

    @Override
    public boolean isFullscreen() {
        return false;
    }

    @Override
    public void onShown() {

    }

    @Override
    public void onHidden() {

    }
}
