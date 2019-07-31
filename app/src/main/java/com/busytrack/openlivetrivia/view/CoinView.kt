package com.busytrack.openlivetrivia.view

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.busytrack.openlivetrivia.R

class CoinView(
    context: Context,
    attributeSet: AttributeSet
) : AppCompatImageView(context, attributeSet) {

    init {
        setBackgroundResource(R.drawable.point)
    }
}