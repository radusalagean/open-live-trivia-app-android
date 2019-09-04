package com.busytrack.openlivetrivia.test

import androidx.test.espresso.IdlingResource

/**
 * Espresso idling resource which idles when a game round is ongoing
 */
object EspressoRoundIdlingResource : IdlingResource {

    var contract: Contract? = null

    // written from main thread, read from any thread.
    @Volatile var resourceCallback: IdlingResource.ResourceCallback? = null

    override fun getName(): String = javaClass.simpleName

    override fun isIdleNow(): Boolean = contract?.isRoundOngoing() ?: false

    override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback?) {
        this.resourceCallback = callback
    }

    interface Contract {
        /**
         * Return true if the round is ongoing, false otherwise
         */
        fun isRoundOngoing(): Boolean
    }
}