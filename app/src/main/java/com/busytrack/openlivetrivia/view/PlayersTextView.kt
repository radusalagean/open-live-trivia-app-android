package com.busytrack.openlivetrivia.view

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import com.busytrack.openlivetrivia.R

class PlayersTextView(
    context: Context,
    attributeSet: AttributeSet
) : TextView(context, attributeSet) {

    var playersCount: Int = 0
        set(value) {
            field = value
            val stringId = if (value == 1) R.string.game_online_players_singular_form else R.string.game_online_players
            text = context.getString(stringId, value)
        }

    fun incrementCount() {
        playersCount++
    }

    fun decrementCount() {
        playersCount--
    }
}