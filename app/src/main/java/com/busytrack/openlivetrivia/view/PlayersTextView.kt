package com.busytrack.openlivetrivia.view

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import com.busytrack.openlivetrivia.R

class PlayersTextView(
    context: Context,
    attributeSet: AttributeSet
) : AppCompatTextView(context, attributeSet) {

    var playersCount: Int = 0
        set(value) {
            field = value
            text = value.toString()
        }

    fun incrementCount() {
        playersCount++
    }

    fun decrementCount() {
        playersCount--
    }
}