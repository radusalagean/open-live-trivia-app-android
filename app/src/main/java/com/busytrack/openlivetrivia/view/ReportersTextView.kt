package com.busytrack.openlivetrivia.view

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.busytrack.openlivetrivia.R
import kotlin.math.min

const val DISPLAYED_REPORTERS_COUNT = 3

class ReportersTextView(
    context: Context,
    attributeSet: AttributeSet
) : AppCompatTextView(context, attributeSet) {

    private val prefix = context.getString(R.string.reporter_prefix)
    private val accountUnavailable = context.getString(R.string.reporter_account_unavailable)

    var reporters: List<String>? = null
        set(value) {
            field = value
            if (value == null) {
                text = null
            } else {
                val displayedReporters = if (value.isEmpty()) {
                    accountUnavailable
                } else {
                    value.subList(0, min(value.size, DISPLAYED_REPORTERS_COUNT)).joinToString().let {
                        if (value.size > DISPLAYED_REPORTERS_COUNT) {
                            val suffix = context.getString(R.string.reporter_suffix,
                                value.size - DISPLAYED_REPORTERS_COUNT)
                            "$it $suffix"
                        } else {
                            it
                        }
                    }
                }
                text = "$prefix $displayedReporters"
            }
        }
}