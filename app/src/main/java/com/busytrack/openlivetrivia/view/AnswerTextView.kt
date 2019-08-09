package com.busytrack.openlivetrivia.view

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.res.ResourcesCompat
import com.busytrack.openlivetrivia.R

/**
 * Answer-specific TextView, handles correct and incorrect answers
 */
class AnswerTextView(
    context: Context,
    attributeSet: AttributeSet
) : AppCompatTextView(context, attributeSet) {

    private var defaultTextColor = textColors

    fun reveal() {
        setTextColor(ResourcesCompat.getColor(
            resources,
            R.color.colorRevealAnswer,
            null
        ))
    }

    fun correct() {
        setTextColor(ResourcesCompat.getColor(
            resources,
            R.color.colorCorrectAnswer,
            null
        ))
    }

    fun resetState() {
        setTextColor(defaultTextColor)
    }
}