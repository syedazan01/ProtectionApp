package com.example.protectionapp.model;


import com.example.protectionapp.R;


public enum Intro {
    ONE(R.drawable.hiddencam_slide),
    TWO(R.drawable.recording_slide),
    THREE(R.drawable.chache_slide),
    FOUR(R.drawable.filesshare_slide);

    private int mLayoutResId;

    Intro(int layoutResId) {
        mLayoutResId = layoutResId;
    }

    public int getLayoutResId() {
        return mLayoutResId;
    }
}
