package com.example.protectionapp.utils.views;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

public class ExpandCollapseAnimationHelper {

    private final static int DURATION = 300;

    ValueAnimator valueAnimator;

    ViewGroup contentLayout;

    public ExpandCollapseAnimationHelper(ViewGroup contentLayout) {
        this.contentLayout = contentLayout;

        init();
    }

    private void init() {
        contentLayout.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {

                contentLayout.getViewTreeObserver().removeOnPreDrawListener(this);
                contentLayout.setVisibility(View.GONE);

                final int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                final int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                contentLayout.measure(widthSpec, heightSpec);

                valueAnimator = slideAnimator(0, contentLayout.getHeight());
                return true;
            }
        });
    }

    public void collapse() {

        ValueAnimator mAnimator = slideAnimator(contentLayout.getHeight(), 0);

        mAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                contentLayout.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });

        mAnimator.start();
    }

    public void expand() {
        contentLayout.setVisibility(View.VISIBLE);
        valueAnimator.start();
    }

    public void toggle() {
        if (contentLayout.isShown()) {
            collapse();
        } else {
            expand();
        }
    }

    private ValueAnimator slideAnimator(int start, int end) {

        ValueAnimator animator = ValueAnimator.ofInt(start, end);
        animator.setDuration(DURATION);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int value = (Integer) valueAnimator.getAnimatedValue();

                ViewGroup.LayoutParams layoutParams = contentLayout.getLayoutParams();
                layoutParams.height = value;

                contentLayout.setLayoutParams(layoutParams);
                contentLayout.invalidate();
            }
        });

        return animator;
    }
}
