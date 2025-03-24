package com.busytrack.openlivetrivia.test

import androidx.test.espresso.IdlingResource
import java.util.concurrent.atomic.AtomicInteger

/**
 * Espresso Idling Contract, implement in classes that handle blocking calls
 * (network, computation, etc)
 */
interface EspressoIdlingContract {
    /**
     * Incremented when a blocking event is occurring (network call, blocking computation call, etc.)
     * Decremented when a previously counted blocking event is finished
     * When the counter reaches 0, the resource is considered idle
     */
    val blockingEventCounter: AtomicInteger

    /**
     * Override and set the initial value:
     * - true, if the idling resource doesn't need initialization
     * - false, if the idling resource needs to be initialized by loading data
     *
     * The flag will be set to true when [blockingEventCounter] is decremented to 0
     */
    var idlingResourceInitialized: Boolean

    /**
     * Callback for notifying Espresso about transitions to idle state
     */
    var resourceCallback: IdlingResource.ResourceCallback?

    /**
     * Returns true if the resource is idle, false otherwise
     */
    fun isResourceIdle(): Boolean = blockingEventCounter.get() == 0 && idlingResourceInitialized

    /**
     * Call when an asynchronous event is occurring
     */
    fun incrementBlockingEventCounter() {
        blockingEventCounter.incrementAndGet()
    }

    /**
     * Call when a previously counted blocking event is finished
     */
    fun decrementBlockingEventCounter() {
        idlingResourceInitialized = true
        val current = blockingEventCounter.decrementAndGet()
        if (current == 0) {
            // The app should be idle now
            resourceCallback?.onTransitionToIdle()
        } else check(current >= 0) {
            "blockingEventCounter went below 0"
        }
    }

    /**
     * If [loading] is true, [incrementBlockingEventCounter] is called.
     * Else, [decrementBlockingEventCounter] is called.
     */
    fun handleCounterByLoadingState(loading: Boolean) {
        if (loading) {
            incrementBlockingEventCounter()
        } else {
            decrementBlockingEventCounter()
        }
    }
}