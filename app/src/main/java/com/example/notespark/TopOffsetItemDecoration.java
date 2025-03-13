package com.example.notespark;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class TopOffsetItemDecoration extends RecyclerView.ItemDecoration {
    private final int topOffset;

    public TopOffsetItemDecoration(int topOffset) {
        this.topOffset = topOffset;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.top = topOffset;
        }
    }
}