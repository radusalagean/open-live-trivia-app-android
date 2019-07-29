package com.busytrack.openlivetrivia.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import com.busytrack.openlivetrivia.R
import com.busytrack.openlivetriviainterface.socket.model.UserRightsLevel

class RightsLevelTextView(
    context: Context,
    attributeSet: AttributeSet
) : TextView(context, attributeSet, R.style.rights_level) {

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