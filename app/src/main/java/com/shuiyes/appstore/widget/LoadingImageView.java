package com.shuiyes.appstore.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;

public class LoadingImageView extends android.support.v7.widget.AppCompatImageView {

    public LoadingImageView(Context context) {
        super(context);
    }

    public LoadingImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LoadingImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private RotateAnimation rotate;

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);

        if (visibility == View.VISIBLE) {
            if (rotate == null) {
                rotate = new RotateAnimation(0f, 359f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                rotate.setInterpolator(new android.view.animation.LinearInterpolator());
                rotate.setDuration(1500);
                rotate.setRepeatCount(Animation.INFINITE);
                rotate.setFillAfter(true);
            }

            this.clearAnimation();
            this.startAnimation(rotate);
        } else if (visibility == View.GONE) {
            this.clearAnimation();
        }
    }
}
