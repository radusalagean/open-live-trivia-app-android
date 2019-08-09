package com.busytrack.openlivetrivia.generic.activity

import android.content.Intent
import androidx.annotation.IdRes
import com.busytrack.openlivetrivia.infobar.*

/**
 * The contract that open Activity-specific functionality to other components
 */
interface ActivityContract {

    /**
     * Show an on-screen message through the [InfoBar] ([TYPE_INFO] level)
     */
    fun showInfoMessage(message: Int, args: Any? = null)

    /**
     * Show an on-screen message through the [InfoBar] ([TYPE_WARN] level)
     */
    fun showWarningMessage(message: Int, args: Any? = null)

    /**
     * Show an on-screen message through the [InfoBar] ([TYPE_ERROR] level)
     */
    fun showErrorMessage(message: Int, args: Any? = null)

    /**
     * Start the Google Sign In flow
     */
    fun triggerGoogleSignIn(intent: Intent)

    /**
     * Get the main container for Fragments from a specific Activity
     */
    @IdRes fun getFragmentContainerId(): Int

    /**
     * Pop all fragments from the back stack
     */
    fun popAllFragments()

    // Show specific screens

    fun showAuthenticationScreen()
    fun showMainMenuScreen()
    fun showGameScreen()
    fun showLeaderboardScreen()
    fun showModerateReportsScreen()
    fun showSettingsFragment()

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
    fun handleLogOut()

    /**
     * Helper method which opens the passed [url] in the default web browser
     */
    fun openLinkInBrowser(url: String)
}