package com.xueqin.contacts.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

/**
 * a Circle image view implementation
 */
public class AvatarView extends AppCompatImageView {

    private Path mClipPath = new Path();
    private RectF mTmpRect = new RectF();

    public AvatarView(Context context) {
        this(context, null);
    }

    public AvatarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AvatarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        final int w = getWidth(), h = getHeight();
        final int d = Math.min(w, h);
        final int halfDiff = Math.abs(w-h)/2;
        // clip path
        if (d > 0) {
            if (w > h) {
                mTmpRect.set(halfDiff, 0, h+halfDiff, h);
            } else {
                mTmpRect.set(0, halfDiff, w, w+halfDiff);
            }
            mClipPath.reset();
            mClipPath.addRoundRect(mTmpRect, d/2, d/2, Path.Direction.CW);
            canvas.clipPath(mClipPath);
        }
        // draw content
        super.onDraw(canvas);
        canvas.restore();
    }
}
