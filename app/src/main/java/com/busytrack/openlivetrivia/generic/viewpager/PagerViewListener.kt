package com.busytrack.openlivetrivia.generic.viewpager

import androidx.viewbinding.ViewBinding

interface PagerViewListener<BINDING : ViewBinding> {
    fun onViewInflated(index: Int, binding: BINDING)
    fun onViewDestroyed(index: Int)
}