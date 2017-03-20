package com.mydeliveries.toastit.views;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;

import com.mydeliveries.toastit.R;

public class LoadingView extends LinearLayout {

    private Handler mHandler;
    private boolean cancelled = false;
    private boolean isAnimating = false;
    private View bar1;
    private View bar2;
    private View bar3;
    private View bar4;
    private View bar5;
    private int loadPosition = 0;
    private Context context;

    public LoadingView(Context context) {
        super(context);
        this.context = context;
        setupLoadingView();
    }

    public LoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        setupLoadingView();
    }

    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {
            if (!cancelled) {
                displayLoadingPosition(loadPosition);

                loadPosition++;

                mHandler.postDelayed(mStatusChecker, 250);
            }

        }
    };


    public void setupLoadingView() {
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.loading_view, this);
        bar1 = findViewById(R.id.bar1);
        bar2 = findViewById(R.id.bar2);
        bar3 = findViewById(R.id.bar3);
        bar4 = findViewById(R.id.bar4);
        bar5 = findViewById(R.id.bar5);
        prepareForAnimation();
    }

    private void prepareForAnimation() {
        bar1.setBackgroundColor(getResources().getColor(R.color.textColour));
        resizeBar(bar1);
        bar2.setBackgroundColor(getResources().getColor(R.color.ColorPrimary));
        resizeBar(bar2);
        bar3.setBackgroundColor(getResources().getColor(R.color.textColour));
        resizeBar(bar3);
        bar4.setBackgroundColor(getResources().getColor(R.color.ColorPrimary));
        resizeBar(bar4);
        bar5.setBackgroundColor(getResources().getColor(R.color.textColour));
        resizeBar(bar5);
    }

    private void displayLoadingPosition(int loadPosition) {
        int emphasizedViewPos = loadPosition % 7;

        switch (emphasizedViewPos) {
            case 0:
                animateBar(bar1);
                break;

            case 1:
                animateBar(bar2);
                break;

            case 2:
                animateBar(bar3);
                break;

            case 3:
                animateBar(bar4);
                break;

            case 4:
                animateBar(bar5);
                break;
            case 5:
                break;
            case 6:
                break;
        }
    }

    private void animateBar(final View bar) {

        ObjectAnimator cloudAnim = ObjectAnimator.ofPropertyValuesHolder(bar, PropertyValuesHolder.ofFloat("scaleY", 1.f));

        cloudAnim.setDuration(400);

        cloudAnim.setRepeatCount(1);
        cloudAnim.setRepeatMode(ValueAnimator.REVERSE);
        cloudAnim.setInterpolator(new LinearInterpolator());

        cloudAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                bar.invalidate();
                ((View) bar.getParent()).invalidate();
            }
        });
        cloudAnim.start();
    }

    private void resizeBar(View bar) {
        float scaleFactor = 0.5f;
        bar.setScaleY(scaleFactor);
    }

    public void stopAnimating() {
        isAnimating = false;
        cancelled = true;
        loadPosition = 0;
    }

    public void startAnimating() {
        if (!isAnimating) {
            cancelled = false;
            mHandler = new Handler();
            mHandler.postDelayed(mStatusChecker, 50);
            isAnimating = !isAnimating;
        }
    }
}
