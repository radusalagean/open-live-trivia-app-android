package com.busytrack.openlivetrivia.view

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView

/**
 * RecyclerView implementation which is aware of resizing events (caused by the on-screen keyboard being shown or hidden)
 * and scrolls to the latest element in specific cases
 */
class ResizeAwareRecyclerView(
    context: Context,
    attributeSet: AttributeSet
) : RecyclerView(context, attributeSet) {

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (oldh > h) {
            adapter?.let { scrollToPosition(it.itemCount - 1) } // TODO check if needs further work
        }
    }
}