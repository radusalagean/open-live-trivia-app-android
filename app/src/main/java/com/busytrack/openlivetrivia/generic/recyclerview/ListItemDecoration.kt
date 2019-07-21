package com.busytrack.openlivetrivia.generic.recyclerview

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class ListItemDecoration(private val offset: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        outRect.apply {
            if (parent.getChildAdapterPosition(view) == 0) {
                top = offset
            }
            if (parent.getChildAdapterPosition(view) != parent.adapter?.itemCount!! - 1) {
                bottom = offset
            }
        }
    }
}