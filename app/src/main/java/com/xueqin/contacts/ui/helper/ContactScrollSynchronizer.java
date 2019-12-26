package com.xueqin.contacts.ui.helper;

import android.content.Context;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;

/**
 * The only purpose of this class is to **synchronize** the scrolling of Avatar and Introduction List
 */
public class ContactScrollSynchronizer {

    private static final String TAG = "ScrollSynchronizer";

    public static ContactScrollSynchronizer syncContactScroll(@NonNull Context context,
                                                              @NonNull RecyclerView avatarList,
                                                              @NonNull RecyclerView introductionList) {
        if (!(avatarList.getLayoutManager() instanceof LinearLayoutManager &&
                introductionList.getLayoutManager() instanceof LinearLayoutManager)) {
            throw new IllegalStateException("avatar list and introduction list must have LayoutManager set");
        }
        // create synchronizer instance
        ContactScrollSynchronizer synchronizer = new ContactScrollSynchronizer(
                context,
                (LinearLayoutManager) avatarList.getLayoutManager(),
                (LinearLayoutManager) introductionList.getLayoutManager());
        // attach scroll listener
        avatarList.addOnScrollListener(synchronizer.mAvatarScrollHelper);
        introductionList.addOnScrollListener(synchronizer.mIntroductionScrollHelper);
        return synchronizer;
    }

    // this two parameter are used to record current scroll position
    private int mPosition = 0;
    private float mPositionOffsetPercent = 0f;

    @NonNull
    private Context mContext;
    @NonNull
    private LinearScrollHelper mAvatarScrollHelper;
    @NonNull
    private LinearScrollHelper mIntroductionScrollHelper;
    @Nullable
    private LinearScrollHelper mActiveScrollHelper;

    private ContactScrollSynchronizer(@NonNull Context context,
                                      @NonNull LinearLayoutManager avatarLm,
                                      @NonNull LinearLayoutManager introductionLm) {
        mContext = context.getApplicationContext();
        mAvatarScrollHelper = new LinearScrollHelper(avatarLm);
        mIntroductionScrollHelper = new LinearScrollHelper(introductionLm);
    }

    public void setAvatarListActive() {
        mActiveScrollHelper = mAvatarScrollHelper;
    }

    public void setIntroductionListActive() {
        mActiveScrollHelper = mIntroductionScrollHelper;
    }

    public void clearActiveList() {
        mActiveScrollHelper = null;
    }

    private void onActiveListScrolled(RecyclerView recyclerView, int dx, int dy) {
        if (mActiveScrollHelper == null ||
                mActiveScrollHelper.layoutManager != recyclerView.getLayoutManager()) {
            return;
        }
        Log.d(TAG, "active list scroll by dx = " + dx + ", dy = " + dy);
        if (mActiveScrollHelper == mAvatarScrollHelper) {
            onAvatarListActiveAndScrolled();
        } else if (mActiveScrollHelper == mIntroductionScrollHelper) {
            onIntroductionListActiveAndScrolled();
        }
    }

    private void onAvatarListActiveAndScrolled() {
        updateCurrentPosition(mAvatarScrollHelper.layoutManager,
                mAvatarScrollHelper.orientationHelper,
                ALIGN_CENTER);
        Log.d(TAG, "by avatar list, current position = " + mPosition + ", offset percent = " + mPositionOffsetPercent);
        // scroll introduction list to corresponding position
        mIntroductionScrollHelper.scrollToPositionWithOffset(mPosition, mPositionOffsetPercent);
    }

    private void onIntroductionListActiveAndScrolled() {
        updateCurrentPosition(mIntroductionScrollHelper.layoutManager,
                mIntroductionScrollHelper.orientationHelper,
                ALIGN_START);
        Log.d(TAG, "by introduction list, current position = " + mPosition + ", offset percent = " + mPositionOffsetPercent);
        // scroll avatar list to corresponding position
        mAvatarScrollHelper.scrollToPositionWithOffset(mPosition, mPositionOffsetPercent);

        /*
         * use {@link LinearLayoutManager#scrollToPosition(int)} does not trigger
         * {@link RecyclerView.OnScrollListener#onScrolled(RecyclerView, int, int)} callback
         * so we here send notify this situation add an extra interface to detect such situation
         */
        if (Math.abs(mPositionOffsetPercent) < 0.000001f) {
            AvatarHighlighter.performHighlightChange(mAvatarScrollHelper.layoutManager,
                    mAvatarScrollHelper.orientationHelper);
        }
    }

    private static final int ALIGN_CENTER = 0;
    private static final int ALIGN_START = 1;

    private void updateCurrentPosition(LinearLayoutManager lm, OrientationHelper helper, int align) {
        final int childCount = lm.getChildCount();
        if (childCount == 0) {
            return;
        }
        View closestChild = null;
        int closestDist = Integer.MAX_VALUE;
        for (int i = 0; i < childCount; i++) {
            final View child = lm.getChildAt(i);
            if (child == null) {
                continue;
            }
            final int dist = align == ALIGN_CENTER ?
                    distanceToCenter(child, helper) : distanceToStart(child, helper);
            if (Math.abs(dist) < Math.abs(closestDist)) {
                closestDist = dist;
                closestChild = child;
            }
        }
        if (closestChild != null) {
            mPosition = lm.getPosition(closestChild);
            mPositionOffsetPercent = closestDist / (float) helper.getDecoratedMeasurement(closestChild);
        }
    }

    private int distanceToStart(@NonNull View targetView, @NonNull OrientationHelper helper) {
        return helper.getDecoratedStart(targetView) - helper.getStartAfterPadding();
    }

    private int distanceToCenter(@NonNull View targetView, OrientationHelper helper) {
        final int childCenter = helper.getDecoratedStart(targetView)
                + (helper.getDecoratedMeasurement(targetView) / 2);
        final int containerCenter = helper.getStartAfterPadding() + helper.getTotalSpace() / 2;
        return childCenter - containerCenter;
    }

    private class LinearScrollHelper extends RecyclerView.OnScrollListener {

        LinearLayoutManager layoutManager;
        OrientationHelper orientationHelper;

        LinearScrollHelper(@NonNull LinearLayoutManager layoutManager) {
            this.layoutManager = layoutManager;
            if (layoutManager.canScrollHorizontally()) {
                orientationHelper = OrientationHelper.createHorizontalHelper(layoutManager);
            } else {
                orientationHelper = OrientationHelper.createVerticalHelper(layoutManager);
            }
        }

        void scrollToPositionWithOffset(int position, float positionPercent) {
            View targetView = layoutManager.findViewByPosition(position);
            if (targetView == null) {
                layoutManager.scrollToPosition(mPosition);
            } else {
                float offset = (int) (positionPercent * orientationHelper.getDecoratedMeasurement(targetView));
                layoutManager.scrollToPositionWithOffset(mPosition, (int) offset);
            }
        }

        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
            if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                mActiveScrollHelper = this;
            }
        }

        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            onActiveListScrolled(recyclerView, dx, dy);
        }
    }


}
