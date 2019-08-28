package com.busytrack.openlivetrivia.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import com.busytrack.openlivetrivia.R
import com.busytrack.openlivetriviainterface.socket.model.UserRightsLevel

/**
 * TextView implementation which displays the rights level badge, based on the [rightsLevel] value
 */
class RightsLevelTextView(
    context: Context,
    attributeSet: AttributeSet? = null
) : AppCompatTextView(context, attributeSet, R.style.rights_level) {

    var rightsLevel: UserRightsLevel? = null
        set(value) {
            field = value ?: UserRightsLevel.REGULAR
            if (field != UserRightsLevel.REGULAR) {
                text = field?.name
                visibility = View.VISIBLE
            } else {
                text = null
                visibility = View.INVISIBLE
            }
        }
}