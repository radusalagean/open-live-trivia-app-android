package com.busytrack.openlivetrivia.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.busytrack.openlivetrivia.R
import com.google.android.material.snackbar.ContentViewCallback

class InfoBarView(
    context: Context,
    attributeSet: AttributeSet
) : FrameLayout(context, attributeSet), ContentViewCallback {

    init {
        View.inflate(context, R.layout.layout_info_bar, this)
    }

    override fun animateContentIn(p0: Int, p1: Int) {
    }

    override fun animateContentOut(p0: Int, p1: Int) {
    }
}