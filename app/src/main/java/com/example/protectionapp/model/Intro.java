package com.example.protectionapp.model;


import com.example.protectionapp.R;


public enum Intro {
    ONE(R.drawable.intro_screen),
    TWO(R.drawable.intro_screen_two);

    private int mLayoutResId;

    Intro(int layoutResId) {
        mLayoutResId = layoutResId;
    }

    public int getLayoutResId() {
        return mLayoutResId;
    }
}
