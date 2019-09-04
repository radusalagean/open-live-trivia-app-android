package com.busytrack.openlivetrivia.test

import androidx.test.espresso.IdlingResource

/**
 * Espresso idling resource which idles when the app is accepting user input
 */
object EspressoGlobalIdlingResource : IdlingResource {

    private val idlingResources = arrayListOf<EspressoIdlingContract>()

    // written from main thread, read from any thread.
    @Volatile private var resourceCallback: IdlingResource.ResourceCallback? = null
        set(value) {
            field = value
            // Register the callback with the known idling resources
            idlingResources.forEach {
                it.resourceCallback = value
            }
        }

    override fun getName(): String = javaClass.simpleName

    override fun isIdleNow(): Boolean {
        idlingResources.forEach {
            if (!it.isResourceIdle()) {
                return false
            }
        }
        return true
    }

    override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback?) {
        this.resourceCallback = callback
    }

    fun registerIdlingResource(resource: EspressoIdlingContract) {
        resource.resourceCallback = resourceCallback
        idlingResources += resource
    }

    fun unregisterIdlingResource(resource: EspressoIdlingContract) {
        idlingResources -= resource
        resourceCallback?.onTransitionToIdle()
    }
}