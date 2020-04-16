package com.shuiyes.appstore.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;

import com.shuiyes.appstore.R;

public class ProgressButton extends android.support.v7.widget.AppCompatButton {

    public ProgressButton(Context context) {
        super(context);
    }

    public ProgressButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private int mProgressColor = Color.parseColor("#4499cc00");
    private int mProgressStroke = getContext().getResources().getDimensionPixelSize(R.dimen.rect_btn_stroke);

    public ProgressButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        Object tag = this.getTag();
        if (tag != null) {
            String text = (String) tag;
            Paint paint = this.getPaint();
            int color = paint.getColor();
            paint.setColor(mProgressColor);
            float percent = Float.parseFloat(text) / 100;


            int left = this.getPaddingLeft() + mProgressStroke;
            int right = this.getPaddingRight() + mProgressStroke;
            int top = this.getPaddingTop() + mProgressStroke;
            int bottom = this.getPaddingBottom() + mProgressStroke;

//            canvas.drawRoundRect(left, top, (this.getMeasuredWidth() - left - right) * percent + left, this.getMeasuredHeight() - top, 10, 10, paint);
            canvas.drawRect(left, top, (this.getMeasuredWidth() - left - right) * percent + left, this.getMeasuredHeight() - bottom, paint);
            paint.setColor(color);
        }

        super.onDraw(canvas);
    }
}
