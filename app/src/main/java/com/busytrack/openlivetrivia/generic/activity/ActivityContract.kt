package com.busytrack.openlivetrivia.generic.activity

import android.content.Intent
import androidx.annotation.IdRes
import com.busytrack.openlivetrivia.infobar.InfoBar
import com.busytrack.openlivetrivia.infobar.TYPE_ERROR
import com.busytrack.openlivetrivia.infobar.TYPE_INFO
import com.busytrack.openlivetrivia.infobar.TYPE_WARN

/**
 * The contract that open Activity-specific functionality to other components
 */
interface ActivityContract {

    /**
     * Show an on-screen message through the [InfoBar] ([TYPE_INFO] level)
     */
    fun showInfoMessage(message: Int, vararg args: Any? = emptyArray())

    /**
     * Show an on-screen message through the [InfoBar] ([TYPE_WARN] level)
     */
    fun showWarningMessage(message: Int, vararg args: Any? = emptyArray())

    /**
     * Show an on-screen message through the [InfoBar] ([TYPE_ERROR] level)
     */
    fun showErrorMessage(message: Int, vararg args: Any? = emptyArray())

    /**
     * Start the Google Sign In flow
     */
    fun triggerGoogleSignIn(intent: Intent)

    /**
     * Get the main container for Fragments from a specific Activity
     */
    @IdRes fun getFragmentContainerId(): Int

    /**
     * Called when the Firebase authentication is successful
     */
    fun handleSuccessfulFirebaseLogIn()

    /**
     * Called when the Firebase authentication failed
     */
    fun handleFailedFirebaseLogIn(t: Throwable?)

    /**
     * Called when an authenticated user logged out
     */
    fun showLogOutMessage()

    /**
     * Helper method which opens the passed [url] in the default web browser
     */
    fun openLinkInBrowser(url: String)
}