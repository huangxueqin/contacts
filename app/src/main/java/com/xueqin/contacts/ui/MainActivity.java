package com.xueqin.contacts.ui;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.xueqin.contacts.R;
import com.xueqin.contacts.model.ContactInfo;
import com.xueqin.contacts.ui.adapter.AvatarListAdapter;
import com.xueqin.contacts.ui.adapter.IntroductionListAdapter;
import com.xueqin.contacts.ui.helper.AvatarHighlighter;
import com.xueqin.contacts.ui.helper.AvatarListSlowScrollHelper;
import com.xueqin.contacts.ui.helper.ContactScrollSynchronizer;
import com.xueqin.contacts.ui.helper.StartPagerSnapHelper;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private static final String KEY_AVATAR_POSITION = "avatar_position";

    private RecyclerView mAvatarListView;
    private RecyclerView mIntroductionListView;
    private FrameLayout mAvatarListContainer;
    private View mDividerView;

    private AvatarListAdapter mAvatarListAdapter;
    private IntroductionListAdapter mIntroductionListAdapter;

    private ContactScrollSynchronizer mScrollSynchronizer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adjustWindowStyle();
        setContentView(R.layout.activity_main);
        initViews();
        // observe contact info loading result
        ViewModelProviders.of(this).get(ContactViewModel.class)
                .getContactList()
                .observe(this, new Observer<List<ContactInfo>>() {
                    @Override
                    public void onChanged(List<ContactInfo> contactInfos) {
                        Log.d(TAG, "avatar info changed");
                        mAvatarListAdapter.setContactList(contactInfos);
                        mIntroductionListAdapter.setContactList(contactInfos);
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void adjustWindowStyle() {
        int systemUiFlag = getWindow().getDecorView().getSystemUiVisibility();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // light status bar
            systemUiFlag |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // light navigation bar
            systemUiFlag |= View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
        }
        getWindow().getDecorView().setSystemUiVisibility(systemUiFlag);
    }

    private void initViews() {
        mAvatarListContainer = findViewById(R.id.avatar_list_container);
        mDividerView = findViewById(R.id.divider);
        initAvatarListView();
        initIntroductionListView();
        // sync the scroll behavior of the two list
        mScrollSynchronizer = ContactScrollSynchronizer.syncContactScroll(this, mAvatarListView, mIntroductionListView);
    }

    private void initAvatarListView() {
        mAvatarListView = findViewById(R.id.avatar_list);
        mAvatarListView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        new LinearSnapHelper().attachToRecyclerView(mAvatarListView);
        // TODO: use itemDecoration maybe a better idea than use padding trick
        mAvatarListView.setPadding(computeAvatarListPaddingSize(), 0, computeAvatarListPaddingSize(), 0);
        mAvatarListView.setClipToPadding(false);
        // init adapter
        mAvatarListAdapter = new AvatarListAdapter(this);
        mAvatarListView.setAdapter(mAvatarListAdapter);
        // add highlight support
        AvatarHighlighter.trackHighlight(mAvatarListView);
        // add click listener
        mAvatarListAdapter.setOnAvatarItemClickListener(new AvatarListAdapter.OnAvatarItemClickListener() {
            @Override
            public void onClicked(View itemView, int position) {
                mScrollSynchronizer.setAvatarListActive();
                AvatarListSlowScrollHelper.smoothScrollToPosition(mAvatarListView, position, 500);
            }
        });
    }

    private void initIntroductionListView() {
        mIntroductionListView = findViewById(R.id.introduction_list);
        mIntroductionListView.setLayoutManager(new LinearLayoutManager(this));
        new StartPagerSnapHelper().attachToRecyclerView(mIntroductionListView);
        // init adapter
        mIntroductionListAdapter = new IntroductionListAdapter(this);
        mIntroductionListView.setAdapter(mIntroductionListAdapter);
        // show top shadow when scroll
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mIntroductionListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    mDividerView.setVisibility(
                            newState == RecyclerView.SCROLL_STATE_IDLE ? View.INVISIBLE : View.VISIBLE);
                }
            });
        }
    }

    private int computeAvatarListPaddingSize() {
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int avatarSize = getResources().getDimensionPixelSize(R.dimen.avatar_size);
        return (screenWidth - avatarSize) / 2;
    }

}
