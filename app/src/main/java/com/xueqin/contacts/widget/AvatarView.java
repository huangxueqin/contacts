package com.xueqin.contacts.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

import com.xueqin.contacts.R;
import com.xueqin.contacts.util.ScreenUtils;

public class AvatarView extends AppCompatImageView {

    private static final int DEFAULT_STROKE_WIDTH_DP = 5;
    private static final int DEFAULT_STROKE_COLOR = 0xff00b0ff;

    private int mStrokeWidth;
    private int mStrokeColor;
    private Paint mStrokePaint;

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
        init(context, attrs);
    }

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        invalidate();
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.AvatarView);
        mStrokeColor = ta.getColor(R.styleable.AvatarView_stroke_color, DEFAULT_STROKE_COLOR);
        mStrokeWidth = ta.getDimensionPixelSize(R.styleable.AvatarView_stroke_width,
                ScreenUtils.dp2px(context, DEFAULT_STROKE_WIDTH_DP));
        ta.recycle();

        // init stroke paint
        mStrokePaint = new Paint();
        mStrokePaint.setColor(mStrokeColor);
        mStrokePaint.setStyle(Paint.Style.STROKE);
        mStrokePaint.setStrokeWidth(mStrokeWidth);
        mStrokePaint.setAntiAlias(true);
    }

    public void setStrokeWidth(int strokeWidth) {
        if (mStrokeWidth != strokeWidth) {
            mStrokeWidth = strokeWidth;
            invalidate();
        }
    }

    public void setStrokeColor(int color) {
        if (mStrokeColor != color) {
            mStrokeColor = color;
            invalidate();
        }
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
        // draw stroke
        if (d > mStrokeWidth && isSelected()) {
            canvas.drawCircle(w/2, h/2, (d-mStrokeWidth)/2+1, mStrokePaint);
        }
    }
}
