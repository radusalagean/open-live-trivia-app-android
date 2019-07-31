package com.busytrack.openlivetrivia.extension

import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.snackbar.Snackbar

/**
 * If [visible] is false, the View will be set to [View.INVISIBLE]
 */
fun View.setVisibleSoft(visible: Boolean) {
    visibility = if (visible) View.VISIBLE else View.INVISIBLE
}

/**
 * If [visible] is false, the View will be set to [View.GONE]
 */
fun View.setVisibleHard(visible: Boolean) {
    visibility = if (visible) View.VISIBLE else View.GONE
}

/**
 * Taken from [Snackbar] source code
 */
fun View?.findSuitableParent(): ViewGroup? {
    var view = this
    var fallback: ViewGroup? = null
    do {
        if (view is CoordinatorLayout) {
            // We've found a CoordinatorLayout, use it
            return view
        } else if (view is FrameLayout) {
            if (view.id == android.R.id.content) {
                // If we've hit the decor content view, then we didn't find a CoL in the
                // hierarchy, so use it.
                return view
            } else {
                // It's not the content view but we'll use it as our fallback
                fallback = view
            }
        }

        if (view != null) {
            // Else, we will loop and crawl up the view hierarchy and try to find a parent
            val parent = view.parent
            view = if (parent is View) parent else null
        }
    } while (view != null)

    // If we reach here then we didn't find a CoL or a suitable content view so we'll fallback
    return fallback
}

/**
 * Get all children of a parent [View] (for debugging purposes)
 *
 * from: https://stackoverflow.com/a/11263152/11631823
 */
fun View.getAllChildren(): ArrayList<View> {
    if (this !is ViewGroup) {
        val viewArrayList = ArrayList<View>()
        viewArrayList.add(this)
        return viewArrayList
    }

    val result = ArrayList<View>()

    for (i in 0 until childCount) {
        val child = getChildAt(i)
        val viewArrayList = ArrayList<View>()
        viewArrayList.add(this)
        viewArrayList.addAll(child.getAllChildren())
        result.addAll(viewArrayList)
    }
    return result
}