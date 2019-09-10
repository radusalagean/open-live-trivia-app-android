package com.busytrack.openlivetrivia.infobar

import androidx.annotation.VisibleForTesting
import com.google.android.material.snackbar.BaseTransientBottomBar
import java.util.*

/**
 * Responsible for managing [InfoBar] message queue
 *
 * Role: Handles showing enqueued messages while waiting for the auto dismissal of any previously shown message, thus
 * avoiding the flooding of messages on the screen
 */
class InfoBarManager {

    private val messageQueue: Queue<InfoBarConfiguration> = LinkedList()
    private var infoBarContract: InfoBarContract? = null

    @VisibleForTesting val callback = object : BaseTransientBottomBar.BaseCallback<InfoBar>() {
        override fun onDismissed(transientBottomBar: InfoBar?, event: Int) {
            messageQueue.poll()
            processNextMessage()
        }
    }

    fun enqueueMessage(infoBarConfiguration: InfoBarConfiguration) {
        messageQueue.offer(infoBarConfiguration)
        if (messageQueue.size == 1) {
            processNextMessage()
        }
    }

    private fun processNextMessage() {
        if (messageQueue.peek() != null && infoBarContract != null) {
            val nextMessage = messageQueue.peek()
            infoBarContract?.showInfoBarNow(nextMessage, callback)
        }
    }

    fun resume(infoBarContract: InfoBarContract) {
        this.infoBarContract = infoBarContract
        if (!messageQueue.isEmpty()) {
            processNextMessage()
        }
    }

    fun pause() {
        infoBarContract = null
    }
}