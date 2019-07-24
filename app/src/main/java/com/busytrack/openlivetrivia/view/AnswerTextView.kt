package com.busytrack.openlivetrivia.view

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.res.ResourcesCompat
import com.busytrack.openlivetrivia.R

class AnswerTextView(
    context: Context,
    attributeSet: AttributeSet
) : AppCompatTextView(context, attributeSet) {

    var defaultColor = textColors

    fun reveal() {
        setTextColor(ResourcesCompat.getColor(
            resources,
            R.color.colorNegative,
            null
        ))
    }

    fun correct() {
        setTextColor(ResourcesCompat.getColor(
            resources,
            R.color.colorPositive,
            null
        ))
    }

    fun resetState() {
        setTextColor(defaultColor)
    }
}