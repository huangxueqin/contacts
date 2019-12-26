package com.xueqin.contacts.ui.helper;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;

/**
 * set the avatar item which is closest to center of the list highlighted in time
 * for performance consideration, we only change the high lighted item when list is settled
 * TODO: use a draw over ItemDecorator maybe a better idea
 */
public class AvatarHighlighter {

    private static final String TAG = "AvatarHighlighter";

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

    static void performHighlightChange(RecyclerView.LayoutManager lm, OrientationHelper helper) {
        final int childCount = lm.getChildCount();
        if (childCount == -1) {
            return;
        }
        int absClosestToCenter = Integer.MAX_VALUE;
        View closetToCenterChild = null;
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
            // set select state to false
            child.setSelected(false);
        }
        // high light closest child view
        if (closetToCenterChild != null) {
            closetToCenterChild.setSelected(true);
        }
    }

    private static int distanceToCenter(@NonNull View targetView, OrientationHelper helper) {
        final int childCenter = helper.getDecoratedStart(targetView)
                + (helper.getDecoratedMeasurement(targetView) / 2);
        final int containerCenter = helper.getStartAfterPadding() + helper.getTotalSpace() / 2;
        return childCenter - containerCenter;
    }
}
