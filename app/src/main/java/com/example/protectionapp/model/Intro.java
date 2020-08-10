package com.example.protectionapp.model;


import com.example.protectionapp.R;


public enum Intro {
    ONE(R.drawable.ic_intro_one),
    TWO(R.drawable.ic_intro_two),
    THREE(R.drawable.ic_intro_three),
    FOUR(R.drawable.ic_intro_four);

    private int mLayoutResId;

    Intro(int layoutResId) {
        mLayoutResId = layoutResId;
    }

    public int getLayoutResId() {
        return mLayoutResId;
    }
}
