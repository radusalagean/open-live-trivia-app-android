package com.busytrack.openlivetrivia.broadcastreceiver

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nhaarman.mockitokotlin2.verify
import org.junit.After
import org.junit.Before

import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito

@RunWith(AndroidJUnit4::class)
class TimeAwareReceiverTest {

    lateinit var listener: TimeTickReceiver.Listener

    lateinit var timeAwareReceiver: TimeAwareReceiver

    @Before
    fun setUp() {
        listener = Mockito.mock(TimeTickReceiver.Listener::class.java)
        timeAwareReceiver = TimeAwareReceiver(listener)
        timeAwareReceiver.register(ApplicationProvider.getApplicationContext())
    }

    @After
    fun tearDown() {
        timeAwareReceiver.unregister(ApplicationProvider.getApplicationContext())
    }

    @Test
    fun relativeTimeReloadBroadcasted_listenerNotified() {
        ApplicationProvider.getApplicationContext<Context>()
            .sendBroadcast(Intent(ACTION_RELATIVE_TIME_RELOAD))

        verify(listener).onReceive()
    }

    @Test
    fun timeChangedBroadcasted_listenerNotified() {
        ApplicationProvider.getApplicationContext<Context>()
            .sendBroadcast(Intent(Intent.ACTION_TIME_CHANGED))

        verify(listener).onReceive()
    }

    @Test
    fun timezoneChangedBroadcasted_listenerNotified() {
        ApplicationProvider.getApplicationContext<Context>()
            .sendBroadcast(Intent(Intent.ACTION_TIMEZONE_CHANGED))

        verify(listener).onReceive()
    }
}