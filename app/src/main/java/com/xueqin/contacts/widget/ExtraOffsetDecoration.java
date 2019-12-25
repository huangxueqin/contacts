package com.xueqin.contacts.widget;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ExtraOffsetDecoration extends RecyclerView.ItemDecoration {

    private int mOffset;

    public ExtraOffsetDecoration(int offset) {
        mOffset = offset;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect,
                               @NonNull View view,
                               @NonNull RecyclerView parent,
                               @NonNull RecyclerView.State state) {
        RecyclerView.Adapter adapter = parent.getAdapter();
        if (adapter == null) {
            return;
        }
        final int adapterPosition = parent.getChildAdapterPosition(view);
        if (adapterPosition == 0) {
            outRect.left = mOffset;
        } else if (adapterPosition == adapter.getItemCount()-1) {
            outRect.right = mOffset;
        }
    }
}
