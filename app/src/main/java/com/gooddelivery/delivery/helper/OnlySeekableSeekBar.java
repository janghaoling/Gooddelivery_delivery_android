package com.gooddelivery.delivery.helper;

/**
 * Created by santhosh@appoets.com on 16-10-2017.
 */
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.SeekBar;

public class OnlySeekableSeekBar extends SeekBar {

    public OnlySeekableSeekBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public OnlySeekableSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public OnlySeekableSeekBar(Context context) {
        super(context);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                final int width = getWidth();
                final int available = width - getPaddingLeft() - getPaddingRight();
                int x = (int) event.getX();
                float scale;
                float progress = 0;
                if (x < getPaddingLeft()) {
                    scale = 0.0f;
                } else if (x > width - getPaddingRight()) {
                    scale = 1.0f;
                } else {
                    scale = (float) (x - getPaddingLeft()) / (float) available;
                }
                final int max = getMax();
                progress += scale * max;
                if (progress < 0) {
                    progress = 0;
                } else if (progress > max) {
                    progress = max;
                }

                if (Math.abs(progress - getProgress()) < 10)
                    return super.onTouchEvent(event);
                else
                    return false;
            default:
                return super.onTouchEvent(event);
        }
    }
}
