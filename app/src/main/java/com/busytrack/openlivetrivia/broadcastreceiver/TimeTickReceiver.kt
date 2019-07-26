package com.busytrack.openlivetrivia.broadcastreceiver

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import com.busytrack.openlivetrivia.generic.broadcastreceiver.BaseBroadcastReceiver

open class TimeTickReceiver(
    private val listener: Listener
) : BaseBroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        listener.onReceive()
    }

    override fun getIntentFilter(): IntentFilter = IntentFilter(Intent.ACTION_TIME_TICK)

    interface Listener {
        fun onReceive()
    }
}