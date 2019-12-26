package com.xueqin.contacts.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.xueqin.contacts.R;
import com.xueqin.contacts.model.ContactInfo;
import com.xueqin.contacts.util.AvatarLoader;

import java.util.List;

public class AvatarListAdapter extends RecyclerView.Adapter<AvatarListAdapter.AvatarViewHolder> {

    @NonNull
    private Context mContext;
    @NonNull
    private LayoutInflater mInflater;
    @Nullable
    private List<ContactInfo> mContactList;

    @Nullable
    private OnAvatarItemClickListener mLi;

    public AvatarListAdapter(@NonNull Context context) {
        this(context, null);
    }

    public AvatarListAdapter(@NonNull Context context, @Nullable List<ContactInfo> contactList) {
        mContext = context;
        mContactList = contactList;
        mInflater = LayoutInflater.from(context);
    }

    public void setOnAvatarItemClickListener(OnAvatarItemClickListener li) {
        mLi = li;
    }

    public void setContactList(@Nullable List<ContactInfo> contactList) {
        mContactList = contactList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AvatarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AvatarViewHolder(mInflater.inflate(R.layout.view_contact_avatar, parent, false));
    }

    @SuppressWarnings("all")
    @Override
    public void onBindViewHolder(@NonNull AvatarViewHolder holder, int position) {
        AvatarLoader.getInstance(mContext).loadAvatar(holder.avatarView, mContactList.get(position));
    }

    @Override
    public int getItemCount() {
        return mContactList == null ? 0 : mContactList.size();
    }

    class AvatarViewHolder extends RecyclerView.ViewHolder {

        ImageView avatarView;

        AvatarViewHolder(@NonNull final View itemView) {
            super(itemView);
            avatarView = itemView.findViewById(R.id.iv_avatar);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mLi != null) {
                        mLi.onClicked(v, getAdapterPosition());
                    }
                }
            });
        }
    }

    public interface OnAvatarItemClickListener {
        void onClicked(View itemView, int position);
    }
}
