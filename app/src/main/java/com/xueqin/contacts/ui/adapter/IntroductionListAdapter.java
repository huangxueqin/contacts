package com.xueqin.contacts.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.xueqin.contacts.R;
import com.xueqin.contacts.model.ContactInfo;
import com.xueqin.contacts.util.ScreenUtils;

import java.util.List;

public class IntroductionListAdapter extends RecyclerView.Adapter<IntroductionListAdapter.IntroductionViewHolder> {

    @NonNull
    private Context mContext;
    @NonNull
    private LayoutInflater mInflater;
    @Nullable
    private List<ContactInfo> mContactList;

    public IntroductionListAdapter(@NonNull Context context) {
        this(context, null);
    }

    public IntroductionListAdapter(@NonNull Context context, @Nullable List<ContactInfo> contactList) {
        mContext = context;
        mContactList = contactList;
        mInflater = LayoutInflater.from(context);
    }

    public void setContactList(@Nullable List<ContactInfo> contactList) {
        mContactList = contactList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public IntroductionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new IntroductionViewHolder(mInflater.inflate(R.layout.view_contact_instruction, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull IntroductionViewHolder holder, int position) {
        final ContactInfo contactInfo = mContactList.get(position);
        holder.nameText.setText(mContext.getResources()
                .getString(R.string.contact_user_name_format, contactInfo.getFirstName(), contactInfo.getLastName()));
        holder.titleText.setText(contactInfo.getTitle());
        holder.introductionText.setText(contactInfo.getIntroduction());
        // when screen is horizontal we set itemView's height to wrap_content
        if (ScreenUtils.isScreenPortrait(mContext)) {
            holder.itemView.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
        } else {
            holder.itemView.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
        }
    }

    @Override
    public int getItemCount() {
        return mContactList == null ? 0 : mContactList.size();
    }

    class IntroductionViewHolder extends RecyclerView.ViewHolder {

        TextView nameText;
        TextView titleText;
        TextView introductionText;

        IntroductionViewHolder(@NonNull View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.tv_name);
            titleText = itemView.findViewById(R.id.tv_title);
            introductionText = itemView.findViewById(R.id.tv_introduction);
        }
    }

}
