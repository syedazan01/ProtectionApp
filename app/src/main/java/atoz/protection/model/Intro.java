package atoz.protection.model;


import atoz.protection.R;


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
