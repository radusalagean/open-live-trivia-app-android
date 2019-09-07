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
import org.mockito.Mockito.mock

@RunWith(AndroidJUnit4::class)
class TimeTickReceiverTest {

    lateinit var listener: TimeTickReceiver.Listener

    lateinit var timeTickReceiver: TimeTickReceiver

    @Before
    fun setUp() {
        listener = mock(TimeTickReceiver.Listener::class.java)
        timeTickReceiver = TimeTickReceiver(listener)
        timeTickReceiver.register(ApplicationProvider.getApplicationContext())
    }

    @After
    fun tearDown() {
        timeTickReceiver.unregister(ApplicationProvider.getApplicationContext())
    }

    @Test
    fun timeTickBroadcasted_listenerNotified() {
        ApplicationProvider.getApplicationContext<Context>()
            .sendBroadcast(Intent(Intent.ACTION_TIME_TICK))

        verify(listener).onReceive()
    }
}