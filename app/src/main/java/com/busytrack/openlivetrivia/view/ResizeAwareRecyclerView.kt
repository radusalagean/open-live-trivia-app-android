package com.busytrack.openlivetrivia.view

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView

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