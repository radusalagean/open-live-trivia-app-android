package com.busytrack.openlivetrivia.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.busytrack.openlivetrivia.databinding.LayoutInfoBarBinding
import com.google.android.material.snackbar.ContentViewCallback

class InfoBarView(
    context: Context,
    attributeSet: AttributeSet
) : FrameLayout(context, attributeSet), ContentViewCallback {

    val binding: LayoutInfoBarBinding = LayoutInfoBarBinding.inflate(
        LayoutInflater.from(context),
        this
    )

    override fun animateContentIn(p0: Int, p1: Int) {
    }

    override fun animateContentOut(p0: Int, p1: Int) {
    }
}