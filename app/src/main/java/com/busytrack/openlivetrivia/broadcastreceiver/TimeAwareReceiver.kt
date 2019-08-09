package com.busytrack.openlivetrivia.broadcastreceiver

import android.content.Intent
import android.content.IntentFilter

// Sent when time-specific settings are changed
const val ACTION_RELATIVE_TIME_RELOAD = "com.busytrack.openlivetrivia.ACTION_RELATIVE_TIME_RELOAD"

/**
 * A wrapper for [TimeTickReceiver], which also handles Time and Time Zone changes from the phone settings
 */
class TimeAwareReceiver(listener: Listener) : TimeTickReceiver(listener) {
    override fun getIntentFilter(): IntentFilter =
        super.getIntentFilter().apply {
            addAction(ACTION_RELATIVE_TIME_RELOAD)
            addAction(Intent.ACTION_TIME_CHANGED)
            addAction(Intent.ACTION_TIMEZONE_CHANGED)
        }
}