package com.xueqin.contacts;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.xueqin.contacts.model.ContactInfo;
import com.xueqin.contacts.ui.AvatarListAdapter;
import com.xueqin.contacts.ui.IntroductionListAdapter;
import com.xueqin.contacts.ui.snap.ScrollSynchronizer;
import com.xueqin.contacts.ui.snap.StartPagerSnapHelper;
import com.xueqin.contacts.util.AssetUtils;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private static final String KEY_AVATAR_POSITION = "avatar_position";

    private RecyclerView mAvatarListView;
    private RecyclerView mIntroductionListView;

    private List<ContactInfo> mContactList;
    private AvatarListAdapter mAvatarListAdapter;
    private IntroductionListAdapter mIntroductionListAdapter;

    private ScrollSynchronizer mScrollHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContactList = AssetUtils.loadAllContacts(this);
        initViews();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initViews() {
        initAvatarListView();
        initIntroductionListView();
        // sync the scroll behavior of the two list
        mScrollHelper = ScrollSynchronizer.syncContactScroll(mAvatarListView, mIntroductionListView);
        // handle select item change
        mScrollHelper.registerPositionChangeListener(new ScrollSynchronizer.OnPositionChangeListener() {
            @Override
            public void onPositionChange(int lastPosition, int currentPosition) {
                onSelectAvatarChanged(currentPosition);
            }
        });
    }

    private void initAvatarListView() {
        mAvatarListView = findViewById(R.id.avatar_list);
        mAvatarListView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        new LinearSnapHelper().attachToRecyclerView(mAvatarListView);
        // fixme: maybe better to use itemDecoration to add extra padding
        mAvatarListView.setPadding(computeExtraOffsetOfAvatarList(), 0, computeExtraOffsetOfAvatarList(), 0);
        mAvatarListView.setClipToPadding(false);
        // init adapter
        mAvatarListAdapter = new AvatarListAdapter(this, mContactList);
        mAvatarListView.setAdapter(mAvatarListAdapter);
        // add click listener
        mAvatarListAdapter.setOnAvatarItemClickListener(new AvatarListAdapter.OnAvatarItemClickListener() {
            @Override
            public void onClicked(View itemView, int position) {
                mScrollHelper.setCurrentActiveScroller(mAvatarListView, position, true);
            }
        });
    }

    private void initIntroductionListView() {
        mIntroductionListView = findViewById(R.id.introduction_list);
        mIntroductionListView.setLayoutManager(new LinearLayoutManager(this));
        new StartPagerSnapHelper().attachToRecyclerView(mIntroductionListView);
        // init adapter
        mIntroductionListAdapter = new IntroductionListAdapter(this, mContactList);
        mIntroductionListView.setAdapter(mIntroductionListAdapter);
    }

    private int computeExtraOffsetOfAvatarList() {
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int avatarSize = getResources().getDimensionPixelSize(R.dimen.avatar_size);
        return (screenWidth - avatarSize) / 2;
    }

    private void onSelectAvatarChanged(int position) {
        if (mAvatarListView == null || mAvatarListView.getLayoutManager() == null) {
            return;
        }
        final RecyclerView.LayoutManager lm = mAvatarListView.getLayoutManager();
        final int childCount = lm.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = lm.getChildAt(i);
            if (child == null) {
                return;
            }
            int childPosition = mAvatarListView.getChildAdapterPosition(child);
            child.setSelected(childPosition == position);
        }
    }
}
