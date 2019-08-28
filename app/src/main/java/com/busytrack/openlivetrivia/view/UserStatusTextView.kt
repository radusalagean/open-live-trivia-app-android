package com.busytrack.openlivetrivia.view

import android.content.Context
import android.util.AttributeSet
import androidx.core.content.res.ResourcesCompat
import com.busytrack.openlivetrivia.R

class UserStatusTextView(
    context: Context,
    attributeSet: AttributeSet? = null
) : TimeTextView(context, attributeSet) {

    val defaultTextColor = textColors

    fun setPlaying(playing: Boolean, lastSeenTimestamp: Long? = null) {
        if (playing) {
            setTextColor(
                ResourcesCompat.getColor(
                    resources,
                    R.color.colorPositive,
                    null
                )
            )
            setPlaceholderText(context.getString(R.string.user_playing))
        } else {
            requireNotNull(lastSeenTimestamp) {
                "Please provide a valid value for lastSeenTimestamp param"
            }
            setTextColor(defaultTextColor)
            setTimestamp(lastSeenTimestamp, context.getString(R.string.user_last_seen_prefix))
        }
    }
}