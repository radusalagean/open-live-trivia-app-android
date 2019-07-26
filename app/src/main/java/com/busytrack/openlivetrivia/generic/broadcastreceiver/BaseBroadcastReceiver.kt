package com.busytrack.openlivetrivia.generic.broadcastreceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.IntentFilter
import android.content.Intent
import timber.log.Timber


abstract class BaseBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Timber.tag(javaClass.simpleName).d("onReceive($intent)")
    }

    abstract fun getIntentFilter(): IntentFilter

    fun register(context: Context) {
        context.registerReceiver(this, getIntentFilter())
    }

    fun unregister(context: Context) {
        context.unregisterReceiver(this)
    }
}