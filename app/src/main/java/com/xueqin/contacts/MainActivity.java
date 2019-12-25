package com.xueqin.contacts;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.xueqin.contacts.model.ContactInfo;
import com.xueqin.contacts.util.AssetUtils;
import com.xueqin.contacts.util.AvatarLoader;
import com.xueqin.contacts.widget.ExtraOffsetDecoration;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private RecyclerView mAvatarListView;
    private RecyclerView mIntroductionListView;

    private List<ContactInfo> mContactList;
    private int mSelectedPosition;
    private int mAvatarSize;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContactList = AssetUtils.loadAllContacts(this);
        mAvatarSize = getResources().getDimensionPixelSize(R.dimen.avatar_size);
        mSelectedPosition = 0;
        initAvatarListView();
        initIntroductionListView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initAvatarListView() {
        mAvatarListView = findViewById(R.id.avatar_list);
        mAvatarListView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mAvatarListView.setAdapter(new AvatarListAdapter());
        mAvatarListView.addItemDecoration(new ExtraOffsetDecoration(computeExtraOffsetOfAvatarList()));
        new LinearSnapHelper().attachToRecyclerView(mAvatarListView);
        mAvatarListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull final RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int width = recyclerView.getWidth();
                        LinearLayoutManager llm = (LinearLayoutManager) recyclerView.getLayoutManager();
                        View selectChild = null;
                        int dist = width;
                        for (int i = 0; i < llm.getChildCount(); i++) {
                            View child = llm.getChildAt(i);
                            int childMiddle = child.getLeft() + child.getWidth()/2;
                            int childDist = Math.abs(width/2-childMiddle);
                            if (childDist < dist) {
                                dist = childDist;
                                selectChild = child;
                            }
                        }
                        if (selectChild != null) {
                            int selectPosition = recyclerView.getChildAdapterPosition(selectChild);
                            if (mSelectedPosition != selectPosition) {
                                mSelectedPosition = selectPosition;
                                mIntroductionListView.smoothScrollToPosition(mSelectedPosition);
                            }
                        }
                    }
                });
            }
        });
    }

    private void initIntroductionListView() {
        mIntroductionListView = findViewById(R.id.introduction_list);
        mIntroductionListView.setLayoutManager(new LinearLayoutManager(this));
        mIntroductionListView.setAdapter(new IntroductionListAdapter());
        new PagerSnapHelper().attachToRecyclerView(mIntroductionListView);
        mIntroductionListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    LinearLayoutManager llm = (LinearLayoutManager) recyclerView.getLayoutManager();
                    if (llm.getChildCount() > 0) {
                        int position = recyclerView.getChildAdapterPosition(llm.getChildAt(0));
                        if (mSelectedPosition != position) {
                            mSelectedPosition = position;
                            mAvatarListView.smoothScrollToPosition(mSelectedPosition);
                        }
                    }
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    private int computeExtraOffsetOfAvatarList() {
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int avatarSize = getResources().getDimensionPixelSize(R.dimen.avatar_size);
        return (screenWidth - avatarSize) / 2;
    }

    private class AvatarListAdapter extends RecyclerView.Adapter<AvatarListAdapter.AvatarItemHolder> {

        @NonNull
        @Override
        public AvatarItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(MainActivity.this)
                    .inflate(R.layout.view_contact_avatar, parent, false);
            return new AvatarItemHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull AvatarItemHolder holder, int position) {
            AvatarLoader.getInstance(MainActivity.this)
                    .loadAvatar(holder.avatarView, mContactList.get(position));
        }

        @Override
        public int getItemCount() {
            return mContactList.size();
        }

        private class AvatarItemHolder extends RecyclerView.ViewHolder {

            ImageView avatarView;

            public AvatarItemHolder(@NonNull View itemView) {
                super(itemView);
                avatarView = itemView.findViewById(R.id.iv_avatar);
            }
        }
    }

    private class IntroductionListAdapter extends RecyclerView.Adapter<IntroductionListAdapter.IntroductionItemHolder> {

        @NonNull
        @Override
        public IntroductionItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new IntroductionItemHolder(LayoutInflater.from(MainActivity.this)
                    .inflate(R.layout.view_contact_instruction, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull IntroductionItemHolder holder, int position) {
            ContactInfo contactInfo = mContactList.get(position);
            holder.nameText.setText(contactInfo.getFirstName() + " " + contactInfo.getLastName());
            holder.titleText.setText(contactInfo.getTitle());
            holder.introductionText.setText(contactInfo.getIntroduction());
        }

        @Override
        public int getItemCount() {
            return mContactList.size();
        }

        private class IntroductionItemHolder extends RecyclerView.ViewHolder {

            TextView nameText;
            TextView titleText;
            TextView introductionText;

            public IntroductionItemHolder(@NonNull View itemView) {
                super(itemView);
                nameText = itemView.findViewById(R.id.tv_name);
                titleText = itemView.findViewById(R.id.tv_title);
                introductionText = itemView.findViewById(R.id.tv_introduction);
            }
        }
    }
}
