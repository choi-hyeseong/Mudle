package com.comet.mudle.recycler

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class ChatDecoration : RecyclerView.ItemDecoration() {

    private val padding = 10;

    override fun getItemOffsets(outRect: Rect,
                                view: View,
                                parent: RecyclerView,
                                state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.top = padding
        outRect.left = padding
        outRect.bottom = padding
        outRect.right = padding
    }
}