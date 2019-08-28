package com.busytrack.openlivetrivia.view

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView

const val THRESHOLD = 1000

/**
 * Used for paginated content, will trigger loading of the next page
 */
class ScrollAwareRecyclerView(
    context: Context,
    attributeSet: AttributeSet? = null
) : RecyclerView(context, attributeSet) {

    var listener: Listener? = null

    override fun onScrolled(dx: Int, dy: Int) {
        super.onScrolled(dx, dy)
        val bottomY = computeVerticalScrollOffset() + height
        if (computeVerticalScrollRange() - THRESHOLD <= bottomY) {
            listener?.onThresholdExceeded()
        }
    }

    interface Listener {
        fun onThresholdExceeded()
    }
}