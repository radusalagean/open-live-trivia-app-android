package com.busytrack.openlivetrivia.view

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.busytrack.openlivetrivia.broadcastreceiver.TimeAwareReceiver
import com.busytrack.openlivetrivia.broadcastreceiver.TimeTickReceiver
import com.busytrack.openlivetrivia.generic.activity.BaseActivity
import com.busytrack.openlivetrivia.persistence.sharedprefs.SharedPreferencesRepository
import com.google.android.gms.common.util.Strings
import java.text.DateFormat
import java.util.*
import javax.inject.Inject

/**
 * TextView implementation which is aware of the preferred in-app date and time format
 */
open class TimeTextView(
    context: Context,
    attributeSet: AttributeSet? = null
) : AppCompatTextView(context, attributeSet) {

    private var prefix: String? = null
    private var timestamp: Long? = null

    @Inject
    lateinit var sharedPreferencesRepository: SharedPreferencesRepository

    private val timeAwareReceiver: TimeAwareReceiver = TimeAwareReceiver(object : TimeTickReceiver.Listener {
        override fun onReceive() {
            refreshTimeText()
        }
    })

    override fun onFinishInflate() {
        super.onFinishInflate()
        (context as BaseActivity).activityComponent.inject(this)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        timeAwareReceiver.register(context)
        refreshTimeText()
    }

    override fun onDetachedFromWindow() {
        timeAwareReceiver.unregister(context)
        super.onDetachedFromWindow()
    }

    fun setTimestamp(timestamp: Long, prefix: String? = null) {
        this.timestamp = timestamp
        this.prefix = prefix
        refreshTimeText()
    }

    fun refreshTimeText() {
        if (timestamp == null) {
            return
        }
        StringBuilder().apply {
            if (!Strings.isEmptyOrWhitespace(prefix)) {
                append(prefix)
                append(" ")
            }
            append(getTimeString(timestamp!!))
            text = toString()
        }
    }

    fun setPlaceholderText(placeholderText: String) {
        clear()
        text = placeholderText
    }

    fun clear() {
        text = null
        timestamp = null
    }

    /**
     * <!> TODO rewrite logic
     */
    private fun getTimeString(time: Long): String {
        if (sharedPreferencesRepository.isRelativeTimeEnabled()) {
            val currentTime = System.currentTimeMillis()
            val dividedTime: Long
            if (currentTime - time < 0)
                return "Negative time" // TODO move all hardcoded strings to strings.xml
            if (currentTime - time < 1000)
                return "Just now"
            when (val delta = (currentTime - time) / 1000) {
                in 0 until 60 -> return "A few seconds ago"
                in 60 until 60 * 60 -> {
                    dividedTime = delta / 60
                    return if (dividedTime == 1L) "$dividedTime minute ago" else "$dividedTime minutes ago"
                }
                in 60 * 60 until 24 * 60 * 60 -> {
                    dividedTime = delta / (60 * 60)
                    return if (dividedTime == 1L) "$dividedTime hour ago" else "$dividedTime hours ago"
                }
                in 24 * 60 * 60 until 30 * 24 * 60 * 60 -> {
                    dividedTime = delta / (24 * 60 * 60)
                    return if (dividedTime == 1L) "$dividedTime day ago" else "$dividedTime days ago"
                }
                in 30 * 24 * 60 * 60 until 12 * 30 * 24 * 60 * 60 -> {
                    dividedTime = delta / (30 * 24 * 60 * 60)
                    return if (dividedTime == 1L) "$dividedTime month ago" else "$dividedTime months ago"
                }
                else -> {
                    dividedTime = delta / (12 * 30 * 24 * 60 * 60)
                    return if (dividedTime == 1L) "$dividedTime year ago" else "$dividedTime years ago"
                }
            }
        } else {
            val date = Date(time)
            val stringDate = DateFormat.getDateInstance(DateFormat.MEDIUM).format(date)
            val stringTime = DateFormat.getTimeInstance(DateFormat.SHORT).format(date)
            return "$stringDate $stringTime"
        }
    }
}