package com.busytrack.openlivetrivia.generic.recyclerview

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * Recycler View Item Decoration (adds horizontal and vertical padding to items)
 */
class ListItemDecoration(
    private val left: Int,
    private val top: Int,
    private val right: Int,
    private val bottom: Int
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        outRect.apply {
            left = this@ListItemDecoration.left
            if (parent.getChildAdapterPosition(view) == 0) {
                top = this@ListItemDecoration.top
            }
            right = this@ListItemDecoration.right
            if (parent.getChildAdapterPosition(view) == parent.adapter?.itemCount!! - 1) {
                bottom = this@ListItemDecoration.bottom
            }
        }
    }
}