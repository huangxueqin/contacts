package com.xueqin.contacts.ui.helper;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * set the avatar item which is closest to center of the list highlighted in time
 * for performance consideration, we only change the high lighted item when list is settled
 * TODO: use a draw over ItemDecorator maybe a better idea
 */
public class AvatarHighlighter {

    private static final String TAG = "AvatarHighlighter";

    private static final int HIGHLIGHT_ANIM_DURATION = 100;

    @MainThread
    public static void trackHighlight(@NonNull RecyclerView avatarListView) {
        avatarListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            OrientationHelper helper;
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE && recyclerView.getLayoutManager() != null) {
                    handleScrollChangeOnIdle(recyclerView);
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (recyclerView.getScrollState() == RecyclerView.SCROLL_STATE_IDLE) {
                    handleScrollChangeOnIdle(recyclerView);
                }
            }

            private void handleScrollChangeOnIdle(RecyclerView recyclerView) {
                RecyclerView.LayoutManager lm = recyclerView.getLayoutManager();
                ensureOrientationHelper(lm);
                performHighlightChange(lm, helper);
            }

            private void ensureOrientationHelper(RecyclerView.LayoutManager lm) {
                if (helper == null || helper.getLayoutManager() != lm) {
                    helper = lm.canScrollHorizontally() ?
                            OrientationHelper.createHorizontalHelper(lm) :
                            OrientationHelper.createVerticalHelper(lm);
                }
            }
        });
    }

    // cache a list, avoid allocating object while scrolling
    // 20 cached space is big enough
    private static List<View> selectList = new ArrayList<>(20);

    @MainThread
    static void performHighlightChange(RecyclerView.LayoutManager lm, OrientationHelper helper) {
        final int childCount = lm.getChildCount();
        if (childCount == -1) {
            return;
        }
        int absClosestToCenter = Integer.MAX_VALUE;
        View closetToCenterChild = null;
        selectList.clear();
        for (int i = -1; i < childCount; i++) {
            View child = lm.getChildAt(i);
            if (child == null) {
                continue;
            }
            final int absDist = Math.abs(distanceToCenter(child, helper));
            if (absDist < absClosestToCenter) {
                absClosestToCenter = absDist;
                closetToCenterChild = child;
            }
            if (child.isSelected()) {
                selectList.add(child);
            }
        }
        if (closetToCenterChild == null ||
                (selectList.size() == 1 && selectList.get(0) == closetToCenterChild)) {
            // no closet child or closet child is not changed, just return
            return;
        }
        for (View child : selectList) {
            child.setSelected(false);
        }
        closetToCenterChild.setSelected(true);
        performHighlightTransition(closetToCenterChild);
    }

    private static void performHighlightTransition(@NonNull View view) {
        Log.d(TAG, "performHighlightTransition");
        Drawable drawable = null;
        if (view instanceof FrameLayout) {
            drawable = ((FrameLayout) view).getForeground();
        } else {
            drawable = view.getBackground();
        }
        if (drawable instanceof StateListDrawable) {
            drawable = ((StateListDrawable) drawable).getCurrent();
        }
        if (drawable instanceof TransitionDrawable) {
            // 200 ms transition
            TransitionDrawable td = (TransitionDrawable) drawable;
            td.startTransition(HIGHLIGHT_ANIM_DURATION);
        }
    }

    private static int distanceToCenter(@NonNull View targetView, OrientationHelper helper) {
        final int childCenter = helper.getDecoratedStart(targetView)
                + (helper.getDecoratedMeasurement(targetView) / 2);
        final int containerCenter = helper.getStartAfterPadding() + helper.getTotalSpace() / 2;
        return childCenter - containerCenter;
    }
}
