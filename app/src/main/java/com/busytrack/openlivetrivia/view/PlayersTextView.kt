package com.busytrack.openlivetrivia.view

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

/**
 * TextView implementation which is able to keep the players count and easily increment / decrement the count based on
 * peer presence events (leave / join), received from the server
 */
class PlayersTextView(
    context: Context,
    attributeSet: AttributeSet? = null
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