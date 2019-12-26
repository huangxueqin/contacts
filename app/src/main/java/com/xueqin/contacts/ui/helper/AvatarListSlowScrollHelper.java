package com.xueqin.contacts.ui.helper;

import android.util.DisplayMetrics;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

/**
 * {@link androidx.recyclerview.widget.RecyclerView#smoothScrollToPosition(int)} scrolls too fast
 * so let's make it slower
 */
public class AvatarListSlowScrollHelper {

    private static final float MILLISECONDS_PER_INCH = 100f;

    public static void smoothScrollToPosition(@NonNull RecyclerView recyclerView, int position, final int duration) {
        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            LinearLayoutManager lm = (LinearLayoutManager) recyclerView.getLayoutManager();
            RecyclerView.SmoothScroller scroller = new LinearSmoothScroller(recyclerView.getContext()) {
                @Override
                protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                    return MILLISECONDS_PER_INCH / displayMetrics.densityDpi;
                }
            };
            scroller.setTargetPosition(position);
            lm.startSmoothScroll(scroller);
        }
    }

}
