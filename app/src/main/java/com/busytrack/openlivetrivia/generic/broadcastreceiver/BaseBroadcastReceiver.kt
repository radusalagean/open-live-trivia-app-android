package com.busytrack.openlivetrivia.generic.broadcastreceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.core.content.ContextCompat
import timber.log.Timber

/**
 * Base class for all broadcast receivers, covers registering and unregistering implementation details
 */
abstract class BaseBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Timber.tag(javaClass.simpleName).d("onReceive($intent)")
    }

    abstract fun getIntentFilter(): IntentFilter

    fun register(context: Context) {
        ContextCompat.registerReceiver(
            context,
            this,
            getIntentFilter(),
            ContextCompat.RECEIVER_NOT_EXPORTED
        )
    }

    fun unregister(context: Context) {
        context.unregisterReceiver(this)
    }
}