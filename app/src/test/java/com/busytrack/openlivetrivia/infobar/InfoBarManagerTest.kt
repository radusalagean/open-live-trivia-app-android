package com.busytrack.openlivetrivia.infobar

import com.nhaarman.mockitokotlin2.*
import org.junit.After
import org.junit.Before

import org.junit.Assert.*
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class InfoBarManagerTest {

    @Mock
    private lateinit var infoBarContract: InfoBarContract

    private lateinit var infoBarManager: InfoBarManager

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        infoBarManager = InfoBarManager()
        infoBarManager.resume(infoBarContract)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun givenEmptyMessageQueue_whenEnqueueMessage_thenMessageIsProcessedImmediately() {
        // Prepare the queue
        val config = InfoBarConfiguration("Test message", TYPE_INFO)
        infoBarManager.enqueueMessage(config)

        // The message should be displayed once
        verify(infoBarContract, times(1)).showInfoBarNow(eq(config), any())
    }

    @Test
    fun givenPopulatedMessageQueue_whenEnqueueMessage_thenMessagesAreProcessedInOrder() {
        // Prepare the queue
        val config1 = InfoBarConfiguration("Message 1", TYPE_INFO)
        infoBarManager.enqueueMessage(config1)

        val config2 = InfoBarConfiguration("Message 2", TYPE_WARN)
        infoBarManager.enqueueMessage(config2)

        val config3 = InfoBarConfiguration("Message 3", TYPE_ERROR)
        infoBarManager.enqueueMessage(config3)

        // Simulate auto dismissing of the first message
        infoBarManager.callback.onDismissed(null, 0)

        // Simulate auto dismissing of the second message
        infoBarManager.callback.onDismissed(null, 0)

        // Check if the messages are shown in the correct order
        inOrder(infoBarContract) {
            verify(infoBarContract).showInfoBarNow(eq(config1), any())
            verify(infoBarContract).showInfoBarNow(eq(config2), any())
            verify(infoBarContract).showInfoBarNow(eq(config3), any())
        }
    }

    @Test
    fun givenPausedManagerAndPopulatedQueue_whenResume_thenTheRemainingMessagesAreProcessedAfterResume() {
        // Prepare the queue
        val config1 = InfoBarConfiguration("Message 1", TYPE_INFO)
        infoBarManager.enqueueMessage(config1)

        val config2 = InfoBarConfiguration("Message 2", TYPE_WARN)
        infoBarManager.enqueueMessage(config2)

        // Now the first message is being shown and the second message is waiting for the
        //  first one to disappear

        // Without giving enough time for the first one to disappear, pause the manager
        //  (i.e. pause the current Activity)
        infoBarManager.pause()

        // Time's up for the first message, it's being dismissed automatically
        infoBarManager.callback.onDismissed(null, 0)

        // Now, the second message should not be displayed, since the manager is paused
        verify(infoBarContract, never()).showInfoBarNow(eq(config2), any())

        // Let's resume the manager (i.e. resume the Activity)
        infoBarManager.resume(infoBarContract)

        // The second message from the queue should now be shown
        inOrder(infoBarContract) {
            verify(infoBarContract).showInfoBarNow(eq(config1), any())
            verify(infoBarContract).showInfoBarNow(eq(config2), any())
        }
    }

    @Test
    fun givenPausedManagerAndPopulatedQueue_whenAddingMessagesInPausedState_thenMessagesAreProcessed() {
        // Prepare the queue
        val config1 = InfoBarConfiguration("Message 1", TYPE_INFO)
        infoBarManager.enqueueMessage(config1)

        // Without giving enough time for the first message to disappear, pause the manager
        //  (i.e. pause the current Activity)
        infoBarManager.pause()

        // Add two more messages while in this paused state
        val config2 = InfoBarConfiguration("Message 2", TYPE_WARN)
        infoBarManager.enqueueMessage(config2)
        val config3 = InfoBarConfiguration("Message 3", TYPE_ERROR)
        infoBarManager.enqueueMessage(config3)

        // While we are in the paused state, the first message expires
        infoBarManager.callback.onDismissed(null, 0)

        // Resume the manager
        infoBarManager.resume(infoBarContract)

        // The second message is shown on resume and dismissed after it expires
        infoBarManager.callback.onDismissed(null, 0)

        // The messages should be processed in order

        inOrder(infoBarContract) {
            verify(infoBarContract).showInfoBarNow(eq(config1), any())
            verify(infoBarContract).showInfoBarNow(eq(config2), any())
            verify(infoBarContract).showInfoBarNow(eq(config3), any())
        }
    }

    @Test
    fun givenOneMessageInQueue_whenPausingAndResumingImmediately_thenThePreviousMessageIsDisplayedAgain() {
        // Prepare the queue
        val config1 = InfoBarConfiguration("Message 1", TYPE_INFO)
        infoBarManager.enqueueMessage(config1)

        // Pause the manager
        infoBarManager.pause()

        // Resume quickly, without giving enough time for the message to expire
        infoBarManager.resume(infoBarContract)

        // The Message should be displayed again
        //  (2 times in total: once before pause and once after resume)
        verify(infoBarContract, times(2)).showInfoBarNow(eq(config1), any())
    }

    @Test
    fun givenOneMessageInQueue_whenPausingAndResumingAfterAWhile_thenThePreviousMessageIsNotDisplayedAnymore() {
        // Prepare the queue
        val config1 = InfoBarConfiguration("Message 1", TYPE_INFO)
        infoBarManager.enqueueMessage(config1)

        // Pause the manager
        infoBarManager.pause()

        // In the meantime, the message expires
        infoBarManager.callback.onDismissed(null, 0)

        // Resume the manager
        infoBarManager.resume(infoBarContract)

        // The message should be displayed exactly one time (before the manager is paused)
        verify(infoBarContract, times(1)).showInfoBarNow(eq(config1), any())
    }
}