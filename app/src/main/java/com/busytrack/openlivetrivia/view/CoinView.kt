package com.busytrack.openlivetrivia.view

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import com.busytrack.openlivetrivia.R

class CoinView(
    context: Context,
    attributeSet: AttributeSet
) : ImageView(context, attributeSet) {

    init {
        setBackgroundResource(R.drawable.point)
    }
}