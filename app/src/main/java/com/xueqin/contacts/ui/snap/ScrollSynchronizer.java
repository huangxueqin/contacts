package com.xueqin.contacts.ui.snap;

import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ScrollSynchronizer {

    private static final String TAG = "ScrollSynchronizer";

    @SuppressWarnings("all")
    public static ScrollSynchronizer syncContactScroll(@NonNull RecyclerView avatarList,
                                                       @NonNull RecyclerView introductionList) {
        ScrollSynchronizer helper = new ScrollSynchronizer();
        helper.addRecyclerView(avatarList, CENTER_ALIGN);
        helper.addRecyclerView(introductionList, START_ALIGN);
        return helper;
    }

    private static final int CENTER_ALIGN = 0;
    private static final int START_ALIGN = 1;

    private int mPosition = 0;
    private float mPositionOffset = 0f;

    private OnPositionChangeListener mPositionChangeListener;

    private List<LinearScroller> mScrollerList = new ArrayList<>();
    private LinearScroller mActiveScroller;

    private ScrollSynchronizer() {
        // do nothing
    }

    public void registerPositionChangeListener(OnPositionChangeListener li) {
        mPositionChangeListener = li;
    }

    public void setCurrentActiveScroller(RecyclerView recyclerView, int position, boolean smooth) {
        for (LinearScroller scroller : mScrollerList) {
            if (scroller.layoutManager == recyclerView.getLayoutManager()) {
                mActiveScroller = scroller;
                break;
            }
        }
        if (mActiveScroller != null) {
            mActiveScroller.scrollToPosition(recyclerView, position, smooth);
        }
    }

    private void addRecyclerView(RecyclerView recyclerView, int alignType) {
        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            LinearScroller scroller = new LinearScroller((LinearLayoutManager) (recyclerView.getLayoutManager()), alignType);
            recyclerView.addOnScrollListener(scroller);
            mScrollerList.add(scroller);
        }
    }

    private void onScrolled() {
        if (mActiveScroller == null) {
            return;
        }
        int lastPosition = mPosition;
        onScrolled(mActiveScroller.alignType, mActiveScroller.layoutManager, mActiveScroller.orientationHelper);
        for (LinearScroller scroller : mScrollerList) {
            if (scroller != mActiveScroller) {
                scroller.scrollToPositionWithOffset(mPosition, mPositionOffset);
            }
        }
        if (lastPosition != mPosition) {
            if (mPositionChangeListener != null) {
                mPositionChangeListener.onPositionChange(lastPosition, mPosition);
            }
        }
    }

    private void onScrolled(int alignType, LinearLayoutManager lm, OrientationHelper helper) {
        final int childCount = lm.getChildCount();
        if (childCount == 0) {
            return;
        }
        // find current position
        View targetChild = null;
        int closestDistance = Integer.MAX_VALUE;
        for (int i = 0; i < childCount; i++) {
            final View child = lm.getChildAt(i);
            if (child == null) {
                continue;
            }
            final int distance;
            if (alignType == CENTER_ALIGN) {
                distance = distanceToCenter(lm, child, helper);
                Log.d(TAG, "distance to center = " + distance);
            } else {
                distance = distanceToStart(child, helper);
                Log.d(TAG, "distance to start = " + distance);
            }
            if (Math.abs(closestDistance) > Math.abs(distance)) {
                closestDistance = distance;
                targetChild = child;
            }
        }
        if (targetChild != null) {
            mPosition = lm.getPosition(targetChild);
            mPositionOffset = closestDistance / (float) helper.getDecoratedMeasurement(targetChild);
            Log.d(TAG, "childHeight = " + targetChild.getHeight() + ", childWidth = " + targetChild.getWidth());
            Log.d(TAG, "mPosition = " + mPosition + ", mPositionOffset = " + mPositionOffset);
        }
    }

    private int distanceToStart(@NonNull View targetView, @NonNull OrientationHelper helper) {
        return helper.getDecoratedStart(targetView) - helper.getStartAfterPadding();
    }

    private int distanceToCenter(@NonNull RecyclerView.LayoutManager layoutManager,
                                 @NonNull View targetView, OrientationHelper helper) {
        final int childCenter = helper.getDecoratedStart(targetView)
                + (helper.getDecoratedMeasurement(targetView) / 2);
        final int containerCenter = helper.getStartAfterPadding() + helper.getTotalSpace() / 2;
        return childCenter - containerCenter;
    }

    private class LinearScroller extends RecyclerView.OnScrollListener {

        int alignType;
        LinearLayoutManager layoutManager;
        OrientationHelper orientationHelper;

        LinearScroller(@NonNull LinearLayoutManager layoutManager, int alignType) {
            this.alignType = alignType;
            this.layoutManager = layoutManager;
            if (layoutManager.canScrollHorizontally()) {
                orientationHelper = OrientationHelper.createHorizontalHelper(layoutManager);
            } else {
                orientationHelper = OrientationHelper.createVerticalHelper(layoutManager);
            }
        }

        void scrollToPositionWithOffset(int position, float offsetPercent) {
            layoutManager.scrollToPositionWithOffset(position, computePositionOffset(position, offsetPercent));
        }

        void scrollToPosition(RecyclerView recyclerView, int position, boolean smooth) {
            recyclerView.smoothScrollToPosition(position);
        }

        private int computePositionOffset(int position, float offsetPercent) {
            int viewSize = 0;
            View view = layoutManager.findViewByPosition(position);
            if (view != null) {
                viewSize = orientationHelper.getDecoratedMeasurement(view);
            }
            if (alignType == START_ALIGN) {
                return (int) (viewSize * offsetPercent);
            } else {
                return (int) ((orientationHelper.getTotalSpace() - viewSize) / 2f + viewSize * offsetPercent);
            }
        }

        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
            if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                mActiveScroller = this;
            }
        }

        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            if (mActiveScroller == this) {
                ScrollSynchronizer.this.onScrolled();
            }
        }
    }

    public interface OnPositionChangeListener {

        void onPositionChange(int lastPosition, int currentPosition);

    }
}
