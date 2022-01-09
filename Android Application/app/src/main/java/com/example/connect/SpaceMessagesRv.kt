package com.example.connect

import androidx.recyclerview.widget.RecyclerView

import android.graphics.Rect
import android.view.*
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

class SpaceMessagesRv(var space: Int) : ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.top = space
        outRect.left = space
    }
}