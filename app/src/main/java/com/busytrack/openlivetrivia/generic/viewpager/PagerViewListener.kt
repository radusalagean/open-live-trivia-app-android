package com.busytrack.openlivetrivia.generic.viewpager

import android.view.View

interface PagerViewListener {
    fun onViewInflated(view: View)
    fun onViewDestroyed(view: View)
}