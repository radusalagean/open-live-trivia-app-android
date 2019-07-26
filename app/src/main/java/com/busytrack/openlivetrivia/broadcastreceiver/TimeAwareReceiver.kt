package com.busytrack.openlivetrivia.broadcastreceiver

import android.content.Intent
import android.content.IntentFilter

// Sent when time-specific settings are changed
const val ACTION_RELATIVE_TIME_RELOAD = "com.busytrack.openlivetrivia.ACTION_RELATIVE_TIME_RELOAD"

class TimeAwareReceiver(listener: Listener) : TimeTickReceiver(listener) {
    override fun getIntentFilter(): IntentFilter =
        super.getIntentFilter().apply {
            addAction(ACTION_RELATIVE_TIME_RELOAD)
            addAction(Intent.ACTION_TIME_CHANGED)
            addAction(Intent.ACTION_TIMEZONE_CHANGED)
        }
}