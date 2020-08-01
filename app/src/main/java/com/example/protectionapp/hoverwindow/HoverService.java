package com.example.protectionapp.hoverwindow;

import android.content.Intent;

import androidx.annotation.NonNull;

import io.mattcarroll.hover.HoverMenu;
import io.mattcarroll.hover.HoverView;
import io.mattcarroll.hover.window.HoverMenuService;

import static com.example.protectionapp.Protection.instance;

public class HoverService extends HoverMenuService {
    @Override
    protected void onHoverMenuLaunched(@NonNull Intent intent, @NonNull HoverView hoverView) {
        // Configure and start your HoverView.
        HoverMenu menu = new MyHoverMenu(instance);
        hoverView.addToWindow();
        hoverView.setMenu(menu);
        hoverView.collapse();
    }
}
